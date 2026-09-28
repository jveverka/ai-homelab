
```bash
docker build -t qwen-code-web -f Dockerfile .

docker run -d --name qwen-code-web \
  --restart unless-stopped \
  -p 4170:4170 \
  --privileged \
  -v /home/juraj/Data:/data \
  qwen-code-web
```
