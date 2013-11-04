package code.snippet

import net.liftweb.util.Helpers._
import net.liftweb.mapper._

import net.liftweb._
import http._

import code.model.Item
import net.liftweb.http.js.JsCmds._

class AllItems extends ItemList[Item] {
  def itemsDisplayed = Item.findAll
  def addItem(title:String) = Item.create.title(title).save
  def removeItem(item:Item) = item.delete_!
  def itemLink(item: Item): String = "#"
  def itemTitle(item: Item): String = item.title
  def suggestions(typed: String): Seq[String] = Item.suggestions(typed)
}


