package com.github.uryyyyyyy.scalikejdbc.dsl

import com.github.uryyyyyyy.scalikejdbc.dao._
import scalikejdbc._
import scalikejdbc.config.DBs

object ItemSample {
  def main(args: Array[String]) {

    val dbName = 'h2

    //use application.conf settings (db.h2.*)
    DBs.setup(dbName)

    NamedDB(dbName).localTx { implicit session =>
      ItemTable.truncate()
    }

    val (a, b, c) = NamedDB(dbName).localTx { implicit session =>
      val a = ItemDao.create("Apple", BigDecimal("11.1111111"))
      val b = ItemDao.create("Banana", BigDecimal("9.9999999999"))
      val c = ItemDao.create("Coconut", BigDecimal("7.5"))
      (a, b, c)
    }

    //check
    NamedDB(dbName).localTx { implicit session =>

      val itemAll: Set[Item] = ItemDao.findAll()
      println(itemAll)

      val alice = ItemDao.findById(a.id)
      println(alice)

      val res1 = ItemDao.findByPrice(BigDecimal("8.00"), BigDecimal("10.00"))
      println(res1)
    }

  }
}