package de.nilsdruyen.gradle.ftpuploader

open class FTPUploadExtension {
  var host = ""
  var port = 22
  var username = ""
  var password = ""
  var sourceDir = ""
  var targetDir = ""
}