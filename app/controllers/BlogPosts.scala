package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.{BlogPost, Administrator, Comment}
import jp.t2v.lab.play20.auth.{Auth => AuthTrait}
import views.ReCaptcha

object BlogPosts extends Controller with AuthTrait with AuthConfig {
  
  val blogPostForm = Form(
    mapping(
      "title" -> nonEmptyText,
      "body" -> text
    ) ((title, body) => BlogPost(title, body))
      ((post: BlogPost) => Some(post.title, post.body))
    )

  val commentForm = Form(
    mapping(
      "blogPostId" -> number,
      "name" -> nonEmptyText,
      "email" -> text,
      "commentBody" -> nonEmptyText
    ) ((blogPostId, name, email, commentBody) => Comment(blogPostId, name, email, commentBody))
      ((comment: Comment) => Some(comment.blogPostId, comment.author, comment.authorEmail, comment.body))
  )
  
  val captchaForm = Form[(String, String)](
    tuple(
      "recaptcha_challenge_field" -> nonEmptyText,
      "recaptcha_response_field" -> nonEmptyText
    )
  )
  
  def index = Action {
    Ok(views.html.frontend.index(BlogPost.all()))
  }
  
  def details(id: Int) = Action {
    Ok(views.html.frontend.details(BlogPost.findOneById(id), Comment.allOfPost(id), commentForm))
  }
  
    def addComment(id: Int) = Action { implicit request => {
      captchaForm.bindFromRequest.fold(
        failure => BadRequest(views.html.frontend.details(BlogPost.findOneById(id), Comment.allOfPost(id), commentForm)),
        {
          case (q, a) => {
            if (ReCaptcha.check("localhost", q, a)) {
              commentForm.bindFromRequest.fold(
                formWithErrors => BadRequest(views.html.frontend.details(BlogPost.findOneById(id), Comment.allOfPost(id), formWithErrors)),
                value => {
                  Comment.saveNew(value)
                  Redirect(routes.BlogPosts.details(id))
                }
              )
            } else {
              BadRequest(views.html.frontend.details(BlogPost.findOneById(id), Comment.allOfPost(id), commentForm))
            }
          }
        }
      )
    }
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