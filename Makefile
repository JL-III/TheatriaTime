.PHONY: build
build:
	./gradlew shadowJar

.PHONY: clean
clean:
	./gradlew clean