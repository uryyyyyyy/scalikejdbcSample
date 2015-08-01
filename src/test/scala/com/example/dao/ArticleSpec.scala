package com.example.dao

import org.scalatest._
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc._
import java.time.{ZonedDateTime}
import scalikejdbc.jsr310._


class ArticleSpec extends fixture.FlatSpec with Matchers with AutoRollback {
  val aa = Article.syntax("a")

  behavior of "Article"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = Article.find(123)
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = Article.findBy(sqls.eq(aa.id, 123))
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = Article.findAll()
    allResults.size should be >(0)
  }
  it should "count all records" in { implicit session =>
    val count = Article.countAll()
    count should be >(0L)
  }
  it should "find all by where clauses" in { implicit session =>
    val results = Article.findAllBy(sqls.eq(aa.id, 123))
    results.size should be >(0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = Article.countBy(sqls.eq(aa.id, 123))
    count should be >(0L)
  }
  it should "create new record" in { implicit session =>
    val created = Article.create(title = "MyString", content = "MyString", createdDatetime = null)
    created should not be(null)
  }
  it should "save a record" in { implicit session =>
    val entity = Article.findAll().head
    // TODO modify something
    val modified = entity
    val updated = Article.save(modified)
    updated should not equal(entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity = Article.findAll().head
    Article.destroy(entity)
    val shouldBeNone = Article.find(123)
    shouldBeNone.isDefined should be(false)
  }

}
