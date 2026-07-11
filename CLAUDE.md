# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

A Gradle plugin (`de.nilsdruyen.gradle-ftp-upload-plugin`) that adds tasks to upload static content to a server. Despite the "FTP" naming, the transfer is actually **SFTP** via the sshj library. Published to the Gradle Plugin Portal; Kotlin/JVM, Java 17 toolchain.

## Commands

```sh
./gradlew build            # compile + test + assemble
./gradlew test             # run JUnit 5 tests
./gradlew test --tests "de.nilsdruyen.gradle.ftp.UploadPluginTest"   # single test class
./gradlew detekt           # static analysis (detekt-config.yml)
./gradlew ktlintCheck      # formatting check (custom Detekt task using detekt-formatting.yml)
./gradlew publishPlugins -Pgradle.publish.key=key -Pgradle.publish.secret=secret   # release to Plugin Portal
```

CI (`.github/workflows/check-and-build.yml`) runs `detekt ktlintCheck` and `build` on pushes/PRs to `develop`/`main`. Branch model: work happens on `develop`, `main` is the release branch.

## Architecture

Single-module plugin, all code under `src/main/kotlin/de/nilsdruyen/gradle/ftp/`:

- `UploadPlugin` — plugin entry point (registered in `build.gradle.kts` under `gradlePlugin {}`). Creates the `ftpUploadExtension` extension and registers two tasks in the `deployment` group: `uploadFilesToFtp` and `clearTargetDirectory`.
- `UploadExtension` — mutable config (host, port, username, password, sourceDir, targetDir, clearDirectoryBeforeUpload). Plain `var`s, not lazy Gradle properties; tasks receive it via a `taskExtension` property.
- `SFTPConnection` — wraps sshj's `SSHClient`/`SFTPClient`; connects in `init`, exposes `with { }` for client operations.
- `tasks/UploadTask`, `tasks/ClearDirectoryTask`, `tasks/Utils.kt` — task implementations built on `SFTPConnection`.
- `Constants` — extension and task name strings.

Task/extension names in the README must stay in sync with `Constants`.

## Conventions

- Dependencies are managed via the version catalog (`gradle/libs.versions.toml`); Renovate keeps them updated.
- detekt runs with `ignoreFailures = false`; both `detekt-config.yml` and `detekt-formatting.yml` gate CI.
- Version is set in `build.gradle.kts` (`version = "..."`); README usage examples reference the published version.