name := "scalikejdbcSample"

version := "1.0"

val scalikejdbcVersion = "3.0.2"


val mysqlSimple = (project in file("mysql-simple"))
  .settings(
    scalaVersion := "2.12.3",
    libraryDependencies ++= Seq(
      "org.scalikejdbc" %% "scalikejdbc" % scalikejdbcVersion,
      "org.scalikejdbc" %% "scalikejdbc-test" % scalikejdbcVersion % Test,
      "mysql" % "mysql-connector-java" % "6.0.6",
      "ch.qos.logback"  %  "logback-classic"   % "1.2.3"
    )
  )

val mysqlDsl = (project in file("mysql-dsl"))
  .settings(
    scalaVersion := "2.12.3",
    libraryDependencies ++= Seq(
      "org.scalikejdbc" %% "scalikejdbc" % scalikejdbcVersion,
      "org.scalikejdbc" %% "scalikejdbc-test" % scalikejdbcVersion % Test,
      "org.scalikejdbc" %% "scalikejdbc-config" % scalikejdbcVersion,
      "mysql" % "mysql-connector-java" % "6.0.6",
      "ch.qos.logback"  %  "logback-classic"   % "1.2.3"
    )
  )