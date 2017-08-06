package com.github.uryyyyyyy.scalikejdbc.daos

import com.github.uryyyyyyy.scalikejdbc.tables.{City, CityTable, Country, CountryTable}
import org.joda.time.{DateTime, LocalDate}
import scalikejdbc._

case class CityWithCountry(
  id: Long,
  name: String,
  countryName: String
)

trait CityJoinDao {
  def findById(id: Long)(implicit session: DBSession): Option[CityWithCountry]
}

object CityJoinDaoImpl extends CityJoinDao {
  private val c = CityTable.syntax("c")
  private val co = CountryTable.syntax("co")

  override def findById(id: Long)(implicit session: DBSession): Option[CityWithCountry] = {
    withSQL {
      select(c.result.*, co.result.name).from(CityTable as c).innerJoin(CountryTable as co).on(c.countryCode, co.code)
        .where.eq(c.id, id)
    }.map(toCityWithCountry(c.resultName, co.resultName)).single.apply()
  }

  def toCityWithCountry(c: ResultName[City], co: ResultName[Country])(rs: WrappedResultSet) = CityWithCountry(
    id = rs.long(c.id),
    name = rs.string(c.name),
    countryName = rs.string(co.name),
  )

}
