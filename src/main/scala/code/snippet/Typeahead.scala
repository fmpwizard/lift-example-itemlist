package code.snippet

import net.liftweb.util.Helpers._

import net.liftweb._
import http._

import net.liftweb.common._
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.js.JsCmds.Run
import net.liftweb.http.js.JE.JsVar
import net.liftweb.json.JsonAST._
import net.liftweb.json.DefaultFormats

trait TypeAhead {
  def suggestions(typed:String):Seq[String]

  implicit val formats = DefaultFormats

  val callbackContext = new JsonContext(Full("callback"),Empty)

  def suggest(value: JValue) : JValue = {
    val matches = for {
      q <- value.extractOpt[String].toList
      sug <- suggestions(q)
    } yield JString(sug)

    JArray(matches)
  }

  val runSuggestion =
    SHtml.jsonCall(JsVar("query"), callbackContext, suggest _ )

  def renderAutoCompleteScript( fieldId: => String) = {
    S.appendJs(Run(s"""
                     |$$('#$fieldId').typeahead({
                     |  source: askServer$fieldId,
                     |  matcher: function(item) {return true;}
                     |});
                   """.stripMargin))

    ".js *" #> Function("askServer"+fieldId, "query" :: "callback" :: Nil,
      Run(runSuggestion.toJsCmd))
  }
}
