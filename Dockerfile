FROM openjdk:17
ADD target/cpm.jar cpm.jar
ENTRYPOINT ["java","-jar","/cpm.jar"]