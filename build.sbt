name := """enron-email-word-count"""

version := "1.0"

scalaVersion := "2.11.8"

// Change this to another test framework if you prefer
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.1.0",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)
