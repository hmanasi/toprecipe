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
  "org.springframework" % "spring-instrument" % "3.2.4.RELEASE",
  "org.springframework" % "spring-test" % "3.2.4.RELEASE" % "test",
  "org.hibernate" % "hibernate-entitymanager" % "4.2.5.Final",
  "org.springframework.data" % "spring-data-jpa" % "1.4.1.RELEASE",
  "org.aspectj" % "aspectjweaver" % "1.7.2",
  "org.springframework" % "spring-aspects" % "3.2.4.RELEASE",
  "cglib" % "cglib" % "2.2.2",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.2.3",
  "org.jsoup" % "jsoup" % "1.7.2"
)

play.Project.playJavaSettings

outputStrategy := Some(StdoutOutput)