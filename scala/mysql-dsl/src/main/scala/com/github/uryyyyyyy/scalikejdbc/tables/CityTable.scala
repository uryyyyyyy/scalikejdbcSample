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
  override val columnNames = Seq("id", "name", "country_code", "created_at_date", "created_at_timestamp")

  def apply(m: ResultName[City])(rs: WrappedResultSet): City = City(
    rs.long(m.id),
    rs.string(m.name),
    rs.string(m.countryCode),
    rs.jodaLocalDateOpt(m.createdAtDate),
    rs.jodaDateTime(m.createdAtTimestamp)
  )

  def truncate()(implicit session: DBSession) = {
    SQL(s"TRUNCATE TABLE $tableName").execute.apply()
  }
}