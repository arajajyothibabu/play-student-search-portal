name := "MongoCassandraMigrations"

version := "1.0"

lazy val `mongocassandramigrations` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

val sparkVersion = "2.1.0"

val cassandraVersion = "2.0.0"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion exclude("org.glassfish.hk2", "hk2-utils") exclude("org.glassfish.hk2", "hk2-locator") exclude("javax.validation", "validation-api"),
  "org.apache.spark" % "spark-sql_2.11" % sparkVersion, //work around for errors
  "org.apache.spark" %% "spark-streaming" % sparkVersion,
  "org.apache.spark" % "spark-streaming-kafka-0-10_2.11" % sparkVersion,
  "com.datastax.spark" %% "spark-cassandra-connector" % cassandraVersion,
  "org.mongodb.spark" % "mongo-spark-connector_2.11" % "1.1.0",
  "org.reactivemongo" %% "play2-reactivemongo" % "0.11.14",
  "org.scalactic" %% "scalactic" % "3.0.1",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)

dependencyOverrides ++= Set(
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.5" //to resolve version conflicts with play
)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Maven Central" at "https://repo1.maven.org/maven2/"

logBuffered in Test := false

sources in (Compile, doc) := Seq.empty

publishArtifact in (Compile, packageDoc) := false