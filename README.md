# cs6303-Final-Project

## instructions to run

You need to have at least jdk 11 and maven installed and on your path.

```bash
javac --version
```
```bash
mvn
```

you must update the following properties in the application.properties file:
```conf
spring.datasource.url
spring.datasource.username
spring.datasource.password
server.ssl.key-store
server.ssl.key-store-password
server.ssl.key-alias
spring.security.oauth2.client.registration.google.client-id
spring.security.oauth2.client.registration.google.client-secret
spring.security.oauth2.client.registration.github.client-id
spring.security.oauth2.client.registration.github.client-secret
initialDataLoader.adminUserName
initialDataLoader.adminUserPassword
initialDataLoader.adminUserEmail
```

You must also have a mysql database setup with a username and password that will have full access to the database you
are setting for the application in spring.datasource.url.


finally you have to have an environment variable called JASYPT_ENCRYPTOR_PASSWORD set.

see [jasypt-spring-boot-maven](https://github.com/ulisesbocchio/jasypt-spring-boot#maven-plugin) if you need more information on how to set
encrypted values as they currently appear in the application.properties file.


after your values are properly set in the application.properties file and you have the JASYPT_ENCRYPTOR_PASSWORD
environment variable set all you need to run is:

```bash
mvn spring-boot:run
```

this readme is also available on github at [https://github.com/leppanayr/cs6303-Final-Project/blob/master/README.md](https://github.com/leppanayr/cs6303-Final-Project/blob/master/README.md)