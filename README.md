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

To support Test Driven Development the sbt plugin sbt-scct from http://mtkopone.github.com/scct/maven-repo is
integrated. With that plugin the Code Coverage can be calculated with sbt.

To integrate sbt-scct in your project add the lines below to your project/plugin.sbt file
    
    resolvers += "scct-github-repository" at "http://mtkopone.github.com/scct/maven-repo"

    addSbtPlugin("reaktor" % "sbt-scct" % "0.2-SNAPSHOT")

To calculate the Code Coverage run 
    
    sbt clean scct:test
    
The clean command should remove old compiled source code so that scct:test compile and introspect all source
code fresh. Without the clean command it could be possible that the calculated Code Coverage is to low because
not all code lines were introspected from scct and also not observed during the running tests.

The Code Coverage report is written as html files to the following folder.

    target/scala-(xxx)/coverage-report/

In that folder there is a index.html open it and you will see your project related Code Coverage report.


Continous Integration
==================

After a while of reasearch for an open and free build server (hudson, jenkins, teamcity) somewhere in the internet
i found Travis CI (https://travis-ci.org). It is a relativ new star on the Continous Integration universe but
it support a wide range of programming languages and its realy easy to setup and configure (see http://about.travis-ci.org/docs/user/getting-started/).

This project is already configured for travis see https://travis-ci.org/pussinboots/wicket-scala. The configuration
is stored in the project itself in the .travis.yml file.

Continous Deployment
==================

Deploy to heroku from travis-ci see here http://about.travis-ci.org/docs/user/deployment/heroku/ its really simple.
and deploy it to heroku. This project is deployed under http://wicket-scala.herokuapp.com/. The magic here is every commit to the github repository (https://github.com/pussinboots/wicket-scala/) trigger a build in travis ci and after a succesful build the result will deployed to heroku. 

    deploy:
        provider: heroku
        app: wicket-scala
        strategy: git
        api_key: 
            secure: oyq......

Above is the snippet from .travis.yml file that configure the heroku deployment for travis ci build server. Here a little explanation.
* provider: heroku is clear or ?
* app: wicket-scala the name of the heroku git repo
* strategy: git travis has two strategies the normal git which means commit the changes of the current build to the heroku git repo this is the normal way
* api_key:  secure: oyq...... the encrypted heroku api token which can be get with the 
    
        heroku auth:token 

command inside a heroku project. With the following command you get the heroku auth token encrypted for use with travis

        travis encrypt $(heroku auth:token) --add deploy.api_key
        
        
Dependencies
=================

* wicket framework 6.6.0
* scala 2.10.x
* sbt 0.12
* slick 1.0.1
