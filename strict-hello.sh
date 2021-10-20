#!/bin/bash
curl -s -X POST \
  --data '
  {
    "hello": false,
    "name": "Piglet"
  }
  ' \
  localhost:8080 \
  | jq .
