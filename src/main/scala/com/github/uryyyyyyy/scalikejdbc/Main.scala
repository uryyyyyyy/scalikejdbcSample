package com.github.uryyyyyyy.scalikejdbc

import org.joda.time.{DateTime, LocalDate}
import scalikejdbc._

object Main {
  def main(args: Array[String]) {

    case class Member(
      id: Long,
      name: String,
      description: Option[String] = None,
      birthday: Option[LocalDate] = None,
      createdAt: DateTime)

    val allColumns = (rs: WrappedResultSet) => Member(
      id = rs.long("id"),
      name = rs.string("name"),
      description = rs.stringOpt("description"),
      birthday = rs.jodaLocalDateOpt("birthday"),
      createdAt = rs.jodaDateTime("created_at")
    )

    Class.forName("com.mysql.jdbc.Driver")
    ConnectionPool.singleton("jdbc:mysql://localhost:3306/scalike","root","root",
      new ConnectionPoolSettings(initialSize = 5, maxSize = 10))

    DB autoCommit { implicit session =>

      SQL("insert into members (name, birthday, created_at) values ({name}, {birthday}, {createdAt})")
        .bindByName('name -> "Alice", 'birthday -> None, 'createdAt -> DateTime.now)
        .update.apply()

      val s = SQL("select * from members limit 10").map(allColumns).list.apply()
      //      SQL("""
      //    create table members (
      //      id bigint primary key auto_increment,
      //      name varchar(30) not null,
      //      description varchar(1000),
      //      birthday date,
      //      created_at timestamp not null
      //    )
      //          """).execute.apply()
      println(s)
    }
  }
}