package org.scala.model

import com.mchange.v2.c3p0.ComboPooledDataSource
import scala.slick.session.Database
import scala.slick.driver.H2Driver

object DB {

  //here you can add other database slick drivers if you need a hsqldb for testing and aa mysql in production for example
  lazy val db = sys.props.get("Database").getOrElse("h2") match {
    case "h2" => DB.getSlickHSQLDatabase()
  }
  lazy val dal = sys.props.get("Database").getOrElse("h2") match {
    case "h2" => new DAL(H2Driver)
  }

  def getSlickHSQLDatabase(jdbcUrl: String = "jdbc:hsqldb:mem:test1") = {
    val ds = new ComboPooledDataSource
    ds.setDriverClass("org.hsqldb.jdbc.JDBCDriver")
    ds.setJdbcUrl(jdbcUrl + ";sql.enforce_size=false")
    Database.forDataSource(ds)
  }
}
