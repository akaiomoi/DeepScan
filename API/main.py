from moderation import * #From files this project
import string
from multiprocessing import cpu_count

from flask import Flask, request, jsonify

app = Flask(__name__)

@app.route('/message/review', methods=['POST'])
def review_message():
    # Get the message from the request JSON
    data = request.get_json()
    message = data.get('message')

    if not message:
        print("[API] Invalid Request!")
        return "{\"error\":\"no message\"}"

    print('[TorchModel] Processing text \"' + message + '\"')
    text = leet_to_text(message)

    embeddings_for_prediction = getEmb(text)
    prediction = predict(moderation, embeddings_for_prediction)

    return json.dumps(prediction,indent=4)


def leet_to_text(leet_str):
    leet_dict = {'4': 'a', '8': 'b', '(': 'c', '|)': 'd', '3': 'e', '|=': 'f', '6': 'g', '|-|': 'h',
                 '1': 'i', '_|': 'j', '|<': 'k', '|_': 'l', '|\/|': 'm', '|\\|': 'n', '0': 'o', '|2': 'r',
                 '5': 's', '7': 't', '|_|': 'u', '\/': 'v', '\/\/': 'w', '><': 'x', '`/': 'y', '2': 'z'}

    text_str = ''
    i = 0
    while i < len(leet_str):
        if leet_str[i:i + 2] in leet_dict:
            text_str += leet_dict[leet_str[i:i + 2]]
            i += 2
        elif leet_str[i:i + 1] in leet_dict:
            text_str += leet_dict[leet_str[i:i + 1]]
            i += 1
        else:
            text_str += leet_str[i]
            i += 1
    return text_str


if __name__ == '__main__':
    # Load model
    moderation = ModerationModel()
    if torch.cuda.is_available():
     print('[TorchModel] Running On CUDA!')
     device = "cuda:0"
    else:
     print('[TorchModel] Running On CPU!')
     device = "cpu"


    moderation.load_state_dict(torch.load('moderation_model.pth', map_location=torch.device(device)))

    app.run(debug=True)
