package controllers

import play.api.mvc._

object Static extends Controller {

  def imprint = Action {
    Ok(views.html.frontend.imprint())
  }
  
}
