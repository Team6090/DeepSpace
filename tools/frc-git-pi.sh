#!/bin/bash

#
# Variables
#

# The latest supported version of PiCore to use.
LATEST_PICORE_VERSION=9.0.3
# The remote to fetch our resources from.
REMOTE="http://www.tinycorelinux.net/9.x/armv6"
# The archive file
LOCAL_ZIP="piCore-$LATEST_PICORE_VERSION.zip"
# The latest PiCore image archive
LATEST_PICORE_IMG="$REMOTE/releases/RPi/$LOCAL_ZIP"
# The repository to fetch applications from
REPO="$REMOTE/tcz"

# The PiCore image file
PICORE_IMG="piCore-$LATEST_PICORE_VERSION.img"
# The directory to mount the data partition on.
DATA_DIR="data"
# The loop device to use for the data partition.
DATA_LOOP="/dev/loop1"

#
# tcget: Download a TinyCore app and its dependencies.
# This will update existing apps and dependencies too.
#
function tcget() {
	if [ ! -f "$1.tcz" ]; then
		wget "$REPO/$1.tcz" -O "$1.tcz"
		wget "$REPO/$1.tcz.dep" -O "$1.tcz.dep"
		wget "$REPO/$1.tcz.md5.txt" -O "$1.tcz.md5.txt"
		if [ -f "$1.tcz.dep" ]; then
			while read -r line; do
				DEP=$(echo "$line" | rev | cut -c 5- | rev)
				tcget $DEP
			done < "$1.tcz.dep"
		fi
	else
		echo "File $1 exists, assuming the dependency is satisfied."
	fi
}

if [ ! -f "$LOCAL_ZIP" ]; then
	wget "$LATEST_PICORE_IMG"
fi

# Extract just the PiCore image
unzip -o "$LOCAL_ZIP"
rm "$LOCAL_ZIP" README $PICORE_IMG.md5.txt

# Get information on the data partition.
FDISK_OUTPUT=$(fdisk -lu "$PICORE_IMG" -o Device,Start)
SECTOR_SIZE=$(echo "$FDISK_OUTPUT" | grep "Units:" | cut -d "=" -f 2 | cut -c 2- | cut -d " " -f 1)

DATA_START=$(echo "$FDISK_OUTPUT" | grep ".img2" | cut -d " " -f 2)
DATA_OFFSET=$(expr $SECTOR_SIZE \* $DATA_START)

# Expand the PiCore image by 50 MB
dd if=/dev/zero bs=1M count=50 >> "$PICORE_IMG"

# Expand the data partition to fill the PiCore image.
(
echo d
echo 2
echo n
echo p
echo 2
echo $DATA_START
echo
echo N
echo w
) | fdisk "$PICORE_IMG"

if [ -d "$MNT_DIR" ]; then
	echo "The data directory exists. An image may be mounted."
	exit 1
fi

# Set up the loop device
losetup -o $DATA_OFFSET "$DATA_LOOP" "$PICORE_IMG"
# Resize the data partition's filesystem
e2fsck "$DATA_LOOP"
resize2fs "$DATA_LOOP"
# Mount the data partition.
mkdir "$DATA_DIR"
mount -o loop,rw,sync "$DATA_LOOP" "$DATA_DIR"
# Change into the data directory
cd "$DATA_DIR/tce"
# Only load essential applications
echo -e "openssh.tcz\ngit.tcz" > onboot.lst
# Change into the applications directory
cd optional
# Remove all the current packages
rm *
# Fetch the required services:
# - OpenSSH
# - Git
tcget "openssh"
tcget "git"
cd ..
# Extract the custom data
gzip -d mydata.tgz
tar -xf mydata.tar
rm mydata.tar
#
# Perform modifications
#
# Add the git user
mkdir home/git
echo "git:x:1002:51:Git,,,:/home/git:/bin/sh" >> etc/passwd
echo "git::16435:0:99999:7:::" >> etc/shadow
# Set the MOTD
echo "FRC-Git-Pi - Created by Jordan Bancino" > etc/motd
# Set the hostname
echo "frc-git-pi" > etc/hostname
sed -i -e 's/sethostname box/sethostname frc-git-pi/g' opt/bootsync.sh

# Repository configuration - bootsync.sh is run at system startup.
echo "if [ ! -d \"/mnt/mmcblk0p2/repo\" ]; then" >> opt/bootsync.sh
echo "  mkdir /mnt/mmcblk0p2/repo" >> opt/bootsync.sh
echo "  cd mnt/mmcblk0p2/repo" >> opt/bootsync.sh
echo "  git init --bare" >> opt/bootsync.sh
echo "  echo \"#!/bin/sh\" > hooks/post-receive" >> opt/bootsync.sh
echo "  echo \"echo \"Syncing flash...\"\" >> hooks/post-receive" >> opt/bootsync.sh
echo "  echo \"sync\" >> hooks/post-receive" >> opt/bootsync.sh
echo "  echo \"echo \"Synced to flash.\"\" >> hooks/post-receive" >> opt/bootsync.sh
echo "  chmod 777 hooks/post-receive" >> opt/bootsync.sh
echo "  chown git /mnt/mmcblk0p2/repo -R" >> opt/bootsync.sh
echo "fi" >> opt/bootsync.sh
echo "ln -s /mnt/mmcblk0p2/repo /home/git/repo" >> opt/bootsync.sh

# Generate SSH keys (so PiCore doesn't have to re-generate them on each boot)
mkdir -p usr/local/etc/ssh
ssh-keygen -t rsa -N "" -f usr/local/etc/ssh/ssh_host_rsa_key
ssh-keygen -t dsa -N "" -f usr/local/etc/ssh/ssh_host_dsa_key
ssh-keygen -t ecdsa -N "" -f usr/local/etc/ssh/ssh_host_ecdsa_key
ssh-keygen -t ed25519 -N "" -f usr/local/etc/ssh/ssh_host_ed25519_key

#
# Generate the SSHD configuration.
#
# These options were pulled directly from the TinyCore openssl squashfs
echo "AuthorizedKeysFile .ssh/authorized_keys" > usr/local/etc/ssh/sshd_config
echo "UsePrivilegeSeparation no" >> usr/local/etc/ssh/sshd_config
echo "Subsystem sftp /usr/local/libexec/sftp-server" >> usr/local/etc/ssh/sshd_config
# These are custom options
echo "AllowUsers tc git" >> usr/local/etc/ssh/sshd_config
echo "PermitEmptyPasswords yes" >> usr/local/etc/ssh/sshd_config

# Re-pack the data archive
tar -cf mydata.tar etc home opt usr
gzip -c mydata.tar > mydata.tgz
rm mydata.tar
rm etc home opt usr -r
cd ../../

# Unmount the data partition
umount -l "$DATA_DIR"
rm -r "$DATA_DIR"
losetup -d "$DATA_LOOP"

mv "$PICORE_IMG" "frcGitPi-1.0.0.img"
