# Ollama Server setup
```
sudo apt update
sudo apt upgrade
```
Install ollama server
```bash
curl -fsSL https://ollama.com/install.sh | sh
```

### Configure ollama server
```bash
sudo vi /etc/systemd/system/ollama.service.d/override.conf

# insert lines
[Service]
Environment="OLLAMA_HOST=0.0.0.0:11434"
Environment="OLLAMA_KEEP_ALIVE=-1"
```
```bash
sudo systemctl daemon-reload
sudo systemctl restart ollama
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

### Models
* [qwen3.8](https://ollama.com/library/qwen3.8)
* [qwen3.8-flash-next](https://ollama.com/library/qwen3.8-flash-next)
