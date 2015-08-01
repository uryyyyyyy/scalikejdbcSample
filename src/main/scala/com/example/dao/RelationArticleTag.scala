package com.example.dao

import scalikejdbc._

case class RelationArticleTag(
  articleId: Int,
  tagName: String) {

  def save()(implicit session: DBSession = RelationArticleTag.autoSession): RelationArticleTag = RelationArticleTag.save(this)(session)

  def destroy()(implicit session: DBSession = RelationArticleTag.autoSession): Unit = RelationArticleTag.destroy(this)(session)

}


object RelationArticleTag extends SQLSyntaxSupport[RelationArticleTag] {

  override val tableName = "relation_article_tag"

  override val columns = Seq("article_id", "tag_name")

  def apply(rat: SyntaxProvider[RelationArticleTag])(rs: WrappedResultSet): RelationArticleTag = apply(rat.resultName)(rs)
  def apply(rat: ResultName[RelationArticleTag])(rs: WrappedResultSet): RelationArticleTag = new RelationArticleTag(
    articleId = rs.get(rat.articleId),
    tagName = rs.get(rat.tagName)
  )

  val rat = RelationArticleTag.syntax("rat")

  override val autoSession = AutoSession

  def find(articleId: Int, tagName: String)(implicit session: DBSession = autoSession): Option[RelationArticleTag] = {
    withSQL {
      select.from(RelationArticleTag as rat).where.eq(rat.articleId, articleId).and.eq(rat.tagName, tagName)
    }.map(RelationArticleTag(rat.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[RelationArticleTag] = {
    withSQL(select.from(RelationArticleTag as rat)).map(RelationArticleTag(rat.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(RelationArticleTag as rat)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[RelationArticleTag] = {
    withSQL {
      select.from(RelationArticleTag as rat).where.append(where)
    }.map(RelationArticleTag(rat.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[RelationArticleTag] = {
    withSQL {
      select.from(RelationArticleTag as rat).where.append(where)
    }.map(RelationArticleTag(rat.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(RelationArticleTag as rat).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    articleId: Int,
    tagName: String)(implicit session: DBSession = autoSession): RelationArticleTag = {
    withSQL {
      insert.into(RelationArticleTag).columns(
        column.articleId,
        column.tagName
      ).values(
        articleId,
        tagName
      )
    }.update.apply()

    RelationArticleTag(
      articleId = articleId,
      tagName = tagName)
  }

  def save(entity: RelationArticleTag)(implicit session: DBSession = autoSession): RelationArticleTag = {
    withSQL {
      update(RelationArticleTag).set(
        column.articleId -> entity.articleId,
        column.tagName -> entity.tagName
      ).where.eq(column.articleId, entity.articleId).and.eq(column.tagName, entity.tagName)
    }.update.apply()
    entity
  }

  def destroy(entity: RelationArticleTag)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(RelationArticleTag).where.eq(column.articleId, entity.articleId).and.eq(column.tagName, entity.tagName) }.update.apply()
  }

}
