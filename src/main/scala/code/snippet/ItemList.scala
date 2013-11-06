package code.snippet

import net.liftweb.http.{IdMemoizeTransform, RequestVar, SHtml}
import net.liftweb.http.js.JsCmds.{SetHtml, SetValueAndFocus}
import net.liftweb.util.Helpers._
import java.util.UUID

import net.liftweb.http.js.JsCmds._
import net.liftweb.common._


trait ItemList[Item] extends TypeAhead {

  def itemsDisplayed:Seq[Item]
  def addItem(title:String)
  def removeItem(item:Item)

  def itemLink(item:Item):String
  def itemTitle(item:Item):String

  
  def render = {
    var itemListXML:IdMemoizeTransform = null

    ".itemlist" #> SHtml.idMemoize{ cached => itemListXML = cached
      ".item" #> itemsDisplayed.map { item =>
        ".link [href]" #> itemLink(item) &
        ".title" #> itemTitle(item) &
        ".remove [onClick]" #> removeItemAndRefreshList(item, itemListXML)
      }
    } &
      ".additemform" #> renderAddItemField(itemListXML)
  }

  def refreshList(itemListXML:IdMemoizeTransform) = itemListXML.setHtml()

  def renderAddItemField(itemListXML:IdMemoizeTransform) = {
    var addItemFieldXML:IdMemoizeTransform = null

    ".add_item" #> SHtml.idMemoize{ cached => addItemFieldXML = cached
      addItemFieldXML
    } &
    ".add_item" #> SHtml.onSubmit{ title =>
        addItemAndRefreshList(title, addItemFieldXML, itemListXML)
      } // &
    //renderAutoCompleteScript(addItemFieldXML.latestId)
  }

  def addItemAndRefreshList(title: String, addItemFieldXML:IdMemoizeTransform, itemListXML:IdMemoizeTransform) = {
    if (title.trim.nonEmpty) {
      addItem(title)

      SetValueAndFocus(addItemFieldXML.latestId, "") &
      refreshList(itemListXML)
    }
  }

  def removeItemAndRefreshList(item: Item, itemListXML:IdMemoizeTransform) = {
    SHtml.ajaxInvoke { () =>
      removeItem(item)
      refreshList(itemListXML)
    }
  }
}
