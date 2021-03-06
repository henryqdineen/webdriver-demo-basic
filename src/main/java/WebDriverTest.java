import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

@ContextConfiguration(locations = {"classpath:context.xml"})
@TestExecutionListeners(
		listeners = {DependencyInjectionTestExecutionListener.class},
		inheritListeners = false
)
public abstract class WebDriverTest extends AbstractTestNGSpringContextTests {

	protected WebDriver driver;

	@Autowired
	private WebDriverFactory webDriverFactory;

	@BeforeMethod(alwaysRun = true)
	protected void beforeMethod(ITestContext context) {
		driver = webDriverFactory.createInstance(context.getName());
	}

	@AfterMethod(alwaysRun = true)
	protected void afterMethod(ITestResult result) {
		if (driver instanceof SauceLabsDriver) {
			((SauceLabsDriver) driver).updateJobInfo("passed", Boolean.valueOf(result.isSuccess()));
		}
		driver.quit();
	}
}
