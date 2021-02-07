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

remove-output-files:
	rm out/*

remove-samples-and-outputs: remove-sample-files remove-output-files

generate-and-run: generate-sample-files run-with-sample-files

nrepl:
	clj -M:server+nrepl:test -r