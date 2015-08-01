package com.example.dao

import org.scalatest._
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc._


class RelationArticleTagSpec extends fixture.FlatSpec with Matchers with AutoRollback {
  val rat = RelationArticleTag.syntax("rat")

  behavior of "RelationArticleTag"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = RelationArticleTag.find(123, "MyString")
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = RelationArticleTag.findBy(sqls.eq(rat.articleId, 123))
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = RelationArticleTag.findAll()
    allResults.size should be >(0)
  }
  it should "count all records" in { implicit session =>
    val count = RelationArticleTag.countAll()
    count should be >(0L)
  }
  it should "find all by where clauses" in { implicit session =>
    val results = RelationArticleTag.findAllBy(sqls.eq(rat.articleId, 123))
    results.size should be >(0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = RelationArticleTag.countBy(sqls.eq(rat.articleId, 123))
    count should be >(0L)
  }
  it should "create new record" in { implicit session =>
    val created = RelationArticleTag.create(articleId = 123, tagName = "MyString")
    created should not be(null)
  }
  it should "save a record" in { implicit session =>
    val entity = RelationArticleTag.findAll().head
    // TODO modify something
    val modified = entity
    val updated = RelationArticleTag.save(modified)
    updated should not equal(entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity = RelationArticleTag.findAll().head
    RelationArticleTag.destroy(entity)
    val shouldBeNone = RelationArticleTag.find(123, "MyString")
    shouldBeNone.isDefined should be(false)
  }

}
