package com.github.uryyyyyyy.scalikejdbc

import com.github.uryyyyyyy.scalikejdbc.daos.{CityDao, CityDaoImpl}
import com.github.uryyyyyyy.scalikejdbc.tables.{City, CityTable}
import org.joda.time.{DateTime, LocalDate}
import scalikejdbc._
import scalikejdbc.config.DBs

object SimpleDSL {
  def main(args: Array[String]): Unit = {
    val dbName = 'mysql
    DBs.loadGlobalSettings()
    DBs.setup(dbName)
    val cityDao: CityDao = CityDaoImpl

    val (a, b, c) = NamedDB(dbName).localTx { implicit session =>
      CityTable.truncate()
      val createdAt = DateTime.now
      val a = cityDao.create("Tokyo","JPN", Option(LocalDate.now()), createdAt)
      val b = cityDao.create("Osaka","JPN", Option(LocalDate.now()), createdAt)
      val c = cityDao.create("Nagoya","JPN", Option(LocalDate.now()), createdAt)
      (a, b, c)
    }


    NamedDB(dbName).localTx { implicit session =>
      val res0: Set[City] = cityDao.findAll()
      println(res0)

      val res1: Seq[City] = cityDao.findByNames(Seq(b.name, c.name))
      println(res1)

      val tokyo = cityDao.findById(a.id)
      println(tokyo)

      cityDao.updateName(a.id, "new Tokyo")

      val tokyo2 = cityDao.findById(tokyo.get.id)
      println(tokyo2)

      cityDao.deleteById(tokyo.get.id)
      val noo = cityDao.findById(tokyo.get.id)
      println(noo)

      val nooo = cityDao.findById(c.id + 1)
      println(nooo)

      try{
        cityDao.updateName(c.id + 1, "faillll")
      } catch {
        case e:RuntimeException => println(e.getMessage)
      }
    }
  }
}
