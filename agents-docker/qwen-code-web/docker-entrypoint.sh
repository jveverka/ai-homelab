#!/bin/bash
set -e

export OPENAI_API_KEY=ollama
export OPENAI_BASE_URL=http://192.168.44.102:11434/v1
export OPENAI_MODEL='qwen3.8-flash-next:125b-a6b-q4_K_M_1M'

qwen serve \
   --require-auth \
   --hostname 0.0.0.0 \
   --port 4170