#!/bin/bash
set -e

export OPENCODE_SERVER_USERNAME=${OPENCODE_SERVER_USERNAME_EXT}
export OPENCODE_SERVER_PASSWORD=${OPENCODE_SERVER_PASSWORD_EXT}
echo "starting opencode web ..."

opencode web \
   --hostname 0.0.0.0 \
   --port 4096
