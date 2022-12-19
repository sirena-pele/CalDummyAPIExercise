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
import utilities.ConfigLoader;

import java.util.Properties;

public class BaseTest {
    protected String baseURI;
    protected String basePath;
    protected RequestSpecification requestSpecification;
    protected ResponseSpecification responseSpecification;

    /**
     * Taking global variables from config.properties file
     * @baseURI
     * @basePath
     */
    @BeforeSuite
    public void config() {
        Properties props = ConfigLoader.getInstance().getProperties();
        baseURI = props.getProperty(IConfigKeys.BASE_URI);
        basePath = props.getProperty(IConfigKeys.BASE_PATH);
    }

    /**
     * Creating common
     * @requestSpecification
     * @responseSpecification
     */
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
