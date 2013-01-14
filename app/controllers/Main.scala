package controllers

import play.api._
import play.api.mvc._
import play.api.http._

object Main extends Controller {

  val home = Action {
    Ok(views.html.home())
  }
}

