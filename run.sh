#!/bin/bash
#
# Script     : run.sh
# Usage      : ./run.sh /path/to/output_dir
# Author     : Giuseppe Totaro
# Date       : 02/05/2015 [MM-DD-YYYY]
# Last Edited: 
# Description: This scripts runs the StringsParser over some govdocs1 files. It 
#              saves all output files in the given directory.
# Notes      : Run this script from its folder by typing ./run.sh
#

if [ $# -lt 1 ] || [ ! -d $1 ]
then
	echo "Usage: $0 /path/to/output_dir (output_dir must be a directory!)"
	printf "\n\tExample: $0 /tmp/output\n"
	exit 1
fi

DATA_DIR="govdocs1/016"
OUT_DIR=$1
EXT="tika"

if [ ! -d $DATA_DIR ]
then
	echo "Error: input data not found!"
	echo "Please provide "016" govdocs1 set files in the "govdocs1" folder and try again."
	exit 1
fi

for file in $DATA_DIR/*
do
	java -cp ./:./lib/tika-app-1.8-SNAPSHOT.jar:./bin TestStringsParser ${file} $OUT_DIR/$(basename $file).$EXT >>strings_parsers.log 2>&1 
done
