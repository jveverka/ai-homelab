
```bash
docker build -t qwen-code-web -f Dockerfile .

docker run -d --name qwen-code-web \
  -e QWEN_ALLOWED_ORIGIN="https://my.origin.com:4170" \
  -e QWEN_SERVER_TOKEN="$(openssl rand -hex 32)" \ 
  --restart unless-stopped \
  -p 4170:4170 \
  --privileged \
  -v /home/juraj/Data:/data \
  qwen-code-web
```
