package com.github.uryyyyyyy.scalikejdbc.dao

import org.scalatest.{MustMatchers, fixture}
import scalikejdbc._
import scalikejdbc.config.DBs
import scalikejdbc.scalatest.AutoRollback


class ItemDaoTest extends fixture.FunSpec with MustMatchers with AutoRollback {

  val dbName = 'h2Test

  override def db(): DB = {
    if (!ConnectionPool.isInitialized(dbName)) DBs.setup(dbName)
    NamedDB(dbName).toDB()
  }

  override def fixture(implicit session: DBSession): Unit = {
    ItemTable.truncate()

    ItemDao.create("Apple", BigDecimal("11.1111111"))
    ItemDao.create("Banana", BigDecimal("9.9999999999"))
    ItemDao.create("Coconut", BigDecimal("7.5"))
  }

  describe("ItemDaoTest") {
    it("test1") { implicit session =>

      val itemAll: Set[Item] = ItemDao.findAll()
      itemAll.size mustBe 3

      val apple = itemAll.find(_.label == "Apple").get
      apple.price mustBe BigDecimal("11.1111111")
    }
  }

}
