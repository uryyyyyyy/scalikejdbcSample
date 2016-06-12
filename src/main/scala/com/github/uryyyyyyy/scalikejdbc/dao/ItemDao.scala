package com.github.uryyyyyyy.scalikejdbc.dao

import java.util.UUID

import scalikejdbc.{autoConstruct, _}

case class Item(
  id: String,
  label: String,
  price: BigDecimal
)

object ItemDao extends SQLSyntaxSupport[Item] {
  override val tableName = "items"
  override val columnNames = Seq("id", "label", "price")
  val alias = ItemDao.syntax("i")

  def toModel(rs: WrappedResultSet): Item = autoConstruct(rs, alias.resultName)

  def create(label: String, price: BigDecimal)(implicit session: DBSession): Item = {
    val id = UUID.randomUUID().toString
    applyUpdate {
      insertInto(this).namedValues(
        column.id -> id,
        column.label -> label,
        column.price -> price
      )
    }
    Item(id, label, price)
  }

  def deleteById(id: String)(implicit session: DBSession): Unit = {
    applyUpdate {
      deleteFrom(this as alias).where.eq(alias.id, id)
    }
  }

  def findById(id: String)(implicit session: DBSession): Option[Item] = {
    withSQL {
      selectFrom(this as alias).where.eq(alias.id, id)
    }.map(toModel(_))
      .single.apply()
  }

  def findAll()(implicit session: DBSession): Set[Item] = {
    withSQL {
      selectFrom(this as alias)
    }.map(toModel(_))
      .list().apply().toSet
  }

  def findAllOrderedById()(implicit session: DBSession): Seq[Item] = {
    withSQL {
      selectFrom(this as alias).orderBy(alias.id)
    }.map(toModel(_))
      .list().apply()
  }

  def findByPrice(fromPrice: BigDecimal, toPrice: BigDecimal)(implicit session: DBSession): Seq[Item] = {
    withSQL {
      selectFrom(this as alias).where
        .gt(alias.price, fromPrice).and
        .lt(alias.price, toPrice)
        .orderBy(alias.price)
    }.map(toModel(_))
      .list().apply()
  }

  def truncateTable()(implicit session: DBSession) = {
    SQL(s"TRUNCATE TABLE $tableName").execute.apply()
  }
}