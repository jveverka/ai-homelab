#!/bin/bash
set -e

export OPENAI_API_KEY="ollama"
export OPENAI_BASE_URL=${OPENAI_BASE_URL_EXT}
export OPENAI_MODEL=${OPENAI_MODEL_EXT}
echo "OPENAI_BASE_URL: ${OPENAI_BASE_URL}"
echo "OPENAI_MODEL: ${OPENAI_MODEL}"
echo "starting qwen serve ..."

qwen serve \
   --require-auth \
   --hostname 0.0.0.0 \
   --port 4170 \
   --allow-origin ${QWEN_ALLOWED_ORIGIN}
