package models
  
import anorm._
import anorm.SqlParser._
import play.api.db._
import play.Logger
import play.api.Play.current
import java.util.Date

case class BlogPost(title: String, body: String, id: Int = 0, publishedDate: Date = null)

object BlogPost {
  
  val blogPost = {
    get[Int]("id") ~
    get[String]("title") ~
    get[String]("body") ~
    get[Date]("publish_date") map {
      case id~title~body~date => BlogPost(title, body, id, date)
    }
  }
  
  def all(): List[BlogPost] = DB.withConnection { implicit c =>
    SQL("select * from blog_post order by publish_date DESC").as(blogPost *)
  }
  
  def persist(post: BlogPost) {
      if(0 != post.id) {
          saveUpdate(post)
      } else {
          saveNew(post)
      }
  }
  
  def saveUpdate(post: BlogPost) {
      DB.withConnection { implicit c =>
          SQL("update blog_post set title = {title}, body = {body} where id = {id}").on(
              'title -> post.title,
              'body -> post.body,
              'id -> post.id
          ).executeUpdate()
      }
  }
  
  def saveNew(post: BlogPost) {
      DB.withConnection { implicit c =>
          SQL("insert into blog_post (title, body) values ({title}, {body})").on(
              'title -> post.title,
              'body -> post.body
          ).executeUpdate()
      }
  }
  
  def remove(id: Long) {
      DB.withConnection { implicit c => 
          SQL("DELETE from blog_post WHERE id = {id} LIMIT 1").on(
              'id -> id
          ).executeUpdate()
      }
  }
  
}