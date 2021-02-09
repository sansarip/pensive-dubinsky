FROM clojure:openjdk-8-tools-deps
ARG workdir=/usr/app

RUN mkdir -p $workdir
WORKDIR $workdir

ADD deps.edn Makefile $workdir/
RUN clj -P

RUN cd /usr/bin && \
 	wget https://github.com/babashka/babashka/releases/download/v0.2.9/babashka-0.2.9-linux-static-amd64.zip && \
 	unzip babashka-0.2.9-linux-static-amd64.zip && \
 	chmod a+x bb

ADD src $workdir/src/
ADD scripts $workdir/scripts/

# API
EXPOSE 3000

# nREPL
EXPOSE 41985

# Sleep necessary due to rlwrap race condition,
# see https://github.com/moby/moby/issues/28009
CMD sleep .5 && make run-prod-nrepl