package code.snippet

import net.liftweb.http.{IdMemoizeTransform, SHtml}
import net.liftweb.util.Helpers._
import java.util.UUID

import net.liftweb.http.js.JsCmds._
import scala.xml.NodeSeq
import net.liftweb.util.CssSel


object ItemList {

  def render[Item](
      itemsDisplayed: => Seq[Item],
      renderItem:(Item) => CssSel,
      suggestions:(String)=>Seq[String],
      addItem:(String) => Any,
      removeItem:(Item) => Any
    ) = {
    var itemListXML:IdMemoizeTransform = null

    ".itemlist" #> SHtml.idMemoize{ cached => itemListXML = cached
      ".item" #> itemsDisplayed.map { item =>
        ".remove [onClick]" #> removeItemAndRefreshList[Item](item, itemListXML, removeItem) &
        renderItem(item)
      }
    } &
      ".additemform" #> renderAddItemField(itemListXML, suggestions, addItem)
  }

  def refreshList(itemListXML:IdMemoizeTransform) = itemListXML.setHtml()

  def renderAddItemField(itemListXML:IdMemoizeTransform, suggestions:(String)=>Seq[String], addItem:(String) => Any) = {
    val addItemFieldId = UUID.randomUUID.toString.replace('-','x')

    ".add_item [id]" #> addItemFieldId &
    ".add_item" #> SHtml.onSubmit{ title =>
        addItemAndRefreshList(title, addItemFieldId, itemListXML, addItem)
      } &
    TypeAhead.renderAutoCompleteScript(addItemFieldId, suggestions)
  }

  def addItemAndRefreshList(title: String, addItemFieldId:String, itemListXML:IdMemoizeTransform, addItem:(String) => Any) = {
    if (title.trim.nonEmpty) {
      addItem(title)

      SetValueAndFocus(addItemFieldId, "") &
      refreshList(itemListXML)
    }
  }

  def removeItemAndRefreshList[Item](item: Item, itemListXML:IdMemoizeTransform, removeItem:(Item) => Any) = {
    SHtml.ajaxInvoke { () =>
      removeItem(item)
      refreshList(itemListXML)
    }
  }
}
