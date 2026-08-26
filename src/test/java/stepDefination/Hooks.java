package stepDefination;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import factory.BaseClass;

public class Hooks {

	WebDriver driver;
	Properties p;

	// Shared so ThenStepScreenshotListener (separate plugin class) can reuse
	// the same active driver without re-launching a browser.
	static WebDriver sharedDriver;

	@Before
	public void setup() throws IOException {
		driver = BaseClass.initilizeBrowser();
		sharedDriver = driver;
		p = BaseClass.getProperties();
		driver.get(p.getProperty("appURL"));
	}

	@After(order = 0)
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
		sharedDriver = null;
		BaseClass.reset();
	}

	@After(order = 1)
	public void addScreenshotOnFailure(Scenario scenario) {
		if (scenario.isFailed()) {
			saveScreenshot(scenario.getName() + "_FAILED");
		}
	}

	// Reusable screenshot saver — called both on scenario failure
	// and after every "Then" step (via ThenStepScreenshotListener).
	static void saveScreenshot(String fileNameBase) {
		if (sharedDriver == null) {
			return;
		}
		TakesScreenshot ts = (TakesScreenshot) sharedDriver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		File destination = new File(
				System.getProperty("user.dir")
				+ "/target/screenshots/"
				+ fileNameBase.replaceAll("[^a-zA-Z0-9.-]", "_")
				+ "_" + System.currentTimeMillis()
				+ ".png"
				);
		try {
			Files.createDirectories(
					destination.getParentFile().toPath()
					);
			Files.copy(
					source.toPath(),
					destination.toPath(),
					StandardCopyOption.REPLACE_EXISTING
					);
			System.out.println(
					"Screenshot saved at: "
							+ destination.getAbsolutePath()
					);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}