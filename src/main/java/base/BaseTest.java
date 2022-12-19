package base;


import constant.IConfigKeys;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import utilities.ConfigLoader;

public class BaseTest {
    protected String baseURI;
    protected String basePath;
    protected RequestSpecification requestSpecification;
    protected ResponseSpecification responseSpecification;


    @BeforeTest
    public void config() {

    }

    @BeforeSuite
    public void beforeSuite() {
        baseURI = ConfigLoader.getInstance().getProperties().getProperty(IConfigKeys.BASE_URI);
        basePath = ConfigLoader.getInstance().getProperties().getProperty(IConfigKeys.BASE_PATH);
    }

    @BeforeClass()
    public void beforeClass() {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder().
                setBaseUri(baseURI)
                .setBasePath(basePath)
                .log(LogDetail.ALL);
        requestSpecification = requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.ALL);
        responseSpecification = responseSpecBuilder.build();
    }

}
