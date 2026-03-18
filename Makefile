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

trigger-jitpack:
	until curl -s "https://jitpack.io/com/github/vapi4k/vapi4k/$(VERSION)/build.log" | grep -qv "not found"; do \
		echo "Waiting for JitPack..."; \
		sleep 10; \
	done

view-jitpack:
	curl -s "https://jitpack.io/com/github/vapi4k/vapi4k/$(VERSION)/build.log"
	curl -s "https://jitpack.io/api/builds/com.github.vapi4k/vapi4k/$(VERSION)" | jq

refresh:
	./gradlew --refresh-dependencies dependencyUpdates

mddocs:
	./gradlew dokkaGfm

updatedocs:
	./bin/update-docs.sh

publish:
	./gradlew publishToMavenLocal

upgrade-wrapper:
	./gradlew wrapper --gradle-version=9.4.0 --distribution-type=bin
