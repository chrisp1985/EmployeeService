package com.chrisp1985.EmployeeApi;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;

import com.chrisp1985.EmployeeApi.data.Employee;
import com.chrisp1985.EmployeeApi.data.EmployeeRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.sql.Date;

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

    private String getTurnerJsonString() {
        return "{\n" +
                "  \"employeeId\": 7844,\n" +
                "  \"lastName\": \"TURNER\",\n" +
                "  \"job\": \"SALESMAN\",\n" +
                "  \"managerId\": 7698,\n" +
                "  \"hiredate\": \"1981-09-08\",\n" +
                "  \"salary\": 1500,\n" +
                "  \"commissionValue\": 0,\n" +
                "  \"deptNumber\": 30\n" +
                "}";
    }

    @Test
    void shouldGetDataForTurner() throws IOException {
        Employee expectedEmployee = new ObjectMapper().readValue(getTurnerJsonString(), Employee.class);

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/v1/employees/7844");

        Employee responseEmployee = response.as(Employee.class);

        Assertions.assertEquals(responseEmployee.employeeId, expectedEmployee.employeeId);
        Assertions.assertEquals(responseEmployee.lastName, expectedEmployee.lastName);
        Assertions.assertEquals(responseEmployee.job, expectedEmployee.job);
        Assertions.assertEquals(responseEmployee.managerId, expectedEmployee.managerId);
        Assertions.assertEquals(responseEmployee.hiredate, expectedEmployee.hiredate);
        Assertions.assertEquals(responseEmployee.salary, expectedEmployee.salary);
        Assertions.assertEquals(responseEmployee.commissionValue, expectedEmployee.commissionValue);
        Assertions.assertEquals(responseEmployee.deptNumber, expectedEmployee.deptNumber);

    }

    @Test
    void shouldGetSalaryForTurner() throws IOException {
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/v1/employees/7844/salary");

        String body = response.body().as(String.class);

        Assertions.assertEquals(body, new ObjectMapper().readValue(getTurnerJsonString(), Employee.class).salary.toString());
    }

    @Test
    void shouldReturnNoValueForInvalidEmployeeSalary() {
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/v1/employees/17844/salary");

        Assertions.assertEquals(response.statusCode(), 404);
    }

    @Test
    void shouldSaveEntityToDatabase() {
        Employee testEmployee = Employee.builder()
                .lastName("")
                .job("WASHER")
                .managerId(7844L)
                .hiredate(Date.valueOf("1981-09-08"))
                .salary(32000L)
                .commissionValue(100L)
                .deptNumber(30L)
                .build();

        int previousSize = employeeRepository.findAll().size();

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .body(testEmployee)
                .post("/v1/employees");

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(employeeRepository.findAll().size(), previousSize + 1);
    }
}