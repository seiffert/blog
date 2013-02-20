package models
  
import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class BlogPost(title: String, body: String) {
    var id: Int = 0
}

object BlogPost {
  
  val blogPost = {
    get[Int]("id") ~
    get[String]("title") ~
    get[String]("body") map {
      case id~title~body => BlogPost(title, body)
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
  
  def delete(id: Long) {}
  
}