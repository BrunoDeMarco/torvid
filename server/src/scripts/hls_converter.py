#Gotta check actual codecs and containers of video file.
#If different from supported ones, reencode them.

#Convert to HLS into 'converted' folder.
import subprocess
import sys
from subprocess import PIPE

video_codec = subprocess.run(["ffprobe.exe", "-v", "error", "-select_streams", "v:0", "-show_entries", "stream=codec_name", "-of", "default=noprint_wrappers=1:nokey=1", "videod.mp4"], stdout=PIPE, stderr=PIPE)
audio_codec = subprocess.run(["ffprobe.exe", "-v", "error", "-select_streams", "a:0", "-show_entries", "stream=codec_name", "-of", "default=noprint_wrappers=1:nokey=1", "videod.mp4"], stdout=PIPE, stderr=PIPE)

print(str(video_codec.stdout))
print(str(audio_codec.stdout))

filename = str(sys.argv[1])
foldername = filename.split(".")[0]

unconverted_folder_relative_path = "../../content/unconverted/"
converted_folder_relative_path = "../../content/converted/"

subprocess.run(["mp4hls", "-o", converted_folder_relative_path + foldername, unconverted_folder_relative_path + filename])