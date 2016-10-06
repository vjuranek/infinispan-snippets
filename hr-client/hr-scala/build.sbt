name := "Hot Rod Client Scala"
version := "1.0"
scalaVersion := "2.11.8"

libraryDependencies += "org.infinispan" % "infinispan-remote" % "9.0.0.Alpha3"

resolvers ++= Seq("Local Maven" at Path.userHome.asFile.toURI.toURL + ".m2/repository",
                  "JBoss Releases" at "https://repository.jboss.org/nexus/content/repositories/releases/",
                  "JBoss Snapshots" at "https://repository.jboss.org/nexus/content/repositories/snapshots/")
