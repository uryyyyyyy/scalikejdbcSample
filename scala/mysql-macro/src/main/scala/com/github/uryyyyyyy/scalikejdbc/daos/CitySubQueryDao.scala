package com.github.uryyyyyyy.scalikejdbc.daos

import com.github.uryyyyyyy.scalikejdbc.tables.{CityTable, Country, CountryTable}
import scalikejdbc._

trait CitySubQueryDao {
  def findById(id: Long)(implicit session: DBSession): Option[Country]
}

object CitySubQueryDaoImpl extends CitySubQueryDao {
  private val c = CityTable.syntax("c")
  private val co = CountryTable.syntax("co")

  override def findById(id: Long)(implicit session: DBSession): Option[Country] = {
    val subQuery = SubQuery.syntax("sub").include(c)

    withSQL {
      select(co.result.*).from(CountryTable as co)
        .innerJoin(select(c.result.countryCode).from(CityTable as c).where.eq(c.id, id).as(subQuery))
        .on(subQuery(c).countryCode, co.code)
    }.map(toCountry(co.resultName)).single.apply()
  }

  def toCountry(c: ResultName[Country])(rs: WrappedResultSet) = Country(
    code = rs.string(c.code),
    name = rs.string(c.name)
  )

}
