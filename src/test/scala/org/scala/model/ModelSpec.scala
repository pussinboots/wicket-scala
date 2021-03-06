package org.scala.model

import org.specs2.mutable.Specification
import scala.slick.session.Database
import Database.threadLocalSession
import java.util.Calendar
import java.sql.Timestamp
import scala.Some
import scala.slick.driver.H2Driver

class ModelSpec extends Specification {
  sequential

  val db = DB.getSlickHSQLDatabase()
  val dao = new DAL(H2Driver)
  import dao._
  import dao.profile.simple._
  db withSession {
    val now = new Timestamp(Calendar.getInstance().getTimeInMillis)
    dao.recreate
    dao.Studiengangs.insert(Studiengang(Some(1), "erster Eintrag", now))
    dao.Studiengangs.insert(Studiengang(Some(2), "zweiter Eintrag", now))
  }

  "Slick Models" should {
    "Studiengang object" should {
      "check that 2 studiengaenge are stored in the database" in {
        db withSession {
          (Studiengangs.length).run must beEqualTo(2)
        }
      }
      "retrieve one studiengaenge by fach from database" in {
        db withSession {
          val studienGang =  (for {a <- Studiengangs if a.name === "erster Eintrag"} yield (a)).first
          studienGang.name must beEqualTo("erster Eintrag")
        }
      }
      "update createDate field" in {
        db withSession {
          val now = new Timestamp(Calendar.getInstance().getTimeInMillis)
          (for {a <- Studiengangs if a.name === "zweiter Eintrag"} yield (a)).map(_.createDate).update(now)
          val studienGang1 = (for {a <- Studiengangs if a.name === "erster Eintrag"} yield (a)).first
	  val studienGang2 = (for {a <- Studiengangs if a.name === "zweiter Eintrag"} yield (a)).first
	  //studienGang1.createDate.getTime must beEqualTo(now.getTime)
          studienGang2.createDate.getTime must beEqualTo(now.getTime)
        }
      }
      "insert a new entry" in {
        db withSession {
	  val now = new Timestamp(Calendar.getInstance().getTimeInMillis)
          val studiengang = Studiengangs.insert(Studiengang(None, "fach2", now))
          studiengang.id mustNotEqual(None)
        }
      }
    }
  }
}
