FROM alpine:3.16.0

RUN apk add -U --no-cache ca-certificates tzdata bash dumb-init unzip openjdk11 openssl && \
        rm -rf /var/cache/apk/* && \
        cp /usr/share/zoneinfo/America/Sao_Paulo /etc/localtime &&\
        echo "America/Sao_Paulo" > /etc/timezone && \
        addgroup -S spring && adduser -S spring -G spring && \
        mkdir -p /app && \
        chown spring:spring /app && \
        apk -U upgrade

WORKDIR /app

COPY application.yml /app/application.yml

COPY entrypoint.sh /app/entrypoint.sh

ADD https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip awscliv2.zip

ADD https://certs.secureserver.net/repository/sf-class2-root.crt /usr/local/share/custom-certificates/sf-class2-root.crt

RUN openssl x509 -outform der -in /usr/local/share/custom-certificates/sf-class2-root.crt -out /usr/local/share/custom-certificates/sf-class2-root.der

RUN /usr/lib/jvm/java-11-openjdk/bin/keytool -import -alias cassandra -keystore /app/cassandra_truststore.jks -file /usr/local/share/custom-certificates/sf-class2-root.der -storepass amazon -noprompt

RUN update-ca-certificates

COPY target/PoCTEF-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8085

RUN ["chmod", "+x", "/app/entrypoint.sh"]

CMD [ "/app/entrypoint.sh"]
