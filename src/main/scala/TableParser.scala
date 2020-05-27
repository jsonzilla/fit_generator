import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.Element

import scala.util.{Failure, Success, Try}

object TableParser {
  def buildSimpleTable(e: Element): Option[SimpleTable] = {
    val rows = e >> elementList("tr")
    rows match {
      case r if r.size > 2 =>
        val title = (r.head >> elementList("td") >> text).mkString("").trim().split("\\.").last
        val eHeader = r(1) >> elementList("td") >> text
        val eValueLine = r(2) >> elementList("td") >> text

        def parsedTableElements = defineType(eHeader zip eValueLine)

        Some(new SimpleTable(title, parsedTableElements))
      case _ => None
    }
  }

  private def defineType(e: (String, String)): (String, Any) = {
    val header = e._1
    val value = e._2
    val possibleDouble = toDouble(value)
    val possibleInt = toInt(value)
    (possibleDouble, possibleInt) match {
      case (Some(d), _) => (header, d)
      case (_, Some(i)) => (header, i)
      case _ => (header, value)
    }
  }

  private def defineType(xs: List[(String, String)]):List[(String, Any)] = {
    xs.map(defineType)
  }

  private def toInt(s: String): Option[Int] = {
    Try {
      Some(s.toInt)
    } match {
      case Success(v) => v
      case Failure(_) => None
    }
  }

  private def toDouble(s: String):Option[Double] = {
    Try {
      Some(s.toDouble)
    } match {
      case Success(v) => v
      case Failure(_) => None
    }
  }

}
