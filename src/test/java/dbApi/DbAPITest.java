package dbApi;

import base.BaseTest;
import base.Retry;
import com.aventstack.extentreports.ExtentTest;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
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

    @Test(description = "Get all Employees Data")
    @Parameters("numberOfEmployees")
    public void getAllEmployeesData(String expectedEmployeesNumber) {
        // Test fetching all employees data from db and compare the count of employees to expected result numberOfEmployees + status + message
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
        Assert.assertEquals("Successfully! All records has been fetched.", response.getMessage());
        Assert.assertEquals(numOfEmployees, response.getData().size());

        Assert.assertEquals("success **********", response.getStatus());//I did this mistake deliberately to show failed test in Report
    }

    @Test(description = "Get single employee by ID", retryAnalyzer = Retry.class)
    @Parameters("id")
    public void getEmployeeById(String id) throws IOException {
        //for given id test take json data from employees.json file and compare it to actual result from a response
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

        Assert.assertEquals(requestEmployee.getId(), responseEmployee.getData().getId());
        Assert.assertEquals(requestEmployee.getEmployee_salary(), responseEmployee.getData().getEmployee_salary());
        Assert.assertEquals(requestEmployee.getEmployee_age(), responseEmployee.getData().getEmployee_age());
        Assert.assertEquals(requestEmployee.getEmployee_name(), responseEmployee.getData().getEmployee_name());
        Assert.assertEquals(requestEmployee.getProfile_image(), responseEmployee.getData().getProfile_image());

    }


    @Test(description = "Create new Employee")
    public void createNewEmployee() {
        //Test create new employee in db by given json file and compare the actual results to expected
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

    @Test(description = "Update Employee data by given ID")
    @Parameters("id")
    public void updateEmployeeById(String id) {
        //Test update existing employee in db and compare the actual results to expected
        File file = new File("src/test/resources/updateEmployee.json");
        String endPoint = "/update";
        given(requestSpecification).body(file)
                .when().put(endPoint + "/" + id)
                .then().spec(responseSpecification)
                .body("status", equalTo("success"), "message", equalTo("Successfully! Record has been updated."));
    }


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

