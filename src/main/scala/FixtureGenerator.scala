
case class Arg(originalName: String, varName: String, argType: ArgType)
case class ArgType(typeName: String, default: String, customized: String = "")

object FixtureGenerator {
  def apply(t: SimpleTable): FixtureGenerator = new FixtureGenerator(t)
}

class FixtureGenerator(t: SimpleTable) {
  lazy private val table: SimpleTable = t
  lazy private val title = fix(table.title)
  lazy private val args = table.args.map(e => Arg(e._1, fix(e._1), getClass(e._2)))
  lazy private val argsPart = args.partition(_.originalName.contains("()"))

  lazy private val className = s"Fit$title"

  val fileNameHeader = s"$className.h"
  val fileNameSource = s"$className.cpp"

  private val separator = "\n//----------------------------------------------------------------------------------------------\n"

  private def fix(s: String): String = {
    s.replaceAll(s"""(?:(?![a-zA-Z0-9_]).)+""", "")
  }

  private def getClass(a: Any): ArgType = a match {
    case _: Double => ArgType("double", "0.0", "DoubleWithTolerance")
    case _: Int => ArgType("int", "0")
    case _ => ArgType("CEEFIT::STRING", "\"\"")
  }

  def createHeader(): String = {
    def res(a: Arg): String =
      if (a.argType.customized.nonEmpty)
        s"    ${a.argType.customized} ceefit_call_spec ${a.varName}();\n"
      else
        s"    ${a.argType.typeName} ceefit_call_spec ${a.varName}();\n"

    def arg(a: Arg): String =
        s"    ${a.argType.typeName} ${a.varName} = ${a.argType.default};\n"

    val includeCustomDouble = argsPart._1.map(_.argType.customized.nonEmpty).nonEmpty
    val customHeader = if (includeCustomDouble) """#include "ceefit\DoubleWithTolerance.h"""" else ""
    val header = s"""#ifndef ${className.toUpperCase}_H
      |#define ${className.toUpperCase}_H
      |
      |#include "ceefit.h"
      |$customHeader
      |
      |#include "$fileNameHeader"
      |$separator
      |class $className: public CEEFIT::COLUMNFIXTURE
      |{
      |  public:
      |    $className();
      |    virtual ~$className() = default;
      |
      |""".stripMargin
    header + argsPart._1.map(res).mkString("\n") + "\n  private:\n" + argsPart._2.map(arg).mkString("") + "};\n" + separator + "\n#endif"
  }

  def createSource(): String = {
    def registerArg(a: Arg): String = s"""  RegisterCeefitField(this, "${a.originalName}", ${a.varName});\n"""
    def registerRes(a: Arg): String =
      s"""  RegisterCeefitTest(this, "${a.originalName.replace("()", "")}", &$className::${a.varName});"""

    def methodRes(a: Arg): String =
      if (a.argType.customized.nonEmpty)
        s"${a.argType.customized} ceefit_call_spec $className::${a.varName}()\n{\nreturn 0.0;\n}\n"
      else
        s"${a.argType.typeName} ceefit_call_spec $className::${a.varName}()\n{\n  return ${a.argType.default};\n}\n"

    val source = s"""#include "$fileNameHeader"
                   |$separator
                   |$className::$className()
                   |{
                   |""".stripMargin
    source + argsPart._2.map(registerArg).mkString("") + "\n" + argsPart._1.map(registerRes).mkString("") + "\n}\n" +
      argsPart._1.map(separator + "\n" + methodRes(_)).mkString("") + separator
  }
}
