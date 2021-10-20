#!/bin/bash
curl -s -X POST \
  --data '
  {
    "hello": true,
    "name": "Justin Case"
  }
  ' \
  localhost:8080 \
  | jq .
