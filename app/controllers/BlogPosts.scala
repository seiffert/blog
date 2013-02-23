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
          )(BlogPost.apply)(BlogPost.unapply)
      )
  
  def index = Action {
    Ok(views.html.frontend.index(BlogPost.all()))
  }
  
  def add = authorizedAction(Administrator) { user => implicit request =>
    Ok(views.html.admin.add(blogPostForm))
  }
  
  def saveNew = authorizedAction(Administrator) { user => implicit request =>
    blogPostForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.admin.add(formWithErrors))
      },
      value => {
        BlogPost.persist(value)
        Redirect(routes.BlogPosts.index)
      }
    )
  }
}