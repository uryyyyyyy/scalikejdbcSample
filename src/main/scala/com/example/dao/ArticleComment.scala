package com.example.dao

import scalikejdbc._

case class ArticleComment(
  id: Int,
  articleId: Int,
  content: String,
  username: Option[String] = None) {

  def save()(implicit session: DBSession = ArticleComment.autoSession): ArticleComment = ArticleComment.save(this)(session)

  def destroy()(implicit session: DBSession = ArticleComment.autoSession): Unit = ArticleComment.destroy(this)(session)

}


object ArticleComment extends SQLSyntaxSupport[ArticleComment] {

  override val tableName = "article_comment"

  override val columns = Seq("id", "article_id", "content", "username")

  def apply(ac: SyntaxProvider[ArticleComment])(rs: WrappedResultSet): ArticleComment = apply(ac.resultName)(rs)
  def apply(ac: ResultName[ArticleComment])(rs: WrappedResultSet): ArticleComment = new ArticleComment(
    id = rs.get(ac.id),
    articleId = rs.get(ac.articleId),
    content = rs.get(ac.content),
    username = rs.get(ac.username)
  )

  val ac = ArticleComment.syntax("ac")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[ArticleComment] = {
    withSQL {
      select.from(ArticleComment as ac).where.eq(ac.id, id)
    }.map(ArticleComment(ac.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[ArticleComment] = {
    withSQL(select.from(ArticleComment as ac)).map(ArticleComment(ac.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(ArticleComment as ac)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[ArticleComment] = {
    withSQL {
      select.from(ArticleComment as ac).where.append(where)
    }.map(ArticleComment(ac.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[ArticleComment] = {
    withSQL {
      select.from(ArticleComment as ac).where.append(where)
    }.map(ArticleComment(ac.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(ArticleComment as ac).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    articleId: Int,
    content: String,
    username: Option[String] = None)(implicit session: DBSession = autoSession): ArticleComment = {
    val generatedKey = withSQL {
      insert.into(ArticleComment).columns(
        column.articleId,
        column.content,
        column.username
      ).values(
        articleId,
        content,
        username
      )
    }.updateAndReturnGeneratedKey.apply()

    ArticleComment(
      id = generatedKey.toInt,
      articleId = articleId,
      content = content,
      username = username)
  }

  def save(entity: ArticleComment)(implicit session: DBSession = autoSession): ArticleComment = {
    withSQL {
      update(ArticleComment).set(
        column.id -> entity.id,
        column.articleId -> entity.articleId,
        column.content -> entity.content,
        column.username -> entity.username
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: ArticleComment)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(ArticleComment).where.eq(column.id, entity.id) }.update.apply()
  }

}
