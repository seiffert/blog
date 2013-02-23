package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.BlogPost
import jp.t2v.lab.play20.auth.{Auth => AuthTrait}
import models.Administrator

object BlogPosts extends Controller with AuthTrait with AuthConfig {
  
  val blogPostForm = Form(
          mapping(
            "title" -> nonEmptyText,
            "body" -> text
          )((title, body) => BlogPost(title, body))
           ((post: BlogPost) => Some(post.title, post.body))
      )
  
  def index = Action {
    Ok(views.html.frontend.index(BlogPost.all()))
  }
  
  def add = authorizedAction(Administrator) { user => implicit request =>
    Ok(views.html.admin.add(blogPostForm))
  }
  
  def remove(id: Int) = authorizedAction(Administrator) { user => implicit request =>
    BlogPost.remove(id)
    Redirect(routes.Admin.index)
  }
  
  def saveNew = authorizedAction(Administrator) { user => implicit request =>
    blogPostForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.admin.add(formWithErrors))
      },
      value => {
        BlogPost.persist(value)
        Redirect(routes.Admin.index)
      }
    )
  }
}