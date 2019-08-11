from flask import Flask, send_file
from flask_cors import CORS, cross_origin

app = Flask(__name__)
CORS(app)

@app.route('/')
def hello_world():
    return 'Hello, World!'

@app.route('/video', methods=['GET'])
@cross_origin(origin='localhost',headers=['Content-Type','Authorization'])
def video():
    return send_file('../video-in/attack-fragmented.mp4')