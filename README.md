# FTP Upload Gradle Plugin

This plugin adds an FTP upload task to your project to easily deploy static content to ftp servers. This plugin is a
result while expermenting with kotlin js / compose web.

## Usage

[![gradlePluginPortal](https://img.shields.io/maven-metadata/v/https/plugins.gradle.org/m2/de/nilsdruyen/gradle-ftp-upload-plugin/maven-metadata.xml.svg?label=gradlePluginPortal)](https://plugins.gradle.org/plugin/de.nilsdruyen.gradle-ftp-upload-plugin)

The plugin is available from the GradlePluginPortal.

### `plugins` block:

<details open>
  <summary>Kotlin</summary>

```kotlin
// app build.gradle.kts
plugins {
  id("de.nilsdruyen.gradle-ftp-upload-plugin") version "0.4.0"
}
```

</details>

<details>
  <summary>Groovy</summary>

```groovy
// app build.gradle
plugins {
  id 'de.nilsdruyen.gradle-ftp-upload-plugin' version '0.4.0'
}
```

</details>

### Configuration

The plugin automatically add the task **uploadFilesToFtp** to your project or subproject. Then you have to define
following fields in the UploadExtension.


<details open>
  <summary>Kotlin</summary>

```kotlin
import de.nilsdruyen.gradle.ftp.UploadExtension

configure<UploadExtension> {
  host = "example.com"
  port = 22
  username = "testuser"
  password = "test123"
  sourceDir = "${project.buildDir}/distributions"
  targetDir = "/folder/"
}
```
</details>

<details>
  <summary>Groovy</summary>

```groovy
import de.nilsdruyen.gradle.ftp.UploadExtension

ftpUploadExtension {
    host "example.com"
    port 22
    username "testuser"
    password "test123"
    sourceDir "${project.buildDir}/distributions"
    targetDir "/folder/"
}
```

</details>

or use gradle properties

<details open>
  <summary>Kotlin</summary>

```kotlin
import de.nilsdruyen.gradle.ftp.UploadExtension

configure<UploadExtension> {
  host = properties.getOrDefault("ftp.host", "").toString()
  port = properties.getOrDefault("ftp.port", 22).toString().toInt()
  username = properties.getOrDefault("ftp.username", "").toString()
  password = properties.getOrDefault("ftp.password", "").toString()
  sourceDir = "${project.buildDir}/distributions"
  targetDir = "/folder/"
}
```
</details>

<details>
  <summary>Groovy</summary>

```groovy
import de.nilsdruyen.gradle.ftp.UploadExtension

ftpUploadExtension {
  host = findProperty("ftp.host") ?: ""
  port = findProperty("ftp.port").toInteger() ?: 22
  username = findProperty("ftp.username") ?: ""
  password = findProperty("ftp.password") ?: ""
  sourceDir = "${project.buildDir}/distributions"
  targetDir = "/folder/"
}
```
</details>

## License

    The MIT License (MIT)

    Copyright (C) 2021 Nils Druyen

    Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
    associated documentation files (the "Software"), to deal in the Software without restriction,
    including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
    and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
    subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all copies or substantial
    portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
    LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
    NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
    DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT
    OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.