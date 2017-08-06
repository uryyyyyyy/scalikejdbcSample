package com.github.uryyyyyyy.scalikejdbc.sql.tables

import org.joda.time.{DateTime, LocalDate}
import scalikejdbc._

case class Member(
  id: Long,
  name: String,
  birthday: Option[LocalDate] = None,
  createdAt: DateTime
)

object MemberTable extends SQLSyntaxSupport[Member] {
  override val tableName = "members"
  override val columnNames = Seq("id", "name", "birthday", "created_at")

  def apply(m: SyntaxProvider[Member])(rs: WrappedResultSet): Member = Member(
    rs.long(m.id),
    rs.string(m.name),
    rs.jodaLocalDateOpt(m.birthday),
    rs.jodaDateTime(m.createdAt)
  )

  def truncate()(implicit session: DBSession) = {
    SQL(s"TRUNCATE TABLE $tableName").execute.apply()
  }
}