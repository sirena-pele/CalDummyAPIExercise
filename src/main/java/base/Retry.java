package base;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import utilities.Log;

public class Retry implements IRetryAnalyzer {
    int count = 0;
    private final int maxTry = 1;

    @Override
    public boolean retry(ITestResult iTestResult) {
        Log.info("RETRY COUNT: "+count);
        if (count<maxTry) {
            count++;
            return true;
        }
        return false;
    }
}
