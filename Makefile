.PHONY: default help build-all stop clean build build-tests cont-build tests \
	lint format detekt \
	versioncheck refresh updatedocs kdocs \
	publish-local publish-local-snapshot check-gpg-env \
	publish-snapshot publish-maven-central upgrade-wrapper

VERSION := $(shell grep '^version=' gradle.properties | cut -d= -f2)

ifeq ($(strip $(VERSION)),)
$(error Could not determine project version from gradle.properties)
endif

GRADLE_VERSION := $(shell grep '^gradle =' gradle/libs.versions.toml | grep -oE '[0-9]+\.[0-9]+(\.[0-9]+)?')

ifeq ($(strip $(GRADLE_VERSION)),)
$(error Could not determine gradle version from gradle/libs.versions.toml)
endif

GPG_ENV = \
	ORG_GRADLE_PROJECT_signingInMemoryKey="$$(gpg --armor --export-secret-keys $$GPG_SIGNING_KEY_ID)" \
	ORG_GRADLE_PROJECT_signingInMemoryKeyId="$$GPG_SIGNING_KEY_ID" \
	ORG_GRADLE_PROJECT_signingInMemoryKeyPassword=$$(security find-generic-password -a "gpg-signing" -s "gradle-signing-password" -w)

default: versioncheck

help: ## Show this help
	@awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z_-]+:.*?## / {printf "  \033[36m%-22s\033[0m %s\n", $$1, $$2}' $(MAKEFILE_LIST)

build-all: clean build ## Clean and build everything (skips tests)

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

lint: detekt ## Run detekt then kotlinter lintKotlin
	./gradlew lintKotlin

detekt: ## Run detekt static analysis
	./gradlew detekt

versioncheck: ## Report dependency updates (default target)
	./gradlew dependencyUpdates --no-configuration-cache

refresh: ## Refresh deps and report updates
	./gradlew --refresh-dependencies dependencyUpdates --no-configuration-cache

updatedocs: ## Run bin/update-docs.sh
	./bin/update-docs.sh

kdocs: ## Generate KDoc HTML via Dokka
	./gradlew :dokkaGenerate

publish-local: ## Publish all artifacts to ~/.m2 (Maven Local)
	./gradlew publishToMavenLocal

publish-local-snapshot: ## Publish -SNAPSHOT artifacts to ~/.m2
	./gradlew -PoverrideVersion=$(VERSION)-SNAPSHOT publishToMavenLocal

check-gpg-env: ## Verify GPG signing env / keychain entries
	@if [ -z "$$GPG_SIGNING_KEY_ID" ]; then \
		echo "Error: GPG_SIGNING_KEY_ID is not set" >&2; exit 1; \
	fi
	@if ! gpg --list-secret-keys "$$GPG_SIGNING_KEY_ID" >/dev/null 2>&1; then \
		echo "Error: no GPG secret key found for GPG_SIGNING_KEY_ID=$$GPG_SIGNING_KEY_ID" >&2; exit 1; \
	fi
	@if ! security find-generic-password -a "gpg-signing" -s "gradle-signing-password" -w >/dev/null 2>&1; then \
		echo "Error: keychain entry 'gradle-signing-password' (account 'gpg-signing') not found" >&2; exit 1; \
	fi

publish-snapshot: check-gpg-env ## Publish -SNAPSHOT artifacts to Maven Central
	$(GPG_ENV) ./gradlew -PoverrideVersion=$(VERSION)-SNAPSHOT publishToMavenCentral

publish-maven-central: check-gpg-env ## Publish and release to Maven Central
	$(GPG_ENV) ./gradlew publishAndReleaseToMavenCentral

upgrade-wrapper: ## Upgrade the Gradle wrapper to the version in libs.versions.toml
	./gradlew wrapper --gradle-version=$(GRADLE_VERSION) --distribution-type=bin
