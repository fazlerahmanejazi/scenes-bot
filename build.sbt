
name := "scenes-music"

version := "0.1"

scalaVersion := "2.13.7"

resolvers += Resolver.JCenterRepository

libraryDependencies ++= Seq(
  "com.discord4j" % "discord4j-core" % "3.0.6",
  "net.dv8tion" % "JDA" % "4.2.0_247",
  "com.sedmelluq" % "lavaplayer" % "1.3.73",
  "com.typesafe" % "config" % "1.4.1"
)
