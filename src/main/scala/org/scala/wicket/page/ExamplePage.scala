package org.scala.wicket.page

import org.scala.model._
import org.apache.wicket.MarkupContainer
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.request.mapper.parameter.PageParameters
import java.util._
import org.wicket.scala.WicketDSL._
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation

object ExamplePage {
  private final val serialVersionUID: Long = 1L
}
@AuthorizeInstantiation(value=Array("ADMIN"))
class ExamplePage(parameters: PageParameters) extends WebPage(parameters) {
  add("datatable".table(getColumns, new StudienGangModelProvider(createFilterContainer())))

  private def createFilterContainer() = {
    val form = "filterForm".form(this)
    import DynamicFilterContainer._
    val filter = new DynamicFilterContainer[DB.dal.Studiengangs.type, Studiengang]()
    filter.add("id", "id".textField(form), equalFilter("id", v=> v.toInt))
    filter.add("name", "name".textField(form), likeFilter("name"))
    filter
  }

  private def getColumns() = {
    val columns = new ArrayList[IColumn[Studiengang, String]]
    "Id".addColumn(columns)
    "Name".addColumn(columns)
    "Angelegt am".addColumn("createDate", columns)
    columns
  }
}
