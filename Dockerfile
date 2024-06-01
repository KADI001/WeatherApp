FROM maven:3.9.6-amazoncorretto-17 AS build

WORKDIR /app
COPY . .

RUN mvn clean package

FROM tomcat:jdk17 AS app

COPY --from=build /app/Front/target/*.war $CATALINA_HOME/webapps/ROOT.war

CMD ["catalina.sh", "run"]