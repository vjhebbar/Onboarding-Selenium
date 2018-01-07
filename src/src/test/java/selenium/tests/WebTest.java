package selenium.tests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

//import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.ChromeDriverManager;

import com.paulhammant.ngwebdriver.NgWebDriver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class WebTest
{
	private static WebDriver driver;
	private String ROOT = "http://192.168.8.8:8080/";
	
	@BeforeClass
	public static void setUp() throws Exception 
	{
		//driver = new HtmlUnitDriver();
		ChromeDriverManager.getInstance().setup();
		ChromeOptions options = new ChromeOptions();
		//options.addArguments("headless");
		//options.addArguments("window-size=1200x600");
		//options.addArguments("blink-settings=imagesEnabled=false");
		driver = new ChromeDriver(options);
	}
	
	@AfterClass
	public static void  tearDown() throws Exception
	{
		driver.close();
		driver.quit();
	}

	
	@Test
	public void googleExists() throws Exception
	{
		driver.get("http://www.google.com");
        assertEquals("Google", driver.getTitle());		
	}
	
	private static void WaitForAngularLoad()
	{
		new NgWebDriver((ChromeDriver)driver).waitForAngularRequestsToFinish();
	}

	@Test
	public void NeverRunOutOfCoffee() throws Exception
	{
		driver.get(ROOT+"inventory.html");

		String xPath = "//span[@id='currentCoffee']";
		
		//WebDriverWait wait = new WebDriverWait(driver, 30);
		//wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xPath)));

		WaitForAngularLoad();
		WebElement span = driver.findElement(By.xpath(xPath));

		assertNotNull(span);
		assertThat("We always have coffee", Integer.parseInt(span.getText()),
				   greaterThan(0));
	}

}
