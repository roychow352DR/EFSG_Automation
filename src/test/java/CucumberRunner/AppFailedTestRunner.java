package CucumberRunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;


@CucumberOptions(features = "@target/app_failed_scenarios.txt", glue = "StepDefinitions", monochrome = true
        , plugin = {"pretty",
        /*"html:target/cucumber-reports-failed.html",*/
        "json:target/cucumber-reports/cucumber-reports-failed.json","rerun:target/app_failed_scenarios.txt"})

public class AppFailedTestRunner extends AbstractTestNGCucumberTests {
}
