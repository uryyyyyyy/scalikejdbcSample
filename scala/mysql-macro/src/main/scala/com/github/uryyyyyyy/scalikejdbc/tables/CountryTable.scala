package com.github.uryyyyyyy.scalikejdbc.tables

import scalikejdbc._

case class Country(
  code: String,
  name: String
)

object CountryTable extends SQLSyntaxSupport[Country] {
  override val tableName = "country"
  override val columnNames = autoColumns[Country]()

  def apply(m: ResultName[Country])(rs: WrappedResultSet): Country = autoConstruct(rs, m)

  def truncate()(implicit session: DBSession) = {
    SQL(s"TRUNCATE TABLE $tableName").execute.apply()
  }
}