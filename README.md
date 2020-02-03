# torvid

In order to build torvid, you're gonna need to install some stuff.

ffmpeg (used to re-encode the video files):

    sudo apt install ffmpeg

Bento4 (used to fragment files and generate dash manifest)

    https://www.bento4.com/downloads/

WebOS SDK (optional since you can run the app files directly in your computer).

    http://webostv.developer.lge.com/


Steps to run the app:

Open the webos CLI

Set the emulator as device
    ares-setup-device

cd to torvid's app folder

Package the app in order to generate the .ipk file
    ares-package ./first-app

Install it into the emulator
    ares-install --device <device-name> ./<generated .ipk file>


If you nedd to check installed apps on device
    ares-install --device <device-name> --list

Launch app
    ares-launch --device <device-name> <app-domain>


Architecture Diagram (Gotta be updated tho)
![Alt text](docs/torvid-sketch.png?raw=true "Architecture Diagram")