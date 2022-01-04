FROM openjdk:11
MAINTAINER horatomo
COPY target/igrow-0.0.1-SNAPSHOT.jar igrow-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/igrow-0.0.1-SNAPSHOT.jar"]