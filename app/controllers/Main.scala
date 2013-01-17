package controllers

import play.api._
import play.api.mvc._
import play.api.http._
import play.api.data._
import play.api.data.Forms._

object Main extends Controller {

  private lazy val init = for {
    user ← Processor("whoami").right
    host ← Processor("hostname").right
    path ← Processor("pwd").right
  } yield views.html.home(user, host, path)

  val home = Action { req ⇒
    renderEither(init)
  }

  val command = Action { implicit req ⇒
    Form(single("command" -> text(minLength = 1))).bindFromRequest.fold(
      err ⇒ BadRequest("No command found"),
      command ⇒ renderEither(Processor(command))
    )
  }

  private def renderEither[A: Writeable: ContentTypeOf](either: Either[String, A]) = either.fold(
    err ⇒ BadRequest(err),
    res ⇒ Ok(res)
  )

  private object Processor {

    import scala.sys.process._
    import java.io.IOException

    def apply(command: String): Either[String, String] = {
      try {
        val result = Process(command).!!
        Right(result.trim)
      }
      catch {
        case ex: IOException      ⇒ Left(ex.getMessage)
        case ex: RuntimeException ⇒ Left(ex.getMessage)
      }
    }
  }
}
