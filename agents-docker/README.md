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

### Run QwenCode
```bash
qwen --auth-type openai --model qwen3.8:27b-1M \
     --approval-mode yolo \
     --openai-api-key ollama \
     --openai-base-url http://192.168.44.102:11434/v1
```

### Run OpenCode
```bash
OPENCODE_CONFIG_CONTENT='{"provider":{"ollama":{"npm":"@ai-sdk/openai-compatible","name":"Ollama","options":{"baseURL":"http://192.168.44.102:11434/v1"},"models":{"qwen3.8:27b":{"tools":true,"limit":{"context":131072,"output":16384}}}}},"model":"ollama/qwen3.8:27b-1M"}'
opencode
```

### Run ClaudeCode
```bash
ANTHROPIC_AUTH_TOKEN=ollama
ANTHROPIC_BASE_URL=http://192.168.44.102:11434
OLLAMA_CONTEXT_LENGTH=64000

claude --model qwen3.6-1M
claude --model qwen3.8:27b-1M
```
