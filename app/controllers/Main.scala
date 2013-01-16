package controllers

import play.api._
import play.api.mvc._
import play.api.http._
import play.api.data._
import play.api.data.Forms._

object Main extends Controller {

  val home = Action { req ⇒
    renderEither(for {
      user ← Processor("whoami").right
      host ← Processor("hostname").right
      path ← Processor("pwd").right
    } yield views.html.home(user, host, path))
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
      var output = collection.mutable.ListBuffer[String]()
      var logger = ProcessLogger(x ⇒ output +: x)
      try {
        val result = Process(command).!!(logger)
        println(result, output)
        Right(result.trim)
      }
      catch {
        case ex: Exception ⇒ {
          println(output)
          Left(ex.getMessage + "\n" + output.mkString("\n"))
        }
        case ex: IOException      ⇒ Left(ex.getMessage + "\n" + output.mkString("\n"))
        case ex: RuntimeException ⇒ Left(ex.getMessage + "\n" + output.mkString("\n"))
      }
    }
  }
}
