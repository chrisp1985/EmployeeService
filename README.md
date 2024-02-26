# Employee API Service

### Background
This is a repo to exercise some Spring framework stuff. 

### Useful Stuff

#### Rest Controller
- The controller pulls data from an RDS MySQL DB, the setup scripts can be found in the resources section. 
- There is also Spring Rest Data enabled, which means all the endpoints using the entity names are active. 
- The Employee entity being available means that the endpoint at /employees is automatically created, with all the HATEOAS stuff that goes with it.

#### Repositories
- The extension of the JpaRepository allows for all the DAO stuff to be automatically available, so using repository.save(), 
repository.findAll() etc for each of the CRUD functions.

#### Caching
- @EnableCaching on the Main application sets the capability, then @Cacheable on a method means caching is available for
that method and @CacheEvict means it'll be cleared when the method is triggered.

#### Actuator
- /health and /info metrics are available through the /actuator endpoint.

#### AOP
- Aspects are set up to trigger when cache is evicted. 
- The pattern can be copied for other annotations, or set via 'execution(* *.methodName(..))' to allow for any params to be passed to a method of method name in any package and with any return type.


---
Spring Boot Starter readme contents below...

---

# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.2.3/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.2.3/gradle-plugin/reference/html/#build-image)
* [Spring Boot Testcontainers support](https://docs.spring.io/spring-boot/docs/3.2.3/reference/html/features.html#features.testing.testcontainers)
* [Testcontainers](https://java.testcontainers.org/)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.2.3/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.2.3/reference/htmlsingle/index.html#web)
* [Rest Repositories](https://docs.spring.io/spring-boot/docs/3.2.3/reference/htmlsingle/index.html#howto.data-access.exposing-spring-data-repositories-as-rest)

### Guides
The following guides illustrate how to use some features concretely:

* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing JPA Data with REST](https://spring.io/guides/gs/accessing-data-rest/)
* [Accessing Neo4j Data with REST](https://spring.io/guides/gs/accessing-neo4j-data-rest/)
* [Accessing MongoDB Data with REST](https://spring.io/guides/gs/accessing-mongodb-data-rest/)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

### Testcontainers support

This project uses [Testcontainers at development time](https://docs.spring.io/spring-boot/docs/3.2.3/reference/html/features.html#features.testing.testcontainers.at-development-time).

Testcontainers has been configured to use the following Docker images:


Please review the tags of the used images and set them to the same as you're running in production.

