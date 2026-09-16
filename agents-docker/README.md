# Agents Docker
This docker image contains basic tools for AI agents and has installed AI agents inside:
* [Qwen Code CLI](https://qwen.ai/qwencode)
* [Claude Code CLI](https://claude.com/product/claude-code)
* [Open Code CLI](https://opencode.ai/)

## Build Docker
```bash
docker build -t ubuntu-java25-maven-agents -f Dockerfile-j25 .
```

## Run in interactive mode
```bash
docker run -it --name ubuntu-java25-maven-agents --rm \
  --privileged \
  -v /home/juraj/Data/Private/ai-home-lab:/data \
  ubuntu-java25-maven-agents
```

## Run in daemon mode
```bash
docker run --name ubuntu-java25-maven-agents -d \
  --privileged \
  -v /home/juraj/Data/Private/ai-home-lab:/data \
  ubuntu-java25-maven-agents
```

## Run QwenCode
```bash
qwen --auth-type openai --model qwen3.8:27b-1M \
     --approval-mode yolo \
     --openai-api-key ollama \
     --openai-base-url http://192.168.44.102:11434/v1
```

## Run OpenCode
```bash
OPENCODE_CONFIG_CONTENT='{"provider":{"ollama":{"npm":"@ai-sdk/openai-compatible","name":"Ollama","options":{"baseURL":"http://192.168.44.102:11434/v1"},"models":{"qwen3.8:27b":{"tools":true,"limit":{"context":131072,"output":16384}}}}},"model":"ollama/qwen3.8:27b-1M"}'
opencode
```
## Run ClaudeCode
```bash
ANTHROPIC_AUTH_TOKEN=ollama
ANTHROPIC_BASE_URL=http://192.168.44.102:11434
OLLAMA_CONTEXT_LENGTH=64000

claude --model qwen3.6-1M
claude --model qwen3.8:27b-1M
```
