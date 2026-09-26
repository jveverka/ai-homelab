
## tmux cheat sheet
```bash
tmux (ctrl b + d)  # <- start new tmux session and detach 
tmux ls            # <- list running sessions
tmux a             # <- reattach to running session
```

### Run QwenCode
```bash
QWEN_MODEL=qwen3.8:27b-1M
QWEN_MODEL=qwen3.8-flash-next:125b-a6b-q4_K_M_1M
qwen --auth-type openai --model ${QWEN_MODEL} \
     --approval-mode yolo \
     --openai-api-key ollama \
     --openai-base-url http://ai-server:11434/v1
```

### Run ClaudeCode
```bash
ANTHROPIC_AUTH_TOKEN=ollama
ANTHROPIC_BASE_URL=http://ai-server:11434
OLLAMA_CONTEXT_LENGTH=64000

claude --model qwen3.8:27b-1M
claude --model qwen3.8-flash-next:125b-a6b-q4_K_M_1M
```