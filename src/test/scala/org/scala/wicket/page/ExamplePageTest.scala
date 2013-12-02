package org.scala.wicket.page

import org.specs2.mutable._
import org.apache.wicket.util.tester.WicketTester
import org.scala.test.SlickDbBefore
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable
import org.scala.wicket.WicketApplication
import org.scala.model._
import scala.slick.session.Database
import Database.threadLocalSession
import scala.Some
import org.specs2.specification.Outside
import scala.slick.driver.H2Driver
import java.sql.Timestamp
import java.util.Calendar

class ExamplePageTest extends Specification with SlickDbBefore {

  sys.props.+=("Database" -> "h2")

  implicit val wicketTester = new Outside[WicketTester] {
    def outside: WicketTester = {
      val wt = new WicketTester(new WicketApplication())
      wt.startPage(classOf[ExamplePage])
      wt
    }
  }

  override def initTestData(db: Database) {
    db withSession {
      val dao = new DAL(H2Driver)
      import dao._
      import dao.profile.simple._
      dao.recreate
      val now = new Timestamp(Calendar.getInstance().getTimeInMillis)
      def studiengang1 = new Studiengang(Some(1), "erster Eintrag", now)
      def studiengang2 = new Studiengang(Some(2), "zweiter Eintrag", now)
      Studiengangs.insertAll(studiengang1, studiengang2)
    }
  }

  def login(wt: WicketTester) {
      wt.assertRenderedPage(classOf[MySignInPage])
      wt.newFormTester("signInPanel:signInForm")
        .setValue("username", "wicket")
        .setValue("password", "wicket")
        .submit()
      wt.assertRenderedPage(classOf[ExamplePage])
  }

  sequential
  "ExamplePage" should {
    "with no filter" in {
      wt: WicketTester =>
        login(wt)
        wt.assertComponent("datatable", classOf[DataTable[Studiengang, String]])
        def table = wt.getComponentFromLastRenderedPage("datatable").asInstanceOf[DataTable[Studiengang, String]]
        table.getRowCount() must beEqualTo(2)
    }
    "with name filter" in {wt: WicketTester =>
	login(wt)
        wt.newFormTester("filterForm").setValue("name", "erster Eintrag")
        wt.executeAjaxEvent("filterForm:name", "onchange");
        wt.assertComponent("datatable", classOf[DataTable[Studiengang, String]])
        def table = wt.getComponentFromLastRenderedPage("datatable").asInstanceOf[DataTable[Studiengang, String]]
        table.getRowCount() must beEqualTo(1)
        val data = table.getDataProvider().iterator(table.getItemsPerPage() * table.getCurrentPage(), table.getItemsPerPage())
        data.next.name must beEqualTo("erster Eintrag")
    }
    "with id filter" in {wt: WicketTester =>
	login(wt)
        wt.newFormTester("filterForm").setValue("id", "2")
        wt.executeAjaxEvent("filterForm:id", "onchange");
        wt.assertComponent("datatable", classOf[DataTable[Studiengang, String]])
        def table = wt.getComponentFromLastRenderedPage("datatable").asInstanceOf[DataTable[Studiengang, String]]
        table.getRowCount() must beEqualTo(1)
        val data = table.getDataProvider().iterator(table.getItemsPerPage() * table.getCurrentPage(), table.getItemsPerPage())
        data.next.name must beEqualTo("zweiter Eintrag")
    }
  }
}
