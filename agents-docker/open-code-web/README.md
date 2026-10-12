
```bash
docker build --network=host -t open-code-web -f Dockerfile .

docker run -d --name open-code-web \
  -e OPENCODE_SERVER_USERNAME_EXT="juraj" \
  -e OPENCODE_SERVER_PASSWORD_EXT="$(openssl rand -hex 32)" \ 
  --restart unless-stopped \
  -p 4096:4096 \
  --privileged \
  -v /home/juraj/Data:/data \
  -v ./opencode.json:/root/.config/opencode/opencode.json:ro \
  open-code-web
```