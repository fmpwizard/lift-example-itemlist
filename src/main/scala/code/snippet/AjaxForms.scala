package code.snippet

import net.liftweb.util.Helpers._

import net.liftweb._
import http._

import net.liftweb.http.js.JsCmds._

import collection.mutable

object AjaxForms {
  def render = {
    ".forms" #> List(1,2,3).map { i =>
      ".textfield [value]" #> i.toString &
      ".textfield" #> SHtml.onSubmit(Alert)
    }
  }
}
