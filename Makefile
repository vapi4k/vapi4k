.PHONY: default help stop clean build build-tests cont-build tests \
	lint format detekt zizmor depends \
	versions versions-all refresh updatedocs kdocs \
	publish-local publish-local-snapshot \
	publish-snapshot publish-maven-central upgrade-wrapper \
	_check-gpg-env _check-maven-central-env _require-version _require-gradle-version

VERSION := $(shell grep '^version=' gradle.properties | cut -d= -f2)
GRADLE_VERSION := $(shell grep '^gradle-wrapper =' gradle/libs.versions.toml | grep -oE '[0-9]+\.[0-9]+(\.[0-9]+)?')

GRADLE := ./gradlew

GPG_ENV := \
	ORG_GRADLE_PROJECT_signingInMemoryKey="$$(gpg --armor --export-secret-keys $$GPG_SIGNING_KEY_ID)" \
	ORG_GRADLE_PROJECT_signingInMemoryKeyId="$$GPG_SIGNING_KEY_ID" \
	ORG_GRADLE_PROJECT_signingInMemoryKeyPassword=$$(security find-generic-password -a "gpg-signing" -s "gradle-signing-password" -w)

# Maven Central's credentials, a user token's name and password from central.sonatype.com, from the macOS keychain, as
# the signing key's passphrase is: the maven-central-username and maven-central-password entries (account
# maven-central), shared by every repository that publishes this way. They reach only the publishing commands, as the
# vanniktech plugin's mavenCentralUsername and mavenCentralPassword, never ~/.gradle/gradle.properties, which every
# Gradle build on the machine is given. Add them once with
#   security add-generic-password -a maven-central -s maven-central-username -U -w
#   security add-generic-password -a maven-central -s maven-central-password -U -w
# each of which asks for the value, so it stays out of the shell's history.
MAVEN_CENTRAL_ENV = \
	ORG_GRADLE_PROJECT_mavenCentralUsername="$$(security find-generic-password -a "maven-central" -s "maven-central-username" -w)" \
	ORG_GRADLE_PROJECT_mavenCentralPassword="$$(security find-generic-password -a "maven-central" -s "maven-central-password" -w)"

default: help

help:  ## Show this help (list of targets)
	@awk 'BEGIN {FS = ":.*?## "; printf "Usage: make <target>\n\nTargets:\n"} \
		/^[a-zA-Z0-9_-]+:.*?## / {printf "  \033[36m%-22s\033[0m %s\n", $$1, $$2}' $(MAKEFILE_LIST)

stop: ## Stop running Gradle daemons
	$(GRADLE) --stop

clean: ## Remove root build/ and run gradle clean
	rm -rf build/
	$(GRADLE) clean

build: clean ## Compile and assemble all modules (skips tests)
	$(GRADLE) build -x test

build-tests: ## Compile test sources only
	$(GRADLE) compileTestKotlin

cont-build: ## Continuous build watch (skips tests)
	$(GRADLE) -t build -x test

tests: ## Re-run the full check suite (tests, lint, etc.)
	$(GRADLE) --rerun-tasks check

lint: ## Run kotlinter lintKotlin then detekt
	$(GRADLE) lintKotlin detekt

format: ## Run kotlinter formatKotlin
	$(GRADLE) formatKotlin

detekt: ## Run detekt static analysis
	$(GRADLE) detekt

zizmor: ## Audit GitHub Actions workflows with zizmor
	zizmor .

depends: ## Show project dependency tree
	$(GRADLE) dependencies

# dependencyUpdates is marked incompatible with the configuration cache (build.gradle.kts), so a run with the cache on
# builds an entry only to discard it, and says so. --no-configuration-cache skips the cache for this run instead.
# build.gradle.kts gives the task a formatter that lists only what is behind; versions-all names the plugin's own plain
# report, which also lists everything up to date.
versions: ## List the dependencies with newer versions, and any that could not be checked
	$(GRADLE) dependencyUpdates --no-configuration-cache --no-parallel

versions-all: ## The plugin's full dependency report, up-to-date dependencies included
	$(GRADLE) dependencyUpdates --no-configuration-cache --no-parallel --output-formatter=plain

refresh: ## Refresh deps and report updates
	$(GRADLE) --refresh-dependencies dependencyUpdates --no-configuration-cache

updatedocs: ## Run bin/update-docs.sh
	./bin/update-docs.sh

kdocs: ## Generate KDoc HTML via Dokka
	$(GRADLE) :dokkaGenerate

publish-local: _require-version ## Publish all artifacts to ~/.m2 (Maven Local)
	$(GRADLE) publishToMavenLocal

publish-local-snapshot: _require-version ## Publish -SNAPSHOT artifacts to ~/.m2/repositories
	$(GRADLE) -PoverrideVersion=$(VERSION)-SNAPSHOT publishToMavenLocal

publish-snapshot: _require-version _check-gpg-env _check-maven-central-env ## Publish -SNAPSHOT artifacts to Maven Central
	$(GPG_ENV) $(MAVEN_CENTRAL_ENV) $(GRADLE) -PoverrideVersion=$(VERSION)-SNAPSHOT publishToMavenCentral

publish-maven-central: _require-version _check-gpg-env _check-maven-central-env ## Publish and release to Maven Central
	$(GPG_ENV) $(MAVEN_CENTRAL_ENV) $(GRADLE) publishAndReleaseToMavenCentral

# Gradle's documented upgrade procedure: the first run rewrites
# gradle-wrapper.properties using the *old* wrapper jar; the second run
# regenerates the wrapper itself with the new version.
upgrade-wrapper: _require-gradle-version ## Upgrade the Gradle wrapper to the version in libs.versions.toml
	$(GRADLE) wrapper --gradle-version=$(GRADLE_VERSION) --distribution-type=bin
	$(GRADLE) wrapper --gradle-version=$(GRADLE_VERSION) --distribution-type=bin

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

# Fails, naming the keychain entry and never its value, when either of Maven Central's credentials is missing or empty
_check-maven-central-env:
	@for entry in maven-central-username maven-central-password; do \
		if [ -z "$$(security find-generic-password -a 'maven-central' -s "$$entry" -w 2>/dev/null)" ]; then \
			echo "ERROR: keychain entry '$$entry' (account 'maven-central') is missing or empty; add it with: security add-generic-password -a maven-central -s $$entry -U -w" >&2; \
			exit 1; \
		fi; \
	done

_require-version:
	@[ -n "$(VERSION)" ] || { echo "ERROR: Could not determine project version from gradle.properties" >&2; exit 1; }

_require-gradle-version:
	@[ -n "$(GRADLE_VERSION)" ] || { echo "ERROR: Could not determine gradle version from gradle/libs.versions.toml" >&2; exit 1; }
