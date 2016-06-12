package com.github.uryyyyyyy.scalikejdbc.dao

import org.joda.time.{DateTime, LocalDate}
import scalikejdbc.{autoConstruct, _}

case class Member(
  id: Long,
  name: String,
  birthday: Option[LocalDate] = None,
  createdAt: DateTime
)

object MemberDao extends SQLSyntaxSupport[Member] {
  override val tableName = "members"
  override val columnNames = Seq("id", "name", "birthday", "created_at")
  val m = MemberDao.syntax("m")

  def toModel(rs: WrappedResultSet): Member = autoConstruct(rs, m.resultName)

  def create(name: String, birthday: Option[LocalDate], createdAt: DateTime)(implicit session: DBSession): Member = {
    val id = applyUpdateAndReturnGeneratedKey {
      insertInto(this).namedValues(
        column.name -> name,
        column.birthday -> birthday,
        column.createdAt -> createdAt
      )
    }
    Member(id, name, birthday, createdAt)
  }

  def updateAllStatus(member: Member)(implicit session: DBSession): Unit = {
    val isSuccess = applyUpdate {
      update(this).set(
        column.name -> member.name,
        column.birthday -> member.birthday
      ).where.eq(column.id, member.id)
    } > 0

    if(!isSuccess){
      throw new RuntimeException("update faild. member:" + member.toString)
    }
  }

  def deleteById(id: Long)(implicit session: DBSession): Unit = {
    applyUpdate {
      deleteFrom(this as m).where.eq(m.id, id)
    }
  }

  def findById(id: Long)(implicit session: DBSession): Option[Member] = {
    withSQL {
      selectFrom(this as m).where.eq(m.id, id)
    }.map(toModel(_))
      .single.apply()
  }

  def findAll()(implicit session: DBSession): Set[Member] = {
    withSQL {
      selectFrom(this as m)
    }.map(toModel(_))
      .list().apply().toSet
  }

  def findAllOrderedById()(implicit session: DBSession): Seq[Member] = {
    withSQL {
      selectFrom(this as m).orderBy(m.id)
    }.map(toModel(_))
      .list().apply()
  }

  def truncateTable()(implicit session: DBSession) = {
    SQL(s"TRUNCATE TABLE $tableName").execute.apply()
  }
}