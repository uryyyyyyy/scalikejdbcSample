package com.example.dao

import org.scalatest._
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc._


class TagSpec extends fixture.FlatSpec with Matchers with AutoRollback {
  val t = Tag.syntax("t")

  behavior of "Tag"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = Tag.find("MyString")
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = Tag.findBy(sqls.eq(t.name, "MyString"))
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = Tag.findAll()
    allResults.size should be >(0)
  }
  it should "count all records" in { implicit session =>
    val count = Tag.countAll()
    count should be >(0L)
  }
  it should "find all by where clauses" in { implicit session =>
    val results = Tag.findAllBy(sqls.eq(t.name, "MyString"))
    results.size should be >(0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = Tag.countBy(sqls.eq(t.name, "MyString"))
    count should be >(0L)
  }
  it should "create new record" in { implicit session =>
    val created = Tag.create(name = "MyString")
    created should not be(null)
  }
  it should "save a record" in { implicit session =>
    val entity = Tag.findAll().head
    // TODO modify something
    val modified = entity
    val updated = Tag.save(modified)
    updated should not equal(entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity = Tag.findAll().head
    Tag.destroy(entity)
    val shouldBeNone = Tag.find("MyString")
    shouldBeNone.isDefined should be(false)
  }

}
