name := "localstack-mock-tests"

version := "0.0.1"

scalaVersion := "2.12.8"

lazy val testcontainersVersion = "0.37.0"

libraryDependencies ++= Seq(
  // AWS
  "com.amazonaws" % "aws-java-sdk-s3" % "1.11.882",

  // Testing
  "org.scalatest" %% "scalatest" % "3.0.0" % "test",
  "com.dimafeng" %% "testcontainers-scala-scalatest" % testcontainersVersion % Test
)
