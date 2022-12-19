package dbApi;

import base.BaseTest;
import base.Retry;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pojo.Data;
import pojo.EmployeeListSuccessResponse;
import pojo.EmployeeSuccessResponse;
import utilities.JsonConvertor;
import utilities.Log;

import java.io.File;
import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class DbAPITest extends BaseTest {

    /**
     * Test fetching all employees' data from db and compare the count
     * of employees to expected result
     *
     * @param expectedEmployeesNumber from dbApiTest.xml file
     *                                If you want this test run successfully: remove  "**********" in last line
     * @numberOfEmployees + status + message
     */
    @Test(description = "Get all Employees Data")
    @Parameters("numberOfEmployees")
    public void getAllEmployeesData(String expectedEmployeesNumber) {
        String endPoint = "/employees";
        int numOfEmployees = Integer.parseInt(expectedEmployeesNumber);
        EmployeeListSuccessResponse response = given(requestSpecification)
                .when().get(endPoint)
                .then().spec(responseSpecification)
                .body("message", equalTo("Successfully! All records has been fetched."),
                        "status", equalTo("success"),
                        "data.size()", equalTo(numOfEmployees))
                .extract().body().as(EmployeeListSuccessResponse.class);

        //Another way to assert the results :
        Assert.assertEquals(response.getMessage(), "Successfully! All records has been fetched.");
        Assert.assertEquals(response.getData().size(), numOfEmployees);
        Assert.assertEquals(response.getStatus(), "success **********");//I did this mistake deliberately to show failed test in Report
    }


    /**
     * For given id test take data from employees.json file and compare every param to actual result from a response
     *
     * @param id from dbApiTest.xml file
     */
    @Test(description = "Get single employee by ID", retryAnalyzer = Retry.class)
    @Parameters("id")
    public void getEmployeeById(String id) throws IOException {
        //
        int intId = Integer.parseInt(id);
        String endPoint = "/employee";
        Data requestEmployee = new Data(intId);
        EmployeeSuccessResponse responseEmployee =
                given(requestSpecification)
                        .when().get(endPoint + "/" + id)
                        .then().spec(responseSpecification)
                        .body("message", equalTo("Successfully! Record has been fetched."),
                                "status", equalTo("success"))
                        .extract().body().as(EmployeeSuccessResponse.class);

        Assert.assertEquals(responseEmployee.getData().getId(), requestEmployee.getId());
        Assert.assertEquals(responseEmployee.getData().getEmployee_salary(), requestEmployee.getEmployee_salary());
        Assert.assertEquals(responseEmployee.getData().getEmployee_age(), requestEmployee.getEmployee_age());
        Assert.assertEquals(responseEmployee.getData().getEmployee_name(), requestEmployee.getEmployee_name());
        Assert.assertEquals(responseEmployee.getData().getProfile_image(), requestEmployee.getProfile_image());

    }

    /**
     * Test create new employee in db by given json file
     * and compare the actual results to expected
     */
    @Test(description = "Create new Employee")
    public void createNewEmployee() {
        JSONObject requestNewEmployee = JsonConvertor.getJSONObject("newEmployee.json");
        String endPoint = "/create";
        EmployeeSuccessResponse responseNewEmployee =
                given(requestSpecification).body(requestNewEmployee)
                        .when().post(endPoint)
                        .then().spec(responseSpecification)
                        .extract().response().as(EmployeeSuccessResponse.class);

        Assert.assertEquals(responseNewEmployee.getMessage(), "Successfully! Record has been added.");
        Assert.assertEquals(responseNewEmployee.getStatus(), "success");
        Log.info("NEW EMPLOYEE ID IS: " + responseNewEmployee.getData().getId());
    }

    /**
     * Test update existing employee in db and compare the actual results
     * to expected response status
     */
    @Test(description = "Update Employee data by given ID")
    @Parameters("id")
    public void updateEmployeeById(String id) {
        File file = new File("src/test/resources/updateEmployee.json");
        String endPoint = "/update";
        given(requestSpecification).body(file)
                .when().put(endPoint + "/" + id)
                .then().spec(responseSpecification)
                .body("status", equalTo("success"), "message", equalTo("Successfully! Record has been updated."));
    }

    /**
     * Test delete employee from db by given
     *
     * @param id
     */
    @Test(description = "Delete Employee by given ID", retryAnalyzer = Retry.class)
    @Parameters("id")
    public void deleteEmployeeById(String id) {
        String endPoint = "/delete";
        given(requestSpecification).pathParam("id", id)
                .when().delete(endPoint + "/{id}")
                .then().spec(responseSpecification)
                .body("status", equalTo("success"),
                        "message", equalTo("Successfully! Record has been deleted"))
        ;


        //Can also check if record is actually deleted from db, by calling to /employee/{id}
        //Also is always possible create negative scenarios with 1 or more missing params in body
    }


}

