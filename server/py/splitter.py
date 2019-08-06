#!/usr/bin/env python
from __future__ import unicode_literals, print_function
import ffmpeg
import sys

try:
    probe = ffmpeg.probe("../video-in/attack.mp4")
except ffmpeg.Error as e:
    print(e.stderr, file=sys.stderr)
    sys.exit(1)

segment_duration = 10
video_stream = next((stream for stream in probe['streams'] if stream['codec_type'] == 'video'), None)
if video_stream is None:
    print('No video stream found', file=sys.stderr)
    sys.exit(1)

duration = float(video_stream['duration'])

for x in range(int(duration // segment_duration)):
    ffmpeg.input('../video-in/attack.mp4', ss=x*segment_duration, t=segment_duration).output('out' + str(x) + '.mp4').run()

