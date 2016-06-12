package com.github.uryyyyyyy.scalikejdbc.dsl

import java.util.TimeZone

import com.github.uryyyyyyy.scalikejdbc.dao._
import org.joda.time.DateTime
import scalikejdbc._
import scalikejdbc.config.DBs

object MemberSample2 {
  def main(args: Array[String]) {

    val dbName = 'mysql

    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Tokyo"))

    DBs.setup(dbName)

    NamedDB(dbName).localTx { implicit session =>
      MemberDao.truncateTable()
    }

    val a = NamedDB(dbName).localTx { implicit session =>
      val createdAt = new DateTime(2016, 1, 1, 11, 0, 0)
      MemberDao.create("Alice", None, createdAt)
    }

    //check
    NamedDB(dbName).localTx { implicit session =>
      val alice = MemberDao.findById(a.id)
      println(alice)
    }

  }
}