package code.snippet

import net.liftweb.http.{IdMemoizeTransform, SHtml}
import net.liftweb.util.Helpers._
import java.util.UUID

import net.liftweb.http.js.JsCmds._


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
    val addItemFieldId = UUID.randomUUID.toString.replace('-','x')

    ".add_item [id]" #> addItemFieldId &
    ".add_item" #> SHtml.onSubmit{ title =>
        addItemAndRefreshList(title, addItemFieldId, itemListXML)
      } &
    renderAutoCompleteScript(addItemFieldId)
  }

  def addItemAndRefreshList(title: String, addItemFieldId:String, itemListXML:IdMemoizeTransform) = {
    if (title.trim.nonEmpty) {
      addItem(title)

      SetValueAndFocus(addItemFieldId, "") &
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
