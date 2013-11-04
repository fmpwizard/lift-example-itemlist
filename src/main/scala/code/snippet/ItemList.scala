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

  var itemListXML:Box[IdMemoizeTransform] = Empty
  var addItemFieldXML:Box[IdMemoizeTransform] = Empty
  
  
  def renderList = SHtml.idMemoize{ cachedXML => itemListXML = Full(cachedXML)
    ".item" #> itemsDisplayed.map { item =>
        ".link [href]" #> itemLink(item) &
        ".title" #> itemTitle(item) &
        ".remove [onClick]" #> removeItemAndRefreshList(item)
      }
  }
  def refreshList() = itemListXML.get.setHtml()

  def renderAddItemField = {
    ".add_item" #> SHtml.idMemoize{ cachedXML => addItemFieldXML = Full(cachedXML)
      cachedXML
    } &
      ".add_item" #> SHtml.onSubmit{ value =>
      addItemAndRefreshList(value)
    }
  }

  def addItemAndRefreshList(title: String) = {
    if (title.trim.nonEmpty) {
      addItem(title)

      SetValueAndFocus(addItemFieldXML.get.latestId, "") &
        refreshList()
    }
  }

  def removeItemAndRefreshList(item: Item) = {
    SHtml.ajaxInvoke { () =>
      removeItem(item)
      refreshList()
    }
  }

  def render = {
    ".itemlist" #> renderList &
    ".additemform" #> renderAddItemField &
    ".additemform" #> renderAutoCompleteScript(addItemFieldXML.get.latestId)
  }
}
