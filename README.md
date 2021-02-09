[![Build Status](https://travis-ci.com/sansarip/pensive-dubinsky.svg?branch=main)](https://travis-ci.com/sansarip/pensive-dubinsky)

# Pensive Dubinsky

A dummy project

## Prerequisites

* [Java](https://openjdk.java.net/install/)
* [Clojure](https://clojure.org/guides/getting_started)
* [Babashka](https://github.com/babashka/babashka)

_OR_

[Docker](https://www.docker.com/get-started)

## CLI

Sorted files will be in `out/`

```sh
scripts/cli.sh <file-1> <file-2> <file-3>
```

_OR_

```sh
docker run \
		-v <input-vol>:/usr/app/in \
		-v <output-vol>:/usr/app/out \
		sansarip/pensive-dubinsky \
		/usr/app/scripts/cli.sh \
		in/<file-3> \
		in/<file-2> \
		in/<file-1>
```

## API

API listens on port 3000 and nREPL is available on port 41985

### Dev

```sh
make run-dev-nrepl
```

### Prod

```sh
make run-prod-nrepl
```

_OR_

```sh
make docker-build
make docker-run-prod-nrepl
```

## Tests

```sh
make run-tests
```