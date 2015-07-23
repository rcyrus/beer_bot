name := "BeerBot"

version := "1.0"

scalaVersion := "2.11.0"

dependencyOverrides += "org.scala-lang" % "scala-compiler" % scalaVersion.value

dependencyOverrides += "org.scala-lang" % "scala-reflect" % scalaVersion.value

libraryDependencies += "net.ruippeixotog" %% "scala-scraper" % "0.1.1"

libraryDependencies += "com.h2database" % "h2" % "1.4.187"

libraryDependencies += "org.sorm-framework" % "sorm" % "0.3.18"