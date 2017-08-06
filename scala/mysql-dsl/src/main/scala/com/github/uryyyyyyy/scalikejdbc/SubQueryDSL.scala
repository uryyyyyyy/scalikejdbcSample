package com.github.uryyyyyyy.scalikejdbc

import com.github.uryyyyyyy.scalikejdbc.daos._
import com.github.uryyyyyyy.scalikejdbc.tables.{CityTable, CountryTable}
import org.joda.time.{DateTime, LocalDate}
import scalikejdbc._
import scalikejdbc.config.DBs

object SubQueryDSL {
  def main(args: Array[String]): Unit = {
    val dbName = 'mysql
    DBs.loadGlobalSettings()
    DBs.setup(dbName)
    val cityDao: CityDao = CityDaoImpl
    val countryDao: CountryDao = CountryDaoImpl
    val citySubQueryDao: CitySubQueryDao = CitySubQueryDaoImpl

    val (a, b, c) = NamedDB(dbName).localTx { implicit session =>
      CityTable.truncate()
      CountryTable.truncate()
      val createdAt = DateTime.now
      val a = cityDao.create("Tokyo","JPN", Option(LocalDate.now()), createdAt)
      val b = cityDao.create("Osaka","JPN", Option(LocalDate.now()), createdAt)
      val c = cityDao.create("LA","USA", Option(LocalDate.now()), createdAt)

      countryDao.create("JPN", "Japan")
      countryDao.create("USA", "United States of America")
      (a, b, c)
    }


    NamedDB(dbName).localTx { implicit session =>
      val res0 = citySubQueryDao.findById(a.id)
      println(res0)

      val res1 = citySubQueryDao.findById(c.id)
      println(res1)
    }
  }
}
