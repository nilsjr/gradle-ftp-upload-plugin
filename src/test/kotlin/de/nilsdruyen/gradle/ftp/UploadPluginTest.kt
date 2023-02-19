package de.nilsdruyen.gradle.ftp

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.appendText

class UploadPluginTest {

    private lateinit var gradleRunner: GradleRunner

    @BeforeEach
    fun setup(@TempDir testProjectDir: Path) {
        val buildFile = Files.createFile(testProjectDir.resolve("build.gradle"))
        buildFile.appendText(
            """
            plugins {
                id 'java'
                id 'de.nilsdruyen.gradle-ftp-upload-plugin'
            }
            
        """.trimIndent()
        )
        Files.createFile(testProjectDir.resolve("settings.gradle"))

        gradleRunner = GradleRunner.create()
            .withPluginClasspath()
            .withProjectDir(testProjectDir.toFile())
            .withTestKitDir(Files.createTempDirectory("testkit").toFile())
            .apply {
                // gradle testkit jacoco support
                javaClass.classLoader.getResourceAsStream("testkit-gradle.properties")?.use { inputStream ->
                    File(projectDir, "gradle.properties").outputStream().use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
            }
    }

    @Test
    fun `apply plugin and check task`() {
        val result = gradleRunner
            .withArguments("uploadFilesToFtp")
            .build()

        assertThat(result.task(":uploadFilesToFtp")!!.outcome).isEqualTo(TaskOutcome.SUCCESS)
    }
}