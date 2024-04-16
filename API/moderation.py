import json
import os
import torch
import torch.nn as nn
import torch.optim as optim
import torch.nn.functional as F
from torch.utils.data import Dataset, DataLoader

from transformers import AutoTokenizer, AutoModel

device = "cuda" if torch.cuda.is_available() else "cpu"

if torch.cuda.is_available():
 device = "cuda:0"
else:
 device = "cpu"
 

tokenizer_embeddings = AutoTokenizer.from_pretrained('sentence-transformers/paraphrase-multilingual-MiniLM-L12-v2')
model_embeddings = AutoModel.from_pretrained('sentence-transformers/paraphrase-multilingual-MiniLM-L12-v2').to(device)

class ModerationModel(nn.Module):
    def __init__(self):
        input_size = 384
        hidden_size = 128
        output_size = 11
        super(ModerationModel, self).__init__()
        self.fc1 = nn.Linear(input_size, hidden_size)
        self.fc2 = nn.Linear(hidden_size, output_size)

    def forward(self, x):
        x = F.relu(self.fc1(x))
        x = self.fc2(x)
        return x

def mean_pooling(model_output, attention_mask):
    token_embeddings = model_output[0]
    input_mask_expanded = attention_mask.unsqueeze(-1).expand(token_embeddings.size()).float()
    return torch.sum(token_embeddings * input_mask_expanded, 1) / torch.clamp(input_mask_expanded.sum(1), min=1e-9)

def getEmbeddings(sentences):
    encoded_input = tokenizer_embeddings(sentences, padding=True, truncation=True, return_tensors='pt').to(device)
    with torch.no_grad():
        model_output = model_embeddings(**encoded_input)
    sentence_embeddings = mean_pooling(model_output, encoded_input['attention_mask'])
    return sentence_embeddings.cpu()

def getEmb(text):
    sentences = [text]
    sentence_embeddings = getEmbeddings(sentences)
    return sentence_embeddings.tolist()[0]

def predict(model, embeddings):
    model.eval()
    with torch.no_grad():
        embeddings_tensor = torch.tensor(embeddings, dtype=torch.float)
        outputs = model(embeddings_tensor.unsqueeze(0))
        predicted_scores = torch.sigmoid(outputs)
        predicted_scores = predicted_scores.squeeze(0).tolist()
        category_names = ["harassment", "harassment-threatening", "hate", "hate-threatening", "self-harm", "self-harm-instructions", "self-harm-intent", "sexual", "sexual-minors", "violence", "violence-graphic"]

        result = {category: score for category, score in zip(category_names, predicted_scores)}
        detected = {category: score > 0.5 for category, score in zip(category_names, predicted_scores)}
        detect_value = any(detected.values())

        return {"category_scores": result, 'detect': detected, 'detected': detect_value}
