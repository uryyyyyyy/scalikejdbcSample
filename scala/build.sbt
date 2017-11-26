name := "scalikejdbcSample"

version := "1.0"

val scalikejdbcVersion = "3.1.0"
val googleCloudVersion = "0.13.0-beta"

val mysqlSimple = (project in file("mysql-simple"))
  .settings(
    scalaVersion := "2.12.3",
    libraryDependencies ++= Seq(
      "org.scalikejdbc" %% "scalikejdbc" % scalikejdbcVersion,
      "org.scalikejdbc" %% "scalikejdbc-test" % scalikejdbcVersion % Test,
      "mysql" % "mysql-connector-java" % "6.0.6",
      "ch.qos.logback"  %  "logback-classic"   % "1.2.3"
    )
  ).enablePlugins(JavaAppPackaging)

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
  ).enablePlugins(JavaAppPackaging)

val mysqlMacro = (project in file("mysql-macro"))
  .settings(
    scalaVersion := "2.12.3",
    libraryDependencies ++= Seq(
      "org.scalikejdbc" %% "scalikejdbc" % scalikejdbcVersion,
      "org.scalikejdbc" %% "scalikejdbc-test" % scalikejdbcVersion % Test,
      "org.scalikejdbc" %% "scalikejdbc-config" % scalikejdbcVersion,
      "org.scalikejdbc" %% "scalikejdbc-syntax-support-macro" % scalikejdbcVersion,
      "mysql" % "mysql-connector-java" % "6.0.6",
      "ch.qos.logback"  %  "logback-classic"   % "1.2.3"
    )
  ).enablePlugins(JavaAppPackaging)

val bigqueryDsl = (project in file("bigquery-dsl"))
  .settings(
    resolvers ++= Seq(
      "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository"
    ),
    scalaVersion := "2.12.3",
    libraryDependencies ++= Seq(
      "org.scalikejdbc" %% "scalikejdbc" % scalikejdbcVersion,
      "org.scalikejdbc" %% "scalikejdbc-config" % scalikejdbcVersion,
      "org.scalikejdbc" %% "scalikejdbc-syntax-support-macro" % scalikejdbcVersion,
      "com.google.cloud" % "google-cloud-bigquery" % googleCloudVersion,
      "com.mayreh" %% "scalikejdbc-bigquery" % "0.0.7-SNAPSHOT",
      "org.scalikejdbc" %% "scalikejdbc-test" % scalikejdbcVersion % Test,
      "ch.qos.logback"  %  "logback-classic"   % "1.2.3"
    )
  ).enablePlugins(JavaAppPackaging)
