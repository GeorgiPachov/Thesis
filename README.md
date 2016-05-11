# Master's thesis - automated opinion gathering

A system for automated opinion gathering and sentiment analysis.

Use 
```
git clone https://github.com/GeorgiPachov/Thesis.git
cd Thesis
mvn install:install-file -Dfile=local-libs/jaws-bin.jar -DpomFile=pom.xml
mvn clean install tomcat7:run-war
```

You would also need the WordNet[1] library:  
https://wordnet.princeton.edu/

