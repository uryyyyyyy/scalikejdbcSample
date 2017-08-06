package com.github.uryyyyyyy.scalikejdbc.sql

import com.github.uryyyyyyy.scalikejdbc.sql.daos.{MemberDao, MemberDaoImpl}
import org.joda.time.{DateTime, LocalDate}
import scalikejdbc._
import scalikejdbc.config.DBs

object SimpleSQLInterpolation {
  def main(args: Array[String]): Unit = {
    val dbName = 'mysql
    DBs.loadGlobalSettings()
    DBs.setup(dbName)
    val memberDao: MemberDao = MemberDaoImpl
    NamedDB(dbName).localTx { implicit session =>
      val createdAt = DateTime.now
      val a = memberDao.create("Alice", Option(new LocalDate("1980-01-01")), createdAt)
      println(a)
      val b = memberDao.findById(a.id)
      println(b)
    }
  }
}
