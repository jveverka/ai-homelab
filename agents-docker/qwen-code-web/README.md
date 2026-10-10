
```bash
docker build --network=host -t qwen-code-web -f Dockerfile .

docker run -d --name qwen-code-web \
  -e OPENAI_BASE_URL="http://192.168.44.102:11434/v1" \
  -e OPENAI_MODEL="qwen3.8-flash-next:125b-a6b-q4_K_M_1M" \
  -e QWEN_ALLOWED_ORIGIN="https://my.origin.com:4170" \
  -e OLLAMA_API_KEY="my-random-secret" \
  -e QWEN_SERVER_TOKEN="$(openssl rand -hex 32)" \ 
  --restart unless-stopped \
  -p 4170:4170 \
  --privileged \
  -v /home/juraj/Data:/data \
  qwen-code-web
```
