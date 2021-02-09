LINES_PER_SAMPLE:=10

generate-sample-files:
	clj -M:test:generate-samples ${LINES_PER_SAMPLE}

remove-sample-files:
	rm samples/*

run-with-sample-files:
	scripts/cli.sh \
		samples/sample1.txt \
		samples/sample2.txt \
		samples/sample3.txt

docker-run-with-sample-files:
	docker run \
		-v $$(pwd)/samples:/usr/app/samples \
		-v $$(pwd)/out:/usr/app/out \
		sansarip/pensive-dubinsky \
		/usr/app/scripts/cli.sh \
		samples/sample1.txt \
		samples/sample2.txt \
		samples/sample3.txt

remove-output-files:
	rm out/*

remove-samples-and-outputs: remove-sample-files remove-output-files

generate-and-run: generate-sample-files run-with-sample-files

run-dev-nrepl:
	clj -M:server+nrepl:test:dev -r

run-prod-nrepl:
	clj -M:server+nrepl:prod -r

run-prod-server:
	clj -M:server:prod

run-tests:
	clj -M:test:runner

docker-build:
	docker build . -t sansarip/pensive-dubinsky

docker-run-prod-nrepl:
	docker run \
		-p 3000:3000 \
		-p 41985:41985 \
		-d \
		--rm \
		sansarip/pensive-dubinsky
