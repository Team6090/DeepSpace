#!/bin/bash
echo "--------------------------------------"
echo "RIO-Network: Connect to an FRC RoboRIO"
echo "--------------------------------------"

#
# Simple dhcp server, written in python.
# Note that this server is not my work.
#
DHCPD_SERVER="simple-dhcpd"
DHCPD_SERVER_URL="https://raw.githubusercontent.com/javier-lopez/learn/master/python/tools/$DHCPD_SERVER"

# Most network operation commands must be run as root.
echo "Checking permissions..."
if [ "$(whoami)" != "root" ]; then
	echo "This script must be run as root!"
	exit 1
fi

# This command is used to check for the existance of network interfaces, and their status (up or down)
if [ ! -x "$(command -v ifplugstatus)" ]; then
	echo "ifplugd not installed. Installing..."
	apt install ifplugd -y
fi

# Check that the user provided an interface.
echo "Checking interface..."
if [ "$1" == "" ]; then
	echo "No interface provided. Please provide an interface."
	exit 1
fi

# Check that the user-provided interface exists and is online.
INTERFACE_FOUND=`ifplugstatus | grep "$1:"`
if [ -n "$INTERFACE_FOUND" ]; then
	echo "Interface exists!"
	INTERFACE=$1
	echo "Checking connection..."
	if [ "$(ifplugstatus | grep $INTERFACE: | cut -d ":" -f 2 | cut -c 2-)" == "link beat detected" ]; then
		echo "Connection established."
	else
		echo "No connection."
		exit 1
	fi
else
	echo "Interface $1 does not exist."
	exit 1
fi

#
# Configure the network and start the DHCP server
#
echo "Setting up the RoboRIO on interface $INTERFACE..."
echo "- Flushing current configuration..."
ifconfig $INTERFACE 192.222.11.1 netmask 255.255.255.0
echo "- Assigning an IP..."
ip addr add 172.22.11.1/24 dev $INTERFACE
# Download simple-dhcpd
if [ ! -f $DHCPD_SERVER ]; then
	echo "- Downloading simple-dhcpd..."
	wget $DHCPD_SERVER_URL
fi
echo "- Starting DHCP server..."
python $DHCPD_SERVER -a 172.22.11.1 -i $INTERFACE -f 172.22.11.2 -t 172.22.11.2

