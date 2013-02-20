package controllers

import jp.t2v.lab.play20.auth.LoginLogout
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.Account

object Auth extends Controller with LoginLogout with AuthConfig {

  val loginForm = Form {
    mapping(
      "username" -> nonEmptyText,
      "password" -> text)(Account.authenticate)(_.map(u => (u.username, "")))
      .verifying("Invalid username or password", result => result.isDefined)
  }

  def index = Action { implicit request =>
    Ok(views.html.login(loginForm))
  }

  /**
   * Return the `gotoLogoutSucceeded` method's result in the logout action.
   *
   * Since the `gotoLogoutSucceeded` returns `Result`,
   * If you import `jp.t2v.lab.play20.auth._`, you can add a procedure like the following.
   *
   *   gotoLogoutSucceeded.flashing(
   *     "success" -> "You've been logged out"
   *   )
   */
  def logout = Action { implicit request =>
    // do something...
    gotoLogoutSucceeded
  }

  /**
   * Return the `gotoLoginSucceeded` method's result in the login action.
   *
   * Since the `gotoLoginSucceeded` returns `Result`,
   * If you import `jp.t2v.lab.play20.auth._`, you can add a procedure like the `gotoLogoutSucceeded`.
   */
  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.login(formWithErrors))
      }, 
      user => {
        gotoLoginSucceeded(user.get.id)
      }
    )
  }
}