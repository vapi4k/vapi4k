VERSION := $(shell grep '^version=' gradle.properties | cut -d= -f2)
GRADLE_VERSION := $(shell grep '^gradle =' gradle/libs.versions.toml | grep -oE '[0-9]+\.[0-9]+(\.[0-9]+)?')

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

stage:
	./gradlew stage

versioncheck:
	./gradlew dependencyUpdates --no-configuration-cache

buildconfig:
	./gradlew generateBuildConfig

refresh:
	./gradlew --refresh-dependencies dependencyUpdates

updatedocs:
	./bin/update-docs.sh

mddocs:
	./gradlew dokkaGfm

kdocs:
	./gradlew :dokkaGenerate

publish-local:
	./gradlew publishToMavenLocal

publish-local-snapshot:
	./gradlew -PoverrideVersion=$(VERSION)-SNAPSHOT publishToMavenLocal

GPG_ENV = \
	ORG_GRADLE_PROJECT_signingInMemoryKey="$$(gpg --armor --export-secret-keys $$GPG_SIGNING_KEY_ID)" \
	ORG_GRADLE_PROJECT_signingInMemoryKeyPassword=$$(security find-generic-password -a "gpg-signing" -s "gradle-signing-password" -w)

publish-snapshot:
	$(GPG_ENV) ./gradlew -PoverrideVersion=$(VERSION)-SNAPSHOT publishToMavenCentral

publish-maven-central:
	$(GPG_ENV) ./gradlew publishAndReleaseToMavenCentral

upgrade-wrapper:
	./gradlew wrapper --gradle-version=$(GRADLE_VERSION) --distribution-type=bin
