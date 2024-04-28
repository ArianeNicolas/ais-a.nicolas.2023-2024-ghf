package es.codeurjc.ais.nitflex.e2e.selenium;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

@SpringBootTest(
    classes = Application.class, 
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SeleniumTests {
    
    @LocalServerPort
    int port;

    private WebDriver driver;

    //film infos
    String title = "Casino Royal";
    String year = "2006";
    String image = "https://images.affiches-et-posters.com//albums/3/52534/medium/poster-film-james-bond-casino-royale.jpg";
    String synopsis = "The first James Bond movie with Daniel Craig";

    @BeforeEach
	void setupTest() {
        FirefoxOptions options = new FirefoxOptions();
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
        System.out.println("add movie !");
        driver.get("http://localhost:"+this.port+"/");
        driver.findElement(By.id("create-film")).click();
        driver.findElement(By.name("title")).sendKeys(title);
        driver.findElement(By.name("releaseYear")).sendKeys(year);
        driver.findElement(By.name("url")).sendKeys(image);
        driver.findElement(By.name("synopsis")).sendKeys(synopsis);
        driver.findElement(By.id("Save")).click();
        Thread.sleep(1000);
        assertThat(driver.findElement(By.id("film-title")).getText()).isEqualTo(title);
        
        //delete the film for next tests
        driver.findElement(By.id("all-films")).click();
        driver.findElement(By.linkText(title)).click();
        driver.findElement(By.id("remove-film")).click();
    }

    @Test
    void deleteMovie() throws InterruptedException {
        driver.get("http://localhost:"+this.port+"/");

        //Create a new film and come back to the list of films
        driver.findElement(By.id("create-film")).click();
        driver.findElement(By.name("title")).sendKeys(title);
        driver.findElement(By.name("releaseYear")).sendKeys(year);
        driver.findElement(By.name("url")).sendKeys(image);
        driver.findElement(By.name("synopsis")).sendKeys(synopsis);
        driver.findElement(By.id("Save")).click();
        driver.findElement(By.id("all-films")).click();

        Thread.sleep(1000);

        //Delete the film
        driver.findElement(By.linkText(title)).click();
        driver.findElement(By.id("remove-film")).click();

        Thread.sleep(1000);
        //Verify that we get the deletion message
        assertThat(driver.findElement(By.id("message")).getText()).contains("deleted");

        //Come back to the list of films
        driver.findElement(By.id("all-films")).click();
        Thread.sleep(1000);
        //Verify that the film is not in the list anymore
        assertThat(driver.findElements(By.linkText(title))).isEmpty();

    }

    @Test
    void cancelEdit() throws InterruptedException{
        driver.get("http://localhost:"+this.port+"/");

        //Create a new film
        driver.findElement(By.id("create-film")).click();
        driver.findElement(By.name("title")).sendKeys(title);
        driver.findElement(By.name("releaseYear")).sendKeys(year);
        driver.findElement(By.name("url")).sendKeys(image);
        //driver.findElement(By.name("synopsis")).sendKeys(synopsis);
        driver.findElement(By.id("Save")).click();
        //We are now on the page about the created film
        Thread.sleep(1000);
        driver.findElement(By.id("edit-film")).click();//We click on Edit
        driver.findElement(By.id("cancel")).click();

        Thread.sleep(1000);
        //Verify that we are on the film page, and that the infos haven't change
        assertThat(driver.findElement(By.id("film-title")).getText()).isEqualTo(title);
        assertThat(driver.findElement(By.id("image")).getAttribute("src")).isEqualTo(image);
        assertThat(driver.findElement(By.id("film-synopsis")).getText()).isEqualTo(synopsis);
    }
}