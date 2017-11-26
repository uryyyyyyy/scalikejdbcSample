package com.github.uryyyyyyy.scalikejdbc.daos

import com.github.uryyyyyyy.scalikejdbc.tables.{City, CityTable}
import org.joda.time.{DateTime, LocalDate}
import scalikejdbc._

trait CityDao {

  def create(name: String, countryCode: String, createdAtDate: Option[LocalDate], createdAtTimestamp: DateTime)(implicit session: DBSession): City

  def updateName(id: Long, name: String)(implicit session: DBSession): Unit

  def deleteById(id: Long)(implicit session: DBSession): Unit

  def findById(id: Long)(implicit session: DBSession): Option[City]

  def findAll()(implicit session: DBSession): Set[City]

  def findAllOrderedById()(implicit session: DBSession): Seq[City]

  def findByNames(names: Seq[String])(implicit session: DBSession): Seq[City]
}

object CityDaoImpl extends CityDao {
  private val c = CityTable.c
  private val column = CityTable.column

  override def create(name: String, countryCode: String, createdAtDate: Option[LocalDate], createdAtTimestamp: DateTime)(implicit session: DBSession): City = {
    val id = withSQL{
      insert.into(CityTable).namedValues(
        column.name -> name,
        column.countryCode -> countryCode,
        column.createdAtDate -> createdAtDate,
        column.createdAtTimestamp -> createdAtTimestamp
      )
    }.updateAndReturnGeneratedKey.apply()

    City(id, name, countryCode, createdAtDate, createdAtTimestamp)
  }

  override def updateName(id: Long, name: String)(implicit session: DBSession): Unit = {
    val updatedNum = withSQL {
      update(CityTable).set(
        column.name -> name
      ).where.eq(column.id, id)
    }.update.apply()

    if(updatedNum == 0){
      throw new RuntimeException("update faild. city:" + id)
    }
  }

  override def deleteById(id: Long)(implicit session: DBSession): Unit = {
    withSQL {
      delete.from(CityTable).where.eq(column.id, id)
    }.update.apply()
  }

  override def findById(id: Long)(implicit session: DBSession): Option[City] = {
    withSQL {
      select(c.result.*).from(CityTable as c).where.eq(c.id, id)
    }.map(CityTable(c.resultName)).single.apply()
  }

  override def findAll()(implicit session: DBSession): Set[City] = {
    withSQL {
      select(c.result.*).from(CityTable as c)
    }.map(CityTable(c.resultName))
      .list().apply().toSet
  }

  override def findAllOrderedById()(implicit session: DBSession): Seq[City] = {
    withSQL {
      select(c.result.*).from(CityTable as c).orderBy(c.id)
    }.map(CityTable(c.resultName))
      .list().apply()
  }

  override def findByNames(names: Seq[String])(implicit session: DBSession): Seq[City] = {
    withSQL {
      select(c.result.*).from(CityTable as c).where.in(c.name, names)
    }.map(CityTable(c.resultName))
      .list().apply()
  }
}
