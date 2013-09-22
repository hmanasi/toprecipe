name := "toprecipie"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "com.h2database" % "h2" % "1.3.168",  
  "org.springframework" % "spring-beans" % "3.2.4.RELEASE",
  "org.springframework" % "spring-context" % "3.2.4.RELEASE",
  "org.springframework" % "spring-core" % "3.2.4.RELEASE",
  "org.springframework" % "spring-orm" % "3.2.4.RELEASE",
  "org.springframework" % "spring-jdbc" % "3.2.4.RELEASE",  
  "org.springframework" % "spring-expression" % "3.2.4.RELEASE",
  "org.springframework" % "spring-tx" % "3.2.4.RELEASE",
  "org.springframework" % "spring-test" % "3.2.4.RELEASE" % "test",
  "org.hibernate" % "hibernate-entitymanager" % "4.2.5.Final",
  "cglib" % "cglib" % "2.2.2"
)

play.Project.playJavaSettings
