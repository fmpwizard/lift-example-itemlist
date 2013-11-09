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

object TypeAhead {
  implicit val formats = DefaultFormats

  def suggest(value: JValue, suggestions:(String) => Seq[String]) : JValue = {
    val matches = for {
      q <- value.extractOpt[String].toList
      sug <- suggestions(q)
    } yield JString(sug)

    JArray(matches)
  }

  def renderAutoCompleteScript( fieldId: String, suggestions:(String) => Seq[String]) = {
    val callbackContext = new JsonContext(Full("callback"),Empty)

    S.appendJs(Run(s"""
                     |$$('#$fieldId').typeahead({
                     |  source: askServer$fieldId,
                     |  matcher: function(item) {return true;}
                     |});
                   """.stripMargin))

    ".js *" #> Function("askServer"+fieldId, "query" :: "callback" :: Nil,
      Run(SHtml.jsonCall(JsVar("query"), callbackContext, (jvalue:JValue) => suggest(jvalue,suggestions) ).toJsCmd))
  }
}
