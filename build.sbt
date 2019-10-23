name := "FitGenerator"

version := "1.1"

scalaVersion := "2.12.8"

mainClass in assembly := Some("TestsSourceGenerator")
assemblyJarName in assembly := "fitGenerator.jar"

libraryDependencies += "net.ruippeixotog" %% "scala-scraper" % "2.1.0"
