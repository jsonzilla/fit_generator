import net.ruippeixotog.scalascraper.browser.HtmlUnitBrowser.HtmlUnitDocument
import net.ruippeixotog.scalascraper.browser.HtmlUnitBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import scala.io.Source
import java.io.File
import java.io.PrintWriter

object TestsSourceGenerator extends App {
  if (args.length == 0) {
    println("dude, i need at least one parameter => the COMPLETE PATH of HTML")
  }
  else {
    val filename = args(0)

    val typedBrowser: HtmlUnitBrowser = HtmlUnitBrowser.typed()
    val typedDoc: HtmlUnitDocument = typedBrowser.parseFile(filename)

    val tables = (typedDoc >> elementList("table")).map(TableParser.buildSimpleTable)
    tables.flatten.foreach {
      table =>
        val gen = FixtureGenerator(table)
        val writeHeader = new PrintWriter(new File(gen.fileNameHeader))
        writeHeader.write(gen.createHeader())
        writeHeader.close()

        val writeSource = new PrintWriter(new File(gen.fileNameSource))
        writeSource.write(gen.createSource())
        writeSource.close()
    }
  }
}

