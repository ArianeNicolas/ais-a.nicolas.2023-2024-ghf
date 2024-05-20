package es.codeurjc.ais.nitflex.smoke;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import static org.assertj.core.api.Assertions.assertThat;

public class SmokeTest {

    private WebDriver driver;
    String host = System.getProperty("host");

    @BeforeEach
	void setupTest() {
        FirefoxOptions options = new FirefoxOptions();
        options.setBinary("/snap/firefox/4033/usr/lib/firefox/firefox");
        options.addArguments("--headless");
		driver = new FirefoxDriver(options);
        
	}

    @AfterEach
	void teardown() {
		if (driver != null) {
			driver.quit();
		}
	}

    @Test
    void addMovie() throws InterruptedException {
        Thread.sleep(10000); //Wait 10 seconds to be sure deployment is complete
        driver.get(host);
        assertThat(driver.findElement(By.className("header")).getText()).isEqualTo("Welcome to Nitflex");
    }
}