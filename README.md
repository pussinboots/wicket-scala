Template project for a wicket scala project
==================

This project contains all stuff to build a wicket project with the scala language.
The build tool sbt (http://www.scala-sbt.org/) is used with all defined dependencies
needed to develop a wicket project with scala. 

My preferred IDE is Idea so to support that the sbt plugin sbt-idea is integrated.
With the sbt command
    sbt gen-idea
the complete idea project files will be created and can be open as Scala Project in Idea.
The Scala Plugin needs to be installed in Idea.

Test Driven Development
==================

Continous Integration
Continous Deployment


Travis CI (https://travis-ci.org/pussinboots/wicket-scala)
and deploy it to heroku. (https://www.heroku.com)

* wicket framework 6.6.0
* scala 2.10.x
* sbt 0.12
* slick 1.0.1
