= Books-highlights

image:https://img.shields.io/badge/vert.x-4.3.7-purple.svg[link="https://vertx.io"]


== Building

To launch your tests:
```
./mvnw clean test
```

To package your application:
```
./mvnw clean package -DskipTests
```

To run your application:
```
./mvnw clean compile exec:java
```

To build docker:
```
docker build -t playbooks .
```

To run docker image:
```
docker run --env-file .env -v $(pwd)/booknotes_full.sqlite:/app/booknotes_full.sqlite -p 8088:8088 playbooks
```

To build angular:
```bash
cd web/playbooks
npm install
ng build --prod
# or
npx ng build --prod
```

== Help

* https://vertx.io/docs/[Vert.x Documentation]
* https://stackoverflow.com/questions/tagged/vert.x?sort=newest&pageSize=15[Vert.x Stack Overflow]
* https://groups.google.com/forum/?fromgroups#!forum/vertx[Vert.x User Group]
* https://gitter.im/eclipse-vertx/vertx-users[Vert.x Gitter]


