name := "SparkETLProcess"

version := "0.1"

scalaVersion := "2.12.12"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.0.0",
  "org.apache.spark" %% "spark-sql" % "3.0.0",
  // https://mvnrepository.com/artifact/org.postgresql/postgresql
  "org.postgresql" % "postgresql" % "42.2.19",
  // https://mvnrepository.com/artifact/org.scalatest/scalatest-funsuite
  "org.scalatest" %% "scalatest-funsuite" % "3.3.0-SNAP3" % Test


)