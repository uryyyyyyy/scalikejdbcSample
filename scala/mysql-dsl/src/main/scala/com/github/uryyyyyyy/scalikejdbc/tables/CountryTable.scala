package com.github.uryyyyyyy.scalikejdbc.tables

import scalikejdbc._

case class Country(
  code: String,
  name: String
)

object CountryTable extends SQLSyntaxSupport[Country] {
  override val tableName = "country"
  override val columnNames = Seq("code", "name")

  def apply(m: SyntaxProvider[Country])(rs: WrappedResultSet): Country = Country(
    rs.string(m.code),
    rs.string(m.name)
  )

  def truncate()(implicit session: DBSession) = {
    SQL(s"TRUNCATE TABLE $tableName").execute.apply()
  }
}