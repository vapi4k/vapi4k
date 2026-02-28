VERSION := $(shell grep 'extra\["versionStr"\]' build.gradle.kts | grep -oE '[0-9]+\.[0-9]+\.[0-9]+')

default: versioncheck

build-all: clean stage

stop:
	./gradlew --stop

clean:
	rm -rf build/
	./gradlew clean

compile: build

build: clean
	rm -rf build/
	./gradlew build -x test

build-tests: clean
	./gradlew compileTestKotlin

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
	./gradlew dependencyUpdates --no-configuration-cache

buildconfig:
	./gradlew generateBuildConfig

kdocs:
	./gradlew :dokkaGenerate

trigger-build:
	curl --fail-with-body "https://jitpack.io/com/github/vapi4k/vapi4k/$(VERSION)/build.log"

view-build:
	curl -s "https://jitpack.io/api/builds/com.github.vapi4k/vapi4k/$(VERSION)" | python3 -m json.tool

refresh:
	./gradlew --refresh-dependencies dependencyUpdates

mddocs:
	./gradlew dokkaGfm

updatedocs:
	./bin/update-docs.sh

publish:
	./gradlew publishToMavenLocal

upgrade-wrapper:
	./gradlew wrapper --gradle-version=9.2.0 --distribution-type=bin
