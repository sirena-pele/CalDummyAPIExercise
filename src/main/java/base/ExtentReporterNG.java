package base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReporterNG {

    public static ExtentReports getReportObject() {
        ExtentReports extent;
        ExtentSparkReporter reporter;
        String path = System.getProperty("user.dir") + "\\reports\\index.html";
        reporter = new ExtentSparkReporter(path);
        reporter.config().setReportName("API Automation Results");
        reporter.config().setDocumentTitle("Test Results");
        extent = new ExtentReports();
        extent.attachReporter(reporter);
        extent.setSystemInfo("Author", "Feldman Irena");
        extent.createTest(path);
        return extent;
    }

}
