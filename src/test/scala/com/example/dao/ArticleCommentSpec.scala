package com.example.dao

import org.scalatest._
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc._


class ArticleCommentSpec extends fixture.FlatSpec with Matchers with AutoRollback {
  val ac = ArticleComment.syntax("ac")

  behavior of "ArticleComment"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = ArticleComment.find(123)
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = ArticleComment.findBy(sqls.eq(ac.id, 123))
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = ArticleComment.findAll()
    allResults.size should be >(0)
  }
  it should "count all records" in { implicit session =>
    val count = ArticleComment.countAll()
    count should be >(0L)
  }
  it should "find all by where clauses" in { implicit session =>
    val results = ArticleComment.findAllBy(sqls.eq(ac.id, 123))
    results.size should be >(0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = ArticleComment.countBy(sqls.eq(ac.id, 123))
    count should be >(0L)
  }
  it should "create new record" in { implicit session =>
    val created = ArticleComment.create(articleId = 123, content = "MyString")
    created should not be(null)
  }
  it should "save a record" in { implicit session =>
    val entity = ArticleComment.findAll().head
    // TODO modify something
    val modified = entity
    val updated = ArticleComment.save(modified)
    updated should not equal(entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity = ArticleComment.findAll().head
    ArticleComment.destroy(entity)
    val shouldBeNone = ArticleComment.find(123)
    shouldBeNone.isDefined should be(false)
  }

}
