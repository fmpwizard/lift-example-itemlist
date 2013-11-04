package code.model

import net.liftweb.mapper._
import net.liftweb.common.{Full, Empty}
import net.liftweb.common.Failure
import net.liftweb.common.ParamFailure

class Item extends LongKeyedMapper[Item]
    with IdPK {

  def getSingleton = Item

  object title extends MappedString(this, 80) {
    override def validations = valMinLen(1, "Text empty.") _ :: super.validations
    override def displayName = "Title"
  }
}

object Item extends Item with LongKeyedMetaMapper[Item] {
  def suggestions(typed:String) = {
    val words = typed.trim.toLowerCase.split(" ").map(_.trim)
    Item.findAll.map(_.title.toString).filter{ title =>
      words.forall(title.toLowerCase contains _)
    }
  }
}
