from app import app
import os

@app.route('/')
@app.route('/index')
def index():
    dire = next(os.walk('./../../content/converted'))[1]
    return str(dire)