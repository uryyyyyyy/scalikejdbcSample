import com.typesafe.config.ConfigFactory


name := """scalikejdbc-sample"""

version := "1.0"

scalaVersion := "2.11.8"

val scalikejdbcVersion = "2.4.2"

val redshiftVersion = "1.1.6.1006"
val redshiftUrl = s"https://s3.amazonaws.com/redshift-downloads/drivers/RedshiftJDBC41-$redshiftVersion.jar"

val conf = {
  ConfigFactory.defaultOverrides
    .withFallback(ConfigFactory.parseFile(new File("src/main/resources/application.conf")).resolve())
}

libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc" % scalikejdbcVersion,
  "org.scalikejdbc" %% "scalikejdbc-syntax-support-macro" % scalikejdbcVersion,
  "org.scalikejdbc" %% "scalikejdbc-test" % scalikejdbcVersion,
  "org.scalikejdbc" %% "scalikejdbc-config" % scalikejdbcVersion,
  "ch.qos.logback"  %  "logback-classic"   % "1.1.3",
  "org.scalatest"     %% "scalatest"        % "3.0.0-M15"   % "test",
  "mysql" % "mysql-connector-java" % "5.1.36",
  "com.h2database" % "h2" % "1.4.187",
  "com.amazonaws" % "redshift.jdbc" % redshiftVersion from redshiftUrl
)

inConfig(Runtime)(
  FlywayPlugin.projectSettings ++ Seq(
    flywayDriver := conf.getString("db.h2.driver"),
    flywayUrl := conf.getString("db.h2.url"),
    flywayUser := conf.getString("db.h2.user"),
    flywayPassword := conf.getString("db.h2.password")
  )
)

inConfig(Test)(
  FlywayPlugin.projectSettings ++ Seq(
    flywayDriver := conf.getString("db.h2Test.driver"),
    flywayUrl := conf.getString("db.h2Test.url"),
    flywayUser := conf.getString("db.h2Test.user"),
    flywayPassword := conf.getString("db.h2Test.password")
  )
)