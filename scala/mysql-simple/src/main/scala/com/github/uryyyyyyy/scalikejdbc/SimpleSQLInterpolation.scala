package com.github.uryyyyyyy.scalikejdbc

import scalikejdbc._

object SimpleSQLInterpolation {
  def main(args: Array[String]): Unit = {
    ConnectionPool.singleton("jdbc:mysql://127.0.0.1:3306/scalikejdbc?characterEncoding=utf8", "my_user", "my_password")

    // ad-hoc session provider on the REPL
    implicit val session = AutoSession

    sql"INSERT INTO items VALUES ('3', '1', '1')".update.apply()
  }
}
