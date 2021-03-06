package org.scala.wicket

import org.scala.model.DB
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.webapp.WebAppContext
import scala.util.Properties
import java.sql.Timestamp
import java.util.Calendar
import org.scala.model.Studiengang
import scala.slick.session.Database
import Database.threadLocalSession

object Start extends App {

  import DB.dal._
  import DB.dal.profile.simple._
  DB.db withSession DB.dal.create
  val now = new Timestamp(Calendar.getInstance().getTimeInMillis)
  def studiengang1 = new Studiengang(Some(1), "erster Eintrag", now)
  def studiengang2 = new Studiengang(Some(2), "zweiter Eintrag", now)
  DB.db withSession Studiengangs.insertAll(studiengang1, studiengang2)

  val webappDirLocation: String = "src/main/webapp/"
  val port: Int = Integer.valueOf(Properties.envOrElse("PORT", "8081"))
  val server: Server = new Server(port)
  val root: WebAppContext = new WebAppContext
  root.setContextPath("/")
  root.setDescriptor(webappDirLocation + "/WEB-INF/web.xml")
  root.setResourceBase(webappDirLocation)
  root.setParentLoaderPriority(true)
  server.setHandler(root)
  server.start
  server.join
}
