from flask import Flask, send_file, make_response
from flask_cors import CORS, cross_origin

app = Flask(__name__)
CORS(app)

@app.route('/')
def hello_world():
    return 'Hello, World!'

@app.route('/video', methods=['GET'])
@cross_origin(origin='localhost',headers=['Content-Type','Authorization'])
def video():
    return send_file('../assets/game_of_thrones/got_fragmented.mp4')

@app.route('/dash.mpd', methods=['GET'])
@cross_origin(origin='localhost',headers=['Content-Type','Authorization'])
def dash():
    response = make_response(send_file('../assets/attack_on_eleicoes_demo/output/stream.mpd'))
    response.headers['Content-Type'] = 'application/dash+xml'
    return response

@app.route('/init.mp4', methods=['GET'])
@cross_origin(origin='localhost',headers=['Content-Type','Authorization'])
def init():
    response = make_response(send_file('../assets/attack_on_eleicoes_demo/output/video/avc1/init.mp4'))
    response.headers['Content-Type'] = 'video/mp4'
    return response
    