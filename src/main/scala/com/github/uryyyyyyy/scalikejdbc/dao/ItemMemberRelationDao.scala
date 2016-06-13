package com.github.uryyyyyyy.scalikejdbc.dao

import scalikejdbc._

case class ItemMemberRelation(
  memberId: Long,
  itemId: String
)

object ItemMemberRelationTable extends SQLSyntaxSupport[ItemMemberRelation] {
  override val tableName = "item_member_relations"
  override val columnNames = Seq("member_id", "item_id")

  def truncate()(implicit session: DBSession) = {
    SQL(s"TRUNCATE TABLE $tableName").execute.apply()
  }
}

trait ItemMemberRelationDao {
  private val imr = ItemMemberRelationTable.syntax("imr")
  private val column = ItemMemberRelationTable.column

  def create(memberId: Long, itemId: String)(implicit session: DBSession): Unit = {
    applyUpdate {
      insertInto(ItemMemberRelationTable).namedValues(
        column.memberId -> memberId,
        column.itemId -> itemId
      )
    }
  }

  def deleteByItemId(itemId: String)(implicit session: DBSession): Unit = {
    applyUpdate {
      deleteFrom(ItemMemberRelationTable as imr).where.eq(imr.itemId, itemId)
    }
  }

  def findMemberIds(itemId: ItemId)(implicit session: DBSession): Set[Long] = {
    withSQL {
      selectFrom(ItemMemberRelationTable as imr).where.eq(imr.itemId, itemId.value)
    }.map(rs => rs.long(column.memberId))
      .list().apply().toSet
  }

  def findMembers(itemId: ItemId)(implicit session: DBSession): Set[Member] = {
    val m = MemberTable.syntax("m")
    withSQL {
      selectFrom(ItemMemberRelationTable as imr)
        .join(MemberTable as m).on(imr.memberId, m.id)
        .where.eq(imr.itemId, itemId.value)
    }.map(MemberTable(m))
      .list().apply().toSet
  }

  def findItems(memberId: Long)(implicit session: DBSession): Set[Item] = {
    val i = ItemTable.syntax("i")

    val sub = select(imr.itemId).from(ItemMemberRelationTable as imr)
      .where.eq(imr.memberId, memberId)
      .and.eq(i.id, imr.itemId)

    withSQL {
      selectFrom(ItemTable as i)
        .where.exists(sub)
    }.map(ItemTable(i))
      .list().apply().toSet
  }
}

object ItemMemberRelationDao extends ItemMemberRelationDao