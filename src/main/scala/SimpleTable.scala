
object SimpleTable {
  def apply(t: String, a: List[(String, Any)]): SimpleTable = new SimpleTable(t, a)
}

class SimpleTable(t: String, a: List[(String, Any)]) {
  val title: String = t
  val args: List[(String, Any)] = a

  override def toString: String= {
    s"$title => ${args.mkString(",")}\n"
  }
}