package com.github.uryyyyyyy.scalikejdbc.dsl

import com.github.uryyyyyyy.scalikejdbc.dao._
import org.joda.time.{DateTime, LocalDate}
import scalikejdbc._
import scalikejdbc.config.DBs

object MemberSample {
  def main(args: Array[String]) {

    val dbName = 'h2

    DBs.setup(dbName)

    NamedDB(dbName).localTx { implicit session =>
      MemberDao.truncateTable()
    }

    val (a, b, c) = NamedDB(dbName).localTx { implicit session =>
      val createdAt = DateTime.now
      val a = MemberDao.create("Alice", Option(new LocalDate("1980-01-01")), createdAt)
      val b = MemberDao.create("Bob", None, createdAt)
      val c = MemberDao.create("Cindy", Option(new LocalDate("2000-01-01")), createdAt)
      (a, b, c)
    }

    //check
    NamedDB(dbName).localTx { implicit session =>
      val res0: Set[Member] = MemberDao.findAll()
      println(res0)

      val res1: Seq[Member] = MemberDao.findByNames(Seq(b.name, c.name))
      println(res1)

      val alice = MemberDao.findById(a.id)
      println(alice)

      val newAlice = Member(alice.get.id, "newAlice", None, alice.get.createdAt)

      MemberDao.updateAllStatus(newAlice)

      val alice2 = MemberDao.findById(alice.get.id)
      println(alice2)

      MemberDao.deleteById(alice.get.id)
      val alice3 = MemberDao.findById(alice.get.id)
      println(alice3)


      val noUser = MemberDao.findById(c.id + 1)
      println(noUser)

      try{
        MemberDao.updateAllStatus(Member(c.id + 1, "noUser", None, DateTime.now))
      } catch {
        case e:RuntimeException => println(e.getMessage)
      }
    }

  }
}