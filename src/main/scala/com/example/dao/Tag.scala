package com.example.dao

import scalikejdbc._

case class Tag(
  name: String) {

  def save()(implicit session: DBSession = Tag.autoSession): Tag = Tag.save(this)(session)

  def destroy()(implicit session: DBSession = Tag.autoSession): Unit = Tag.destroy(this)(session)

}


object Tag extends SQLSyntaxSupport[Tag] {

  override val tableName = "tag"

  override val columns = Seq("name")

  def apply(t: SyntaxProvider[Tag])(rs: WrappedResultSet): Tag = apply(t.resultName)(rs)
  def apply(t: ResultName[Tag])(rs: WrappedResultSet): Tag = new Tag(
    name = rs.get(t.name)
  )

  val t = Tag.syntax("t")

  override val autoSession = AutoSession

  def find(name: String)(implicit session: DBSession = autoSession): Option[Tag] = {
    withSQL {
      select.from(Tag as t).where.eq(t.name, name)
    }.map(Tag(t.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Tag] = {
    withSQL(select.from(Tag as t)).map(Tag(t.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Tag as t)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Tag] = {
    withSQL {
      select.from(Tag as t).where.append(where)
    }.map(Tag(t.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Tag] = {
    withSQL {
      select.from(Tag as t).where.append(where)
    }.map(Tag(t.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Tag as t).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    name: String)(implicit session: DBSession = autoSession): Tag = {
    withSQL {
      insert.into(Tag).columns(
        column.name
      ).values(
        name
      )
    }.update.apply()

    Tag(
      name = name)
  }

  def save(entity: Tag)(implicit session: DBSession = autoSession): Tag = {
    withSQL {
      update(Tag).set(
        column.name -> entity.name
      ).where.eq(column.name, entity.name)
    }.update.apply()
    entity
  }

  def destroy(entity: Tag)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(Tag).where.eq(column.name, entity.name) }.update.apply()
  }

}
