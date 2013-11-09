package code.snippet

import net.liftweb.util.Helpers._

import net.liftweb._
import http._

import net.liftweb.http.js.JsCmds._

import collection.mutable

object AllItems {

  def renderItem(title:String) = {
    ".link [href]" #> "#" &
    ".title" #> title &
    SubItems.render
  }

  def render = {

    val list = mutable.ArrayBuffer.empty[String]

    def itemsDisplayed = list
    def addItem(title:String) = list += title
    def removeItem(title:String) = list -= title
    def suggestions(typed: String): Seq[String] = list.filter(_ contains typed)

    ItemList.render[String](
      itemsDisplayed,
      renderItem,
      suggestions,
      addItem,
      removeItem
    )
  }
}

object SubItems {

  def renderItem(title:String) = {
    ".link [href]" #> "#" &
      ".title" #> title
  }

  def render = {

    val list = mutable.ArrayBuffer.empty[String]

    def itemsDisplayed = list
    def addItem(title:String) = list += title
    def removeItem(title:String) = list -= title
    def suggestions(typed: String): Seq[String] = list.filter(_ contains typed)

    ItemList.render[String](
      itemsDisplayed,
      renderItem,
      suggestions,
      addItem,
      removeItem
    )
  }
}

