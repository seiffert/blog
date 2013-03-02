package models
  
import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import java.util.Date

case class Comment(blogPostId: Int, author: String, authorEmail: String, body: String, id: Int = 0, date: Date = null)

object Comment {
  
  val comment = {
    get[Int]("id") ~
    get[String]("author") ~
    get[String]("authorEmail") ~
    get[String]("body") ~
    get[Int]("blogPostId") ~
    get[Date]("date") map {
      case id~author~authorEmail~body~blogPostId~date => Comment(blogPostId, author, authorEmail, body, id, date)
    }
  }
  
  def allOfPost(blogPostId: Int): List[Comment] = DB.withConnection { implicit c =>
    SQL("select * from comment where blogPostId = {blogPostId} order by date DESC").on(
      'blogPostId -> blogPostId
    ).as(comment *)
  }
  
  def saveNew(comment: Comment) {
    DB.withConnection { implicit c =>
      SQL("insert into comment (blogPostId, author, authorEmail, body) values ({blogPostId}, {author}, {authorEmail}, {body})").on(
        'blogPostId -> comment.blogPostId,
        'author -> comment.author,
        'authorEmail -> comment.authorEmail,
        'body -> comment.body
      ).executeUpdate()
    }
  }
  
}