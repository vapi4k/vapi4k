.PHONY: default help stop clean build build-tests cont-build tests \
	lint format detekt \
	versions refresh updatedocs kdocs \
	publish-local publish-local-snapshot \
	publish-snapshot publish-maven-central upgrade-wrapper \
	_check-gpg-env _require-version _require-gradle-version

VERSION := $(shell grep '^version=' gradle.properties | cut -d= -f2)
GRADLE_VERSION := $(shell grep '^gradle-wrapper =' gradle/libs.versions.toml | grep -oE '[0-9]+\.[0-9]+(\.[0-9]+)?')

GPG_ENV := \
	ORG_GRADLE_PROJECT_signingInMemoryKey="$$(gpg --armor --export-secret-keys $$GPG_SIGNING_KEY_ID)" \
	ORG_GRADLE_PROJECT_signingInMemoryKeyId="$$GPG_SIGNING_KEY_ID" \
	ORG_GRADLE_PROJECT_signingInMemoryKeyPassword=$$(security find-generic-password -a "gpg-signing" -s "gradle-signing-password" -w)

default: help

help:  ## Show this help (list of targets)
	@awk 'BEGIN {FS = ":.*?## "; printf "Usage: make <target>\n\nTargets:\n"} \
		/^[a-zA-Z0-9_-]+:.*?## / {printf "  \033[36m%-22s\033[0m %s\n", $$1, $$2}' $(MAKEFILE_LIST)

stop: ## Stop running Gradle daemons
	./gradlew --stop

clean: ## Remove root build/ and run gradle clean
	rm -rf build/
	./gradlew clean

build: clean ## Compile and assemble all modules (skips tests)
	./gradlew build -x test

build-tests: ## Compile test sources only
	./gradlew compileTestKotlin

cont-build: ## Continuous build watch (skips tests)
	./gradlew -t build -x test

tests: ## Re-run the full check suite (tests, lint, etc.)
	./gradlew --rerun-tasks check

lint: ## Run kotlinter lintKotlin then detekt
	./gradlew lintKotlin detekt

format: ## Run kotlinter formatKotlin
	./gradlew formatKotlin

detekt: ## Run detekt static analysis
	./gradlew detekt

versions: ## Report dependency updates
	./gradlew dependencyUpdates --no-configuration-cache --no-parallel

refresh: ## Refresh deps and report updates
	./gradlew --refresh-dependencies dependencyUpdates --no-configuration-cache

updatedocs: ## Run bin/update-docs.sh
	./bin/update-docs.sh

kdocs: ## Generate KDoc HTML via Dokka
	./gradlew :dokkaGenerate

publish-local: _require-version ## Publish all artifacts to ~/.m2 (Maven Local)
	./gradlew publishToMavenLocal

publish-local-snapshot: _require-version ## Publish -SNAPSHOT artifacts to ~/.m2/repositories
	./gradlew -PoverrideVersion=$(VERSION)-SNAPSHOT publishToMavenLocal

publish-snapshot: _require-version _check-gpg-env ## Publish -SNAPSHOT artifacts to Maven Central
	$(GPG_ENV) ./gradlew -PoverrideVersion=$(VERSION)-SNAPSHOT publishToMavenCentral

publish-maven-central: _require-version _check-gpg-env ## Publish and release to Maven Central
	$(GPG_ENV) ./gradlew publishAndReleaseToMavenCentral

# Gradle's documented upgrade procedure: the first run rewrites
# gradle-wrapper.properties using the *old* wrapper jar; the second run
# regenerates the wrapper itself with the new version.
upgrade-wrapper: _require-gradle-version ## Upgrade the Gradle wrapper to the version in libs.versions.toml
	./gradlew wrapper --gradle-version=$(GRADLE_VERSION) --distribution-type=bin
	./gradlew wrapper --gradle-version=$(GRADLE_VERSION) --distribution-type=bin

_check-gpg-env:
	@if [ -z "$$GPG_SIGNING_KEY_ID" ]; then \
		echo "ERROR: GPG_SIGNING_KEY_ID is not set" >&2; exit 1; \
	fi
	@if ! gpg --list-secret-keys "$$GPG_SIGNING_KEY_ID" >/dev/null 2>&1; then \
		echo "ERROR: no GPG secret key found for GPG_SIGNING_KEY_ID=$$GPG_SIGNING_KEY_ID" >&2; exit 1; \
	fi
	@if ! security find-generic-password -a "gpg-signing" -s "gradle-signing-password" -w >/dev/null 2>&1; then \
		echo "ERROR: keychain entry 'gradle-signing-password' (account 'gpg-signing') not found" >&2; exit 1; \
	fi

_require-version:
	@[ -n "$(VERSION)" ] || { echo "ERROR: Could not determine project version from gradle.properties" >&2; exit 1; }

_require-gradle-version:
	@[ -n "$(GRADLE_VERSION)" ] || { echo "ERROR: Could not determine gradle version from gradle/libs.versions.toml" >&2; exit 1; }
