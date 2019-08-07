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

int_segments = int(duration // segment_duration)
last_segment_duration = duration % segment_duration

for x in range(int_segments):
    ffmpeg.input('../video-in/attack.mp4', ss=x*segment_duration, t=segment_duration).output('out' + str(x) + '.mp4').run()

if last_segment_duration > 1:
    print('from' + str((int_segments+1) * segment_duration))
    print('for ' + str(last_segment_duration) + 'secs')
