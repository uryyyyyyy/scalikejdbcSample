package com.example.dao

import scalikejdbc._
import java.time.{ZonedDateTime}
import scalikejdbc.jsr310._

case class Article(
  id: Int,
  title: String,
  content: String,
  createdDatetime: ZonedDateTime) {

  def save()(implicit session: DBSession): Article = Article.save(this)(session)

  def destroy()(implicit session: DBSession): Unit = Article.destroy(this)(session)

}


object Article extends SQLSyntaxSupport[Article] {

  override val tableName = "article"

  override val columns = Seq("id", "title", "content", "created_datetime")

  def apply(a: SyntaxProvider[Article])(rs: WrappedResultSet): Article = apply(a.resultName)(rs)
  def apply(a: ResultName[Article])(rs: WrappedResultSet): Article = new Article(
    id = rs.get(a.id),
    title = rs.get(a.title),
    content = rs.get(a.content),
    createdDatetime = rs.get(a.createdDatetime)
  )

  val a = Article.syntax("a")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession): Option[Article] = {
    withSQL {
      select.from(Article as a).where.eq(a.id, id)
    }.map(Article(a.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession): List[Article] = {
    withSQL(select.from(Article as a)).map(Article(a.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession): Long = {
    withSQL(select(sqls.count).from(Article as a)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession): Option[Article] = {
    withSQL {
      select.from(Article as a).where.append(where)
    }.map(Article(a.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession): List[Article] = {
    withSQL {
      select.from(Article as a).where.append(where)
    }.map(Article(a.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession): Long = {
    withSQL {
      select(sqls.count).from(Article as a).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    title: String,
    content: String,
    createdDatetime: ZonedDateTime)(implicit session: DBSession): Article = {
    val generatedKey = withSQL {
      insert.into(Article).columns(
        column.title,
        column.content,
        column.createdDatetime
      ).values(
        title,
        content,
        createdDatetime
      )
    }.updateAndReturnGeneratedKey.apply()

    Article(
      id = generatedKey.toInt,
      title = title,
      content = content,
      createdDatetime = createdDatetime)
  }

  def save(entity: Article)(implicit session: DBSession): Article = {
    withSQL {
      update(Article).set(
        column.id -> entity.id,
        column.title -> entity.title,
        column.content -> entity.content,
        column.createdDatetime -> entity.createdDatetime
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: Article)(implicit session: DBSession): Unit = {
    withSQL { delete.from(Article).where.eq(column.id, entity.id) }.update.apply()
  }

}
