FROM openjdk:11-slim-bullseye as build
WORKDIR /app
COPY . .
RUN apt update && apt install wget -y && wget https://mirrors.estointernet.in/apache/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz && tar -xvf apache-maven-3.6.3-bin.tar.gz && mv apache-maven-3.6.3 /opt/
ENV M2_HOME='/opt/apache-maven-3.6.3'
ENV PATH="$M2_HOME/bin:$PATH"
RUN mvn clean install


FROM public.ecr.aws/docker/library/tomcat:jdk11
COPY --from=build /app/encrypted-online-messenger.web/target/encrypted-online-messenger.web-1.0.war ./webapps/ROOT.war

EXPOSE 8080

ENTRYPOINT []

CMD ["catalina.sh", "run"]
