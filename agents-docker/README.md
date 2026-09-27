# Agents Docker
Running AI agents in docker container isolates agents from your host PC so they can see only selected directory.
This docker image contains basic tools for AI agents and has installed AI agents inside:
* [Qwen Code CLI](https://qwen.ai/qwencode)
* [Claude Code CLI](https://claude.com/product/claude-code)
* [Open Code CLI](https://opencode.ai/)

## Build Docker
```bash
docker build -t ubuntu-agents -f Dockerfile-j25 .
```

## tmux cheat sheet
```bash
tmux (ctrl b + d)  # <- start new tmux session and detach 
tmux ls            # <- list running sessions
tmux a             # <- reattach to running session
```

## Run in interactive mode
```bash
docker run -it --name ubuntu-agents --rm \
  --privileged \
  -v /home/juraj/Data/Projects/private-dc/ai-homelab:/data \
  ubuntu-agents
```

## Run in daemon mode
```bash
docker run --name ubuntu-agents -d \
  --privileged \
  -v /home/juraj/Data/Projects/private-dc/ai-homelab:/data \
  ubuntu-agents
```
Connect inside `ubuntu-agents` container.
```bash
docker exec -it ubuntu-agents bash  
```

## Run Agents in Docker

### Run QwenCode in terminal
```bash
qwen --auth-type openai --model qwen3.8:27b-1M \
     --approval-mode yolo \
     --openai-api-key ollama \
     --openai-base-url http://192.168.44.102:11434/v1
```

### Run QwenCode as web page
```bash
export OPENAI_API_KEY=ollama
export OPENAI_BASE_URL=http://192.168.44.102:11434/v1
export OPENAI_MODEL='qwen3.8-flash-next:125b-a6b-q4_K_M_1M'

qwen serve \
    --hostname 0.0.0.0 \
    --port 4170
```

### Run ClaudeCode
```bash
ANTHROPIC_AUTH_TOKEN=ollama
ANTHROPIC_BASE_URL=http://192.168.44.102:11434
OLLAMA_CONTEXT_LENGTH=64000

claude --model qwen3.6-1M
claude --model qwen3.8:27b-1M
```

### Run OpenCode in Terminal
```bash
OPENCODE_CONFIG_CONTENT='{"provider":{"ollama":{"npm":"@ai-sdk/openai-compatible","name":"Ollama","options":{"baseURL":"http://192.168.44.102:11434/v1"},"models":{"qwen3.8:27b":{"tools":true,"limit":{"context":131072,"output":16384}}}}},"model":"ollama/qwen3.8:27b-1M"}'
opencode
```

### Run OpenCode as web page
```bash
export OPENCODE_SERVER_PASSWORD='IamFuckingLegend2026'
export OPENCODE_CONFIG_CONTENT='{"provider":{"ollama":{"npm":"@ai-sdk/openai-compatible","name":"Ollama","options":{"baseURL":"http://192.168.44.102:11434/v1"},"models":{"qwen3.8-flash-next:125b-a6b-q4_K_M_1M":{"tools":true,"limit":{"context":131072,"output":16384}}}}},"model":"ollama/qwen3.8-flash-next:125b-a6b-q4_K_M_1M"}'
opencode web \
    --hostname 0.0.0.0 \
    --port 4096
```