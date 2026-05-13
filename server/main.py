import os
from flask import Flask, request, jsonify
import re
import requests
import ollama

OLLAMA_URL = "http://localhost:11434/api/generate"
MODEL_NAME = "gemma4:e2b" 
UPLOAD_FOLDER = './static/'

app = Flask(__name__)
app.config["UPLOAD_FOLDER"] = UPLOAD_FOLDER



def generate_classification(text, image):
    print("Fetching quiz from Ollama")

    prompt = (
        f"Classify the following, using an image if avaliable and text. Respond with what bin it should be disposed of in.\n"
        f"For green waste, respond green"
        f""
    )


    response = ollama.chat(model=MODEL_NAME, messages=[
        {
            'role': 'user',
            'content': prompt,
            'images': ["http://192.168.50.179:5000/static/" + image]
        },
    ])

    print(response.message.content)

    


    return response.message.content

def filter(prompt):
    bannedPhrases = ["escape", "hack"]

    if(prompt in bannedPhrases):
        return False
    
    return True
    
@app.route("/classify", methods=['POST'])
def classify_file():
    print("classifed fiel recieved")
    
    
    
    if "image" not in request.files:
        return 404
    file = request.files['image']
    print("file found")
    filename = file.filename
    file.save(os.path.join(app.config['UPLOAD_FOLDER'], filename))

    

    
    return jsonify({'classification': generate_classification("Meow", filename)}), 200

    



@app.route('/test', methods=['GET'])
def run_test():
    return jsonify({'quiz': "test"}), 200


if __name__ == '__main__':
    port_num = 5000
    print(f"App running on port {port_num}")
    app.run(port=port_num, host="0.0.0.0")