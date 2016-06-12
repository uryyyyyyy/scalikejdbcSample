package com.github.uryyyyyyy.scalikejdbc.sql

import com.github.uryyyyyyy.scalikejdbc.dao.{Member, MemberDao}
import org.joda.time.{DateTime, LocalDate}
import scalikejdbc._
import scalikejdbc.config.DBs

object Sample1 {
  def main(args: Array[String]) {

    val dbName = 'h2

    DBs.setup(dbName)

    NamedDB(dbName).localTx { implicit session =>
      MemberDao.truncateTable()
    }

    val toMember = (rs: WrappedResultSet) => Member(
      rs.long("id"),
      rs.string("name"),
      rs.jodaLocalDateOpt("birthday"),
      rs.jodaDateTime("created_at")
    )

    val members: List[Member] = NamedDB(dbName) readOnly { implicit session =>
      SQL("select * from members").map(toMember).list.apply()
    }
    println(members)

    NamedDB(dbName) localTx { implicit session =>
      val insertSql = SQL("insert into members (name, birthday, created_at) values (?, ?, ?)")
      val createdAt = DateTime.now

      insertSql.bind("Alice", Option(new LocalDate("1980-01-01")), createdAt).update.apply()
      insertSql.bind("Bob", None, createdAt).update.apply()
    }

    val members2: List[Member] = NamedDB(dbName) readOnly { implicit session =>
      SQL("select * from members").map(toMember).list.apply()
    }
    println(members2)

  }
}