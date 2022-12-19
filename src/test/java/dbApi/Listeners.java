package dbApi;

import base.ExtentReporterNG;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utilities.Log;

public class Listeners implements ITestListener {
    ExtentReports extent = ExtentReporterNG.getReportObject();
    ExtentTest test;
    ThreadLocal<ExtentTest> extentTest = new ThreadLocal();//

    public void onTestStart(ITestResult result) {
        Log.startTestCase(result.getMethod().getMethodName());
        test = extent.createTest(result.getMethod().getMethodName());
        extentTest.set(test);
    }

    public void onTestSuccess(ITestResult result) {
        Log.info("Test Pass " + result.getMethod().getMethodName());
        extentTest.get().pass("Test Pass: " + result.getMethod().getMethodName());
    }

    public void onTestFailure(ITestResult result) {
        Log.error("Test Fail " + result.getMethod().getMethodName());
        extentTest.get().fail(result.getThrowable());
        //Screenshot, Attach to report
    }

    public void onFinish(ITestContext context) {
        extent.flush();
    }

}
