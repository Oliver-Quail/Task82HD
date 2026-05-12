import os
from flask import Flask, request, jsonify
import re
import requests
import ollama

app = Flask(__name__)

OLLAMA_URL = "http://localhost:11434/api/generate"
MODEL_NAME = "gemma4:e2b" 

def generate_classification(student_topic):
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
        },
    ])

    print(response.message.content)

    


    return response.message.content

def filter(prompt):
    bannedPhrases = ["escape", "hack"]

    if(prompt in bannedPhrases):
        return False
    
    return True
    




@app.route('/test', methods=['GET'])
def run_test():
    return jsonify({'quiz': "test"}), 200


if __name__ == '__main__':
    port_num = 5000
    print(f"App running on port {port_num}")
    app.run(port=port_num, host="0.0.0.0")