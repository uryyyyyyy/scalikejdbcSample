package com.github.uryyyyyyy.scalikejdbc.dao

import java.util.UUID

import scalikejdbc._

case class ItemId(value: String) extends AnyVal

case class Item(
  id: ItemId,
  label: String,
  price: BigDecimal
)

object ItemTable extends SQLSyntaxSupport[Item] {
  override val tableName = "items"
  override val columnNames = Seq("id", "label", "price")

  def apply(u: SyntaxProvider[Item])(rs: WrappedResultSet): Item =
    Item(ItemId(rs.string(column.id)), rs.string(column.label), rs.bigDecimal(column.price))

  def truncate()(implicit session: DBSession) = {
    SQL(s"TRUNCATE TABLE $tableName").execute.apply()
  }
}

trait ItemDao {
  private val i = ItemTable.syntax("i")
  private val column = ItemTable.column

  def create(label: String, price: BigDecimal)(implicit session: DBSession): Item = {
    val id = UUID.randomUUID().toString
    applyUpdate {
      insertInto(ItemTable).namedValues(
        column.id -> id,
        column.label -> label,
        column.price -> price
      )
    }
    Item(ItemId(id), label, price)
  }

  def deleteById(id: String)(implicit session: DBSession): Unit = {
    applyUpdate {
      deleteFrom(ItemTable as i).where.eq(i.id, id)
    }
  }

  def findById(id: ItemId)(implicit session: DBSession): Option[Item] = {
    withSQL {
      selectFrom(ItemTable as i).where.eq(i.id, id.value)
    }.map(ItemTable(i))
      .single.apply()
  }

  def findAll()(implicit session: DBSession): Set[Item] = {
    withSQL {
      selectFrom(ItemTable as i)
    }.map(ItemTable(i))
      .list().apply().toSet
  }

  def findAllOrderedById()(implicit session: DBSession): Seq[Item] = {
    withSQL {
      selectFrom(ItemTable as i).orderBy(i.id)
    }.map(ItemTable(i))
      .list().apply()
  }

  def findByPrice(fromPrice: BigDecimal, toPrice: BigDecimal)(implicit session: DBSession): Seq[Item] = {
    withSQL {
      selectFrom(ItemTable as i).where
        .gt(i.price, fromPrice).and
        .lt(i.price, toPrice)
        .orderBy(i.price)
    }.map(ItemTable(i))
      .list().apply()
  }
}

object ItemDao extends ItemDao
