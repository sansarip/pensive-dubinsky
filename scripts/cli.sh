#!/bin/sh

mkdir out || true
bb -f scripts/cli.clj --classpath src/clj $1 $2 $3
