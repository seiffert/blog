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
  
  def index = optionalUserAction { maybeUser => request =>
    Ok(views.html.index(BlogPost.all(), maybeUser.getOrElse(null)))
  }
  
  def add = authorizedAction(Administrator) { user => implicit request =>
    Ok(views.html.add(blogPostForm, user))
  }
  
  def saveNew = authorizedAction(Administrator) { user => implicit request =>
    blogPostForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.add(formWithErrors))
      },
      value => {
        BlogPost.persist(value)
        Redirect(routes.BlogPosts.index)
      }
    )
  }
}