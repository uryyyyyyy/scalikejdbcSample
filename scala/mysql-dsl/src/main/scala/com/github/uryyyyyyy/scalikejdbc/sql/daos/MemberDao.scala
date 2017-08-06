package com.github.uryyyyyyy.scalikejdbc.sql.daos

import com.github.uryyyyyyy.scalikejdbc.sql.tables.{Member, MemberTable}
import org.joda.time.{DateTime, LocalDate}
import scalikejdbc._

trait MemberDao {

  def create(name: String, birthday: Option[LocalDate], createdAt: DateTime)(implicit session: DBSession): Member

  def updateAllStatus(member: Member)(implicit session: DBSession): Unit

  def deleteById(id: Long)(implicit session: DBSession): Unit

  def findById(id: Long)(implicit session: DBSession): Option[Member]

  def findAll()(implicit session: DBSession): Set[Member]

  def findAllOrderedById()(implicit session: DBSession): Seq[Member]

  def findByNames(names: Seq[String])(implicit session: DBSession): Seq[Member]
}

object MemberDaoImpl extends MemberDao {
  private val m = MemberTable.syntax("m")
  private val column = MemberTable.column

  override def create(name: String, birthday: Option[LocalDate], createdAt: DateTime)(implicit session: DBSession): Member = {
    val id = applyUpdateAndReturnGeneratedKey {
      insert.into(MemberTable).namedValues(
        column.name -> name,
        column.birthday -> birthday,
        column.createdAt -> createdAt
      )
    }
    Member(id, name, birthday, createdAt)
  }

  override def updateAllStatus(member: Member)(implicit session: DBSession): Unit = {
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

  override def deleteById(id: Long)(implicit session: DBSession): Unit = {
    applyUpdate {
      delete.from(MemberTable as m).where.eq(m.id, id)
    }
  }

  override def findById(id: Long)(implicit session: DBSession): Option[Member] = {
    withSQL {
      select.from(MemberTable as m).where.eq(m.id, id)
    }.map(MemberTable(m))
      .single.apply()
  }

  override def findAll()(implicit session: DBSession): Set[Member] = {
    withSQL {
      selectFrom(MemberTable as m)
    }.map(MemberTable(m))
      .list().apply().toSet
  }

  override def findAllOrderedById()(implicit session: DBSession): Seq[Member] = {
    withSQL {
      selectFrom(MemberTable as m).orderBy(m.id)
    }.map(MemberTable(m))
      .list().apply()
  }

  override def findByNames(names: Seq[String])(implicit session: DBSession): Seq[Member] = {
    withSQL {
      selectFrom(MemberTable as m).where.in(m.name, names)
    }.map(MemberTable(m))
      .list().apply()
  }
}
