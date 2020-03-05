#!/bin/bash
DIR="../../content/unconverted"
inotifywait -m -r "$DIR" -e create -e moved_to | while read path action file;

do
    python3 hls_converter.py $file
done
