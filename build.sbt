name := """scalikejdbc-sample"""

version := "1.0"

scalaVersion := "2.11.8"

val scalikejdbcVersion = "2.4.1"

libraryDependencies ++= Seq(
  "org.scalikejdbc" % "scalikejdbc_2.11" % scalikejdbcVersion,
  "org.scalikejdbc" %% "scalikejdbc-syntax-support-macro" % scalikejdbcVersion,
  "org.scalikejdbc" %% "scalikejdbc-test" % scalikejdbcVersion,
  "org.scalikejdbc" %% "scalikejdbc-config" % scalikejdbcVersion,
  "ch.qos.logback"  %  "logback-classic"   % "1.1.3",
  "org.scalatest"     %% "scalatest"        % "3.0.0-M15"   % "test",
  "mysql" % "mysql-connector-java" % "5.1.36",
  "com.h2database" % "h2" % "1.4.187",
  "junit" % "junit" % "4.12" % "test"
)
