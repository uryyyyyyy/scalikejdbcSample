name := """scalaScripts"""

version := "1.0"

scalaVersion := "2.11.6"


libraryDependencies ++= Seq(
	"org.scalikejdbc" %% "scalikejdbc" % "2.2.7",
	"mysql" % "mysql-connector-java" % "5.1.36",
	"org.scalikejdbc" %% "scalikejdbc-jsr310" % "2.2.7",
	"ch.qos.logback"  %  "logback-classic"   % "1.1.3",
	"org.scalikejdbc"   %% "scalikejdbc-test" % "2.2.7"   % "test",
	"org.scalatest"     %% "scalatest"        % "2.2.5"   % "test"
)

scalikejdbcSettings
