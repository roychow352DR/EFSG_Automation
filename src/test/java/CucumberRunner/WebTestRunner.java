package CucumberRunner;


import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.FeatureWrapper;
import io.cucumber.testng.PickleWrapper;
import org.junit.runner.RunWith;

//@CucumberOptions(features = "src/test/java/Features", glue = "StepDefinitions", monochrome = true
//        , plugin = {"pretty",
//        /*"html:target/cucumber-reports/cucumber-report.html",*/
//        "json:target/cucumber-reports/cucumber-report.json","rerun:target/web_failed_scenarios.txt"},tags = "@Test")
@CucumberOptions(features = {"src/test/java/Features/AdminPortal/aoApplication/AP-100.feature",
        "src/test/java/Features/AdminPortal/aoApplication/AP-353.feature",
        "src/test/java/Features/AdminPortal/aoApplication/AP-357.feature"
        },
        glue = "StepDefinitions",
        monochrome = true,
        plugin = {"pretty",
        /*"html:target/cucumber-reports/cucumber-report.html",*/
        "json:target/cucumber-reports/cucumber-report.json","rerun:target/web_failed_scenarios.txt"})
public class WebTestRunner extends AbstractTestNGCucumberTests {


    @Override
    public void runScenario(PickleWrapper pickleWrapper, FeatureWrapper featureWrapper) {
        super.runScenario(pickleWrapper, featureWrapper);

    }

/*    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }*/

}
