#!/bin/bash
HERE="`dirname \"$0\"`"
cd $HERE
mosquitto -d -c conf/mosquitto.conf
