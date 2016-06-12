package com.github.uryyyyyyy.scalikejdbc.dao

import scalikejdbc._

case class ItemMemberRelation(
  memberId: Long,
  itemId: String
)

object ItemMemberRelationDao extends SQLSyntaxSupport[ItemMemberRelation] {
  override val tableName = "item_member_relations"
  override val columnNames = Seq("member_id", "item_id")
  val alias = ItemMemberRelationDao.syntax("imr")
  val imr = alias

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
      deleteFrom(this as alias).where.eq(alias.itemId, itemId)
    }
  }

  def findMemberIds(itemId: ItemId)(implicit session: DBSession): Set[Long] = {
    withSQL {
      selectFrom(this as alias).where.eq(alias.itemId, itemId.value)
    }.map(rs => rs.long(column.memberId))
      .list().apply().toSet
  }

  def findMembers(itemId: ItemId)(implicit session: DBSession): Set[Member] = {
    val m = MemberDao.alias
    withSQL {
      selectFrom(this as alias)
        .join(MemberDao as m).on(alias.memberId, m.id)
        .where.eq(alias.itemId, itemId.value)
    }.map(MemberDao.toModel)
      .list().apply().toSet
  }

  def findItems(memberId: Long)(implicit session: DBSession): Set[Item] = {
    val i = ItemDao.alias

    val sub = select(imr.itemId).from(this as imr)
      .where.eq(imr.memberId, memberId)
      .and.eq(i.id, imr.itemId)

    withSQL {
      selectFrom(ItemDao as i)
        .where.exists(sub)
    }.map(ItemDao.toModel)
      .list().apply().toSet
  }

  def truncateTable()(implicit session: DBSession) = {
    SQL(s"TRUNCATE TABLE $tableName").execute.apply()
  }
}