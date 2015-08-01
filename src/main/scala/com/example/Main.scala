package com.example

import java.time.ZonedDateTime

import com.example.dao.Article
import scalikejdbc._

object Main {

	def main(args: Array[String]): Unit ={
		Class.forName("com.mysql.jdbc.Driver")
		ConnectionPool.singleton("jdbc:mysql://localhost:3306/scalike","root","root",
			new ConnectionPoolSettings(initialSize = 5, maxSize = 10))

		GlobalSettings.loggingSQLAndTime = LoggingSQLAndTimeSettings(
			enabled = true,
			singleLineMode = true,
			logLevel = 'DEBUG,
			warningEnabled = true,
			warningThresholdMillis = 1000L,
			warningLogLevel = 'WARN
		)


		DB localTx { implicit session =>
			println("helloworld")
			val a = Article.create("title", "content", ZonedDateTime.now())
			println(a)
			val a_ = Article(a.id, a.title + " modified", a.content, a.createdDatetime)
			val a__ = Article.save(a_)
			println(a__)

			val as = Article.findAll()
			as.foreach(println)
		}
	}

}
