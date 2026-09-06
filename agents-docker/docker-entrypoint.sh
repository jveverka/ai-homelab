#!/bin/bash
set -e

dockerd >/var/log/dockerd.log 2>&1 &
DOCKERD_PID=$!

echo -n "Starting Docker"

for i in $(seq 1 30); do
    if docker info >/dev/null 2>&1; then
        echo
        echo "Docker is ready."

        # Default CMD + interactive terminal -> bash
        if [ -t 0 ] && [ "$#" -eq 2 ] &&
           [ "$1" = "sleep" ] && [ "$2" = "infinity" ]; then
            exec bash
        fi

        exec "$@"
    fi

    if ! kill -0 "$DOCKERD_PID" 2>/dev/null; then
        echo
        echo "Docker daemon failed to start:"
        cat /var/log/dockerd.log
        exit 1
    fi

    echo -n "."
    sleep 1
done

echo
echo "Docker daemon did not start within 30 seconds."
cat /var/log/dockerd.log
exit 1