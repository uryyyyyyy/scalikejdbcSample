package com.github.uryyyyyyy.scalikejdbc.dsl

import com.github.uryyyyyyy.scalikejdbc.dao._
import org.joda.time.{DateTime, LocalDate}
import scalikejdbc._
import scalikejdbc.config.DBs

object ItemMemberRelationSample {
  def main(args: Array[String]) {

    val dbName = 'h2

    //use application.conf settings (db.h2.*)
    DBs.setup(dbName)

    NamedDB(dbName).localTx { implicit session =>
      ItemTable.truncate()
      MemberTable.truncate()
      ItemMemberRelationTable.truncate()
    }

    val (ia, ib, ic) = NamedDB(dbName).localTx { implicit session =>
      val a = ItemDao.create("Apple", BigDecimal("11.1111111"))
      val b = ItemDao.create("Banana", BigDecimal("9.9999999999"))
      val c = ItemDao.create("Coconut", BigDecimal("7.5"))
      (a, b, c)
    }

    val (ma, mb, mc) = NamedDB(dbName).localTx { implicit session =>
      val createdAt = DateTime.now
      val a = MemberDao.create("Alice", Option(new LocalDate("1980-01-01")), createdAt)
      val b = MemberDao.create("Bob", None, createdAt)
      val c = MemberDao.create("Cindy", Option(new LocalDate("2000-01-01")), createdAt)
      (a, b, c)
    }

    NamedDB(dbName).localTx { implicit session =>
      ItemMemberRelationDao.create(ma.id, ia.id.value)
      ItemMemberRelationDao.create(mb.id, ib.id.value)
      ItemMemberRelationDao.create(mb.id, ia.id.value)
    }

    NamedDB(dbName).localTx { implicit session =>

      val memberIds: Set[Long] = ItemMemberRelationDao.findMemberIds(ia.id)
      println(memberIds)

      val members: Set[Member] = ItemMemberRelationDao.findMembers(ia.id)
      println(members)

      ItemDao.findAll().foreach(println)
      println("start")
      val items: Set[Item] = ItemMemberRelationDao.findItems(mb.id)
      println(items)
    }

  }
}