VERSION := $(shell grep '^version=' gradle.properties | cut -d= -f2)
GRADLE_VERSION := $(shell grep '^gradle =' gradle/libs.versions.toml | grep -oE '[0-9]+\.[0-9]+(\.[0-9]+)?')

.PHONY: default build-all stop clean build build-tests cont-build tests \
	versioncheck refresh updatedocs kdocs \
	publish-local publish-local-snapshot check-gpg-env \
	publish-snapshot publish-maven-central upgrade-wrapper

default: versioncheck

build-all: clean build

stop:
	./gradlew --stop

clean:
	rm -rf build/
	./gradlew clean

build: clean
	./gradlew build -x test

build-tests:
	./gradlew compileTestKotlin

cont-build:
	./gradlew -t build -x test

tests:
	./gradlew --rerun-tasks check

versioncheck:
	./gradlew dependencyUpdates --no-configuration-cache

refresh:
	./gradlew --refresh-dependencies dependencyUpdates --no-configuration-cache

updatedocs:
	./bin/update-docs.sh

kdocs:
	./gradlew :dokkaGenerate

publish-local:
	./gradlew publishToMavenLocal

publish-local-snapshot:
	./gradlew -PoverrideVersion=$(VERSION)-SNAPSHOT publishToMavenLocal

GPG_ENV = \
	ORG_GRADLE_PROJECT_signingInMemoryKey="$$(gpg --armor --export-secret-keys $$GPG_SIGNING_KEY_ID)" \
	ORG_GRADLE_PROJECT_signingInMemoryKeyPassword=$$(security find-generic-password -a "gpg-signing" -s "gradle-signing-password" -w)

check-gpg-env:
	@if [ -z "$$GPG_SIGNING_KEY_ID" ]; then \
		echo "Error: GPG_SIGNING_KEY_ID is not set" >&2; exit 1; \
	fi
	@if ! gpg --list-secret-keys "$$GPG_SIGNING_KEY_ID" >/dev/null 2>&1; then \
		echo "Error: no GPG secret key found for GPG_SIGNING_KEY_ID=$$GPG_SIGNING_KEY_ID" >&2; exit 1; \
	fi
	@if ! security find-generic-password -a "gpg-signing" -s "gradle-signing-password" -w >/dev/null 2>&1; then \
		echo "Error: keychain entry 'gradle-signing-password' (account 'gpg-signing') not found" >&2; exit 1; \
	fi

publish-snapshot: check-gpg-env
	$(GPG_ENV) ./gradlew -PoverrideVersion=$(VERSION)-SNAPSHOT publishToMavenCentral

publish-maven-central: check-gpg-env
	$(GPG_ENV) ./gradlew publishAndReleaseToMavenCentral

upgrade-wrapper:
	./gradlew wrapper --gradle-version=$(GRADLE_VERSION) --distribution-type=bin
