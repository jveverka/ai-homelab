
## 0. Prereq
* ollama is installed, download model qwen 3.8 flash-next model  
  ```ollama pull qwen3.8-flash-next:125b-a6b-q4_K_M```
* stop all running ollama models
* install llama.cpp
```bash
sudo apt update
sudo apt install -y \
    git cmake ninja-build build-essential \
    libcurl4-openssl-dev

cd ~
git clone https://github.com/ggml-org/llama.cpp
cd llama.cpp

cmake -S . -B build \
    -G Ninja \
    -DCMAKE_BUILD_TYPE=Release \
    -DGGML_CUDA=ON \
    -DCMAKE_CUDA_COMPILER=/usr/local/cuda/bin/nvcc \
    -DCUDAToolkit_ROOT=/usr/local/cuda \
    -DCMAKE_CUDA_ARCHITECTURES=120a

cmake --build build --parallel 8
./build/bin/llama-server --list-devices    
```

## 1. start llama.cpp server
```bash
ollama show --modelfile \
    qwen3.8-flash-next:125b-a6b-q4_K_M_1M

export MODEL="/usr/share/ollama/.ollama/models/blobs/sha256-89059c880c2f000b24520919d4ba2edd2a8101327c7f8ab33b2280e01addda75"
cd ~/llama.cpp
./build/bin/llama-server \
    --model "$MODEL" \
    --alias "qwen3.8-flash-next:125b-a6b-q4_K_M" \
    --host 0.0.0.0 \
    --port 8080 \
    --ctx-size 262144 \
    --n-gpu-layers 20 \
    --split-mode layer \
    --tensor-split 1,1 \
    --threads 16 \
    --parallel 1 \
    --flash-attn on \
    --cache-type-k q8_0 \
    --cache-type-v q8_0
```

## 2. Connect agents to llama.cpp server
```bash
qwen \
   --auth-type openai \
   --model qwen3.8-flash-next:125b-a6b-q4_K_M_1M \
   --approval-mode yolo \
   --openai-api-key local \
   --openai-base-url http://192.168.55.100:8080/v1
```