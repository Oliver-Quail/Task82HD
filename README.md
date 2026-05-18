# Task 8.2HD
This is an app


## On device setup
Download from here: https://huggingface.co/litert-community/gemma-4-E2B-it-litert-lm/tree/main \\
PLEASE change the name of ```gemma-4-E2B-it.litertlm``` to ```gemma_4_e2b_it.litertlm``` \\
Then place it in ```main/assets folder``` \\
## remote
Firstly change the ip address in the ```WebProvider``` to the ip address of your server. Then change the ip address in ```xml/network_security.xml``` \\
Once that is completed install the following packages in a virtual enviroement inside the server folder. \\
- Ollama \\
- requests \\
- flask \\

Then run the following command to start the server: \\
```python3 main.py```\\
Please note you will also need to install Ollama local on your system for this to work. Along with Gemma4:e2b. \\
If you would like to change these, please change there two parameters : ```OLLAMA_URL``` and ```MODEL_NAME```. Note, any model change should be a vision model.

## How to run
Basically install everything in build.gradle.kts on a device with Android API 35+. \\
Please also ensure your emulator has aleast 8gb RAM, 20GB space (30GB recommend this is due to cache file size during install). \\

## LLM intergration 
This app uses both OnDevice and remote LLMs for classification. The app uses images and text to advice you on how to properly dispose of waste. Also stores all of this data
