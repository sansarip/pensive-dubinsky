#!/bin/sh

mkdir out || true
bb -f scripts/cli.clj --classpath src/clj:src/cljc $1 $2 $3
