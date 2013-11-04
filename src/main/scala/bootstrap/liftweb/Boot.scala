package bootstrap.liftweb

import net.liftweb._
import util._
import Helpers._

import common._
import http._
import sitemap._
import Loc._
import net.liftmodules.JQueryModule
import net.liftweb.http.js.jquery._
import net.liftmodules.FoBo
import net.liftweb.db.{DefaultConnectionIdentifier, DB, StandardDBVendor}
import net.liftweb.mapper.Schemifier
import code.model.Item

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {
    // where to search snippet
    LiftRules.addToPackages("code")

    // Build SiteMap
    val entries = List(
      Menu.i("Home") / "index", // the simple way to declare a menu

      // more complex because this menu allows anything in the
      // /static path to be visible
      Menu(Loc("Static", Link(List("static"), true, "/static/index"), 
	       "Static Content")))

    // set the sitemap.  Note if you don't want access control for
    // each page, just comment this line out.
    LiftRules.setSiteMap(SiteMap(entries:_*))

    //Show the spinny image when an Ajax call starts
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)
    
    // Make the spinny image go away when it ends
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    // Use HTML5 for rendering
    LiftRules.htmlProperties.default.set((r: Req) =>
      new Html5Properties(r.userAgent))

    FoBo.InitParam.JQuery=FoBo.JQuery191
    FoBo.InitParam.ToolKit=FoBo.Bootstrap300
    FoBo.init()

    // Init Database
    val vendor = new StandardDBVendor(Props.get("db.driver") openOr "org.h2.Driver",
      Props.get("db.url") openOr "jdbc:h2:lift_example.db;AUTO_SERVER=TRUE",
      Props.get("db.user"), Props.get("db.password"))

    LiftRules.unloadHooks.append(vendor.closeAllConnections_! _)

    DB.defineConnectionManager(DefaultConnectionIdentifier, vendor)

    // Init model
    Schemifier.schemify(true, Schemifier.infoF _,
      Item)

    // Make a transaction span the whole HTTP request
    S.addAround(DB.buildLoanWrapper)
  }
}
