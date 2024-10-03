VERSION=1.0.0

default: versioncheck

build-all: clean stage

stop:
	./gradlew --stop

clean:
	rm -rf build/
	./gradlew clean

compile: build

build:
	rm -rf build/
	./gradlew build -x test

cont-build:
	./gradlew -t build -x test

tests:
	./gradlew --rerun-tasks check

jar:
	./gradlew uberJar

dist:
	./gradlew installDist

stage:
	./gradlew stage

versioncheck:
	./gradlew dependencyUpdates

buildconfig:
	./gradlew generateBuildConfig

kdocs:
	./gradlew dokkaHtml

mddocs:
	./gradlew dokkaGfm

updatedocs:
	./bin/update-docs.sh

publish:
	./gradlew publishToMavenLocal

upgrade-wrapper:
	./gradlew wrapper --gradle-version=8.10.2 --distribution-type=bin
