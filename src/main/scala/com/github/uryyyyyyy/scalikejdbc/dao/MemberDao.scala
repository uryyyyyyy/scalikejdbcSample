package com.github.uryyyyyyy.scalikejdbc.dao

import org.joda.time.{DateTime, LocalDate}
import scalikejdbc.{autoConstruct, _}

case class Member(
  id: Long,
  name: String,
  birthday: Option[LocalDate] = None,
  createdAt: DateTime
)

object MemberTable extends SQLSyntaxSupport[Member] {
  override val tableName = "members"
  override val columnNames = Seq("id", "name", "birthday", "created_at")

  def apply(u: SyntaxProvider[Member])(rs: WrappedResultSet): Member = autoConstruct(rs, u)

  def truncate()(implicit session: DBSession) = {
    SQL(s"TRUNCATE TABLE $tableName").execute.apply()
  }
}

trait MemberDao {

  private val m = MemberTable.syntax("m")
  private val column = MemberTable.column

  def create(name: String, birthday: Option[LocalDate], createdAt: DateTime)(implicit session: DBSession): Member = {
    val id = applyUpdateAndReturnGeneratedKey {
      insertInto(MemberTable).namedValues(
        column.name -> name,
        column.birthday -> birthday,
        column.createdAt -> createdAt
      )
    }
    Member(id, name, birthday, createdAt)
  }

  def updateAllStatus(member: Member)(implicit session: DBSession): Unit = {
    val isSuccess = applyUpdate {
      update(MemberTable).set(
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
      deleteFrom(MemberTable as m).where.eq(m.id, id)
    }
  }

  def findById(id: Long)(implicit session: DBSession): Option[Member] = {
    withSQL {
      selectFrom(MemberTable as m).where.eq(m.id, id)
    }.map(MemberTable(m))
      .single.apply()
  }

  def findAll()(implicit session: DBSession): Set[Member] = {
    withSQL {
      selectFrom(MemberTable as m)
    }.map(MemberTable(m))
      .list().apply().toSet
  }

  def findAllOrderedById()(implicit session: DBSession): Seq[Member] = {
    withSQL {
      selectFrom(MemberTable as m).orderBy(m.id)
    }.map(MemberTable(m))
      .list().apply()
  }

  def findByNames(names: Seq[String])(implicit session: DBSession): Seq[Member] = {
    withSQL {
      selectFrom(MemberTable as m).where.in(m.name, names)
    }.map(MemberTable(m))
      .list().apply()
  }
}

object MemberDao extends MemberDao
