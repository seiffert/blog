package controllers

import play.api.mvc._
import jp.t2v.lab.play20.auth.{Auth => AuthTrait}
import models._

object Admin extends Controller with AuthConfig with AuthTrait {

  def index = authorizedAction(Administrator) { user => implicit request =>
    Ok(views.html.admin.index(BlogPost.all()))
  }
  
}