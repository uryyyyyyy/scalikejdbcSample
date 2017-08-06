package com.github.uryyyyyyy.scalikejdbc.sql

import com.github.uryyyyyyy.scalikejdbc.sql.daos.{MemberDao, MemberDaoImpl}
import com.github.uryyyyyyy.scalikejdbc.sql.tables.{Member, MemberTable}
import org.joda.time.{DateTime, LocalDate}
import scalikejdbc._
import scalikejdbc.config.DBs

object SimpleDSL {
  def main(args: Array[String]): Unit = {
    val dbName = 'mysql
    DBs.loadGlobalSettings()
    DBs.setup(dbName)
    val memberDao: MemberDao = MemberDaoImpl

    val (a, b, c) = NamedDB(dbName).localTx { implicit session =>
      MemberTable.truncate()
      val createdAt = DateTime.now
      val a = memberDao.create("Alice", Option(new LocalDate("1980-01-01")), createdAt)
      val b = memberDao.create("Bob", None, createdAt)
      val c = memberDao.create("Cindy", Option(new LocalDate("2000-01-01")), createdAt)
      (a, b, c)
    }


    NamedDB(dbName).localTx { implicit session =>
      val res0: Set[Member] = memberDao.findAll()
      println(res0)

      val res1: Seq[Member] = memberDao.findByNames(Seq(b.name, c.name))
      println(res1)

      val alice = memberDao.findById(a.id)
      println(alice)

      val newAlice = Member(alice.get.id, "newAlice", None, alice.get.createdAt)

      memberDao.updateAllStatus(newAlice)

      val alice2 = memberDao.findById(alice.get.id)
      println(alice2)

      memberDao.deleteById(alice.get.id)
      val alice3 = memberDao.findById(alice.get.id)
      println(alice3)

      val noUser = memberDao.findById(c.id + 1)
      println(noUser)

      try{
        memberDao.updateAllStatus(Member(c.id + 1, "noUser", None, DateTime.now))
      } catch {
        case e:RuntimeException => println(e.getMessage)
      }
    }
  }
}
