
## Build Docker
``
docker build -t ubuntu-java25-maven-agents -f Dockerfile-j25 .
``

## Run in interactive mode
``
docker run -it --name ubuntu-java25-maven-agents --rm \
  --privileged \
  -v /home/juraj/Data/Private/ai-home-lab:/data \
  ubuntu-java25-maven-agents
``

## Run in daemon mode
``
docker run --name ubuntu-java25-maven-agents -d \
  --privileged \
  -v /home/juraj/Data/Private/ai-home-lab:/data \
  ubuntu-java25-maven-agents
``

## Run QwenCode
``
qwen --auth-type openai --model qwen3.8:27b-1M \
     --approval-mode auto \
     --openai-api-key ollama \
     --openai-base-url http://192.168.44.102:11434/v1
``

## Run OpenCode
``
OPENCODE_CONFIG_CONTENT='{"provider":{"ollama":{"npm":"@ai-sdk/openai-compatible","name":"Ollama","options":{"baseURL":"http://192.168.44.102:11434/v1"},"models":{"qwen3.8:27b":{"tools":true,"limit":{"context":131072,"output":16384}}}}},"model":"ollama/qwen3.8:27b-1M"}'
opencode
``
## Run ClaudeCode
``
ANTHROPIC_AUTH_TOKEN=ollama
ANTHROPIC_BASE_URL=http://192.168.44.102:11434
OLLAMA_CONTEXT_LENGTH=64000

claude --model qwen3.6-1M
claude --model qwen3.8:27b-1M
``

