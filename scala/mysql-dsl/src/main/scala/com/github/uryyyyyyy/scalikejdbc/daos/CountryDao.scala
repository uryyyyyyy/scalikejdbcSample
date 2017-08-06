package com.github.uryyyyyyy.scalikejdbc.daos

import com.github.uryyyyyyy.scalikejdbc.tables.{City, CityTable, Country, CountryTable}
import scalikejdbc._

trait CountryDao {
  def create(code: String, name: String)(implicit session: DBSession): Unit
  def findById(code: String)(implicit session: DBSession): Option[Country]
}

object CountryDaoImpl extends CountryDao {
  private val co = CountryTable.syntax("co")
  private val column = CountryTable.column

  override def create(code: String, name: String)(implicit session: DBSession): Unit = {
    withSQL{
      insert.into(CountryTable).namedValues(
        column.code -> code,
        column.name -> name,
      )
    }.update.apply()
  }

  override def findById(code: String)(implicit session: DBSession): Option[Country] = {
    withSQL {
      select(co.*).from(CountryTable as co).where.eq(co.code, code)
    }.map(CountryTable(co)).single.apply()
  }

}
