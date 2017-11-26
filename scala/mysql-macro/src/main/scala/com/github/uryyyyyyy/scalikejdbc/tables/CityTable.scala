package com.github.uryyyyyyy.scalikejdbc.tables

import org.joda.time.{DateTime, LocalDate}
import scalikejdbc._

case class City(
  id: Long,
  name: String,
  countryCode: String,
  createdAtDate: Option[LocalDate] = None,
  createdAtTimestamp: DateTime
)

object CityTable extends SQLSyntaxSupport[City] {
  override val tableName = "city"
  lazy val c = CityTable.syntax("c")
  override val columnNames = autoColumns[City]()

  def apply(m: ResultName[City])(rs: WrappedResultSet): City = autoConstruct(rs, m)

  def truncate()(implicit session: DBSession) = {
    SQL(s"TRUNCATE TABLE $tableName").execute.apply()
  }
}
