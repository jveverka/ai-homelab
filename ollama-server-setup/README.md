# Ollama Server setup
```
sudo apt update
sudo apt upgrade
```

### Download Models
```
ollama pull qwen3.8:27b
ollama pull qwen3.8:latest
ollama pull gemma4:latest
ollama pull qwen3.6:latest
ollama pull muse-glimmer:30b
```

### Modify Models
```
ollama create qwen3.8:27b_1M -f Modelfile-qwen3.8_1M
```

### Show Models
```
ollama ls 
```
