package com.github.uryyyyyyy.scalikejdbc.dao

import scalikejdbc._

case class ItemMemberRelation(
  memberId: Long,
  itemId: String
)

object ItemMemberRelationDao extends SQLSyntaxSupport[ItemMemberRelation] {
  override val tableName = "item_member_relations"
  override val columnNames = Seq("member_id", "item_id")
  val imr = ItemMemberRelationDao.syntax("imr")

  def create(memberId: Long, itemId: String)(implicit session: DBSession): Unit = {
    applyUpdate {
      insertInto(this).namedValues(
        column.memberId -> memberId,
        column.itemId -> itemId
      )
    }
  }

  def deleteByItemId(itemId: String)(implicit session: DBSession): Unit = {
    applyUpdate {
      deleteFrom(this as imr).where.eq(imr.itemId, itemId)
    }
  }

  def findMemberIds(itemId: String)(implicit session: DBSession): Set[Long] = {
    withSQL {
      selectFrom(this as imr).where.eq(imr.itemId, itemId)
    }.map(rs => rs.long(column.memberId))
      .list().apply().toSet
  }

  def findMembers(itemId: String)(implicit session: DBSession): Set[Member] = {
    val m = MemberDao.m
    withSQL {
      selectFrom(this as imr)
        .join(MemberDao as m).on(imr.memberId, m.id)
        .where.eq(imr.itemId, itemId)
    }.map(MemberDao.toModel)
      .list().apply().toSet
  }

  def truncateTable()(implicit session: DBSession) = {
    SQL(s"TRUNCATE TABLE $tableName").execute.apply()
  }
}