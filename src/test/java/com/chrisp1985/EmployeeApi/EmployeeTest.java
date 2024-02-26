package com.chrisp1985.EmployeeApi;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import com.chrisp1985.EmployeeApi.data.EmployeeRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmployeeTest {

    @LocalServerPort
    private Integer port;

    static MySQLContainer<?> mysqlContainer = new MySQLContainer<>(
            "mysql:latest"
    );

    @BeforeAll
    static void beforeAll() {
        mysqlContainer.withDatabaseName("EmployeesQX")
                .withUsername("myusername")
                .withPassword("somerandompassword")
                .withInitScript("employees_init.sql")
                .start();
    }

    @AfterAll
    static void afterAll() {
        mysqlContainer.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
    }

    @Autowired
    EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Test
    void shouldGetAllEmployees() {

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/v1/employees")
                .then()
                .statusCode(200)
                .body(".", hasSize(14));
    }

    @Ignore("Doesn't work for some reason - to investigate")
    @Test
    void shouldGetDataForTurner() throws JSONException {
        String json = "{\n" +
                "  \"employeeId\": 7844,\n" +
                "  \"lastName\": \"TURNER\",\n" +
                "  \"job\": \"SALESMAN\",\n" +
                "  \"managerId\": 7698,\n" +
                "  \"hiredate\": \"1981-09-08\",\n" +
                "  \"salary\": 1500,\n" +
                "  \"commissionValue\": 0,\n" +
                "  \"deptNumber\": 30\n" +
                "}";
        JSONObject employeeObj = new JSONObject(json);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/v1/employees/7844")
                .then()
                .statusCode(200)
                .body(Matchers.contains(employeeObj));
    }
}