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
		options.addArguments("headless");
		options.addArguments("window-size=1200x600");
		options.addArguments("blink-settings=imagesEnabled=false");
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
	
	private static void WaitForAngularToLoad()
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

		WaitForAngularToLoad();
		WebElement span = driver.findElement(By.xpath(xPath));

		assertNotNull(span);
		assertThat("We always have coffee", Integer.parseInt(span.getText()),
				   greaterThan(0));
	}

	@Test
	public void VerifyAddInventory() throws Exception
	{
		driver.get(ROOT+"inventory.html");
		
		WaitForAngularToLoad();

		String coffeeValue = driver.findElement(By.xpath("//span[@id='currentCoffee']")).getText();
		String milkValue = driver.findElement(By.xpath("//span[@id='currentMilk']")).getText();
		String sugarValue = driver.findElement(By.xpath("//span[@id='currentSugar']")).getText();
		String chocolateValue = driver.findElement(By.xpath("//span[@id='currentChocolate']")).getText();

		WebElement coffeeInput = driver.findElement(By.xpath("//input[@name='coffee']"));
		WebElement milkInput = driver.findElement(By.xpath("//input[@name='milk']"));
		WebElement sugarInput = driver.findElement(By.xpath("//input[@name='sugar']"));
		WebElement chocolateInput = driver.findElement(By.xpath("//input[@name='chocolate']"));

		coffeeInput.sendKeys("1");
		milkInput.sendKeys("1");
		sugarInput.sendKeys("1");
		chocolateInput.sendKeys("1");

		WebElement button = driver.findElement(By.xpath("//input[@value='Submit']"));
		button.click();

		// Get page again.
		driver.get(ROOT+"inventory.html");
		WaitForAngularToLoad();

		String currentCoffee = driver.findElement(By.xpath("//span[@id='currentCoffee']")).getText();
		String currentMilk = driver.findElement(By.xpath("//span[@id='currentMilk']")).getText();
		String currentSugar = driver.findElement(By.xpath("//span[@id='currentSugar']")).getText();
		String currentChocolate = driver.findElement(By.xpath("//span[@id='currentChocolate']")).getText();

		assertEquals(Integer.parseInt(currentCoffee), Integer.parseInt(coffeeValue)+1);
		assertEquals(Integer.parseInt(currentMilk), Integer.parseInt(milkValue)+1);
		assertEquals(Integer.parseInt(currentSugar), Integer.parseInt(sugarValue)+1);
		assertEquals(Integer.parseInt(currentChocolate), Integer.parseInt(chocolateValue)+1);
	}

}
