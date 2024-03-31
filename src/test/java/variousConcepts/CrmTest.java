package variousConcepts;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CrmTest {

	WebDriver driver;
	String browser;
	String url;
	String userName = "demo@codefios.com";
	String password = "abc123";

	// test or mock date
	String dashboardHearderText = "Dashboard";
	String userNameAlertMsg = "Please enter your user name";
	String passwordAlertMsg = "Please enter your password";
	String newCustomerHearderText = "New Customer";
	String fullName = "Selenium";
	String company = "Techfios";
	String eamil = "abc123@techfios.com";
	String phoneNo = "4567890";
	String country = "Afghanistan";

	// By type
	By USER_NAME_FIELD = By.xpath("//*[@id=\"user_name\"]");
	By PASSWORD_FIELD = By.xpath("//*[@id=\"password\"]");
	By SIGNIN_BUTTON_FIELD = By.xpath("//*[@id=\"login_submit\"]");
	By DASHBOARD_HEARDER_FIELD = By.xpath("/html/body/div[1]/section/div/div[2]/div/div/header/div/strong");
	By CUSTOMER_MENU_FIELD = By.xpath("/html/body/div[1]/aside[1]/div/nav/ul[2]/li[2]/a/span");
	By ADD_CUSTOMER_MENU_FIELD = By.xpath("//*[@id=\"customers\"]/li[2]/a/span");
	By ADD_CUSTOMER_HEADER_FIELD = By
			.xpath("/html/body/div[1]/section/div/div[2]/div/div[1]/div[1]/div/div/header/div/strong");
	By FULL_NAME_FIELD = By.xpath("//*[@id=\"general_compnay\"]/div[1]/div/input");
	By COMPANY_DROPDOWN_FIELD = By.xpath("//select[@name='company_name']");
	By EMAIL_FIELD = By.xpath("//*[@id=\"general_compnay\"]/div[3]/div/input");
	By PHONE_FIELD = By.xpath("//*[@id=\"phone\"]");
	By COUNTRY_DROPDOWN_FIELD = By.xpath("//*[@id=\"general_compnay\"]/div[8]/div[1]/select");

	@BeforeClass
	public void readConfig() {

		// InputStream //BufferedReader //FileREader //Scanner

		try {

			InputStream input = new FileInputStream("src\\main\\java\\config\\config.properties");

			Properties prop = new Properties();
			prop.load(input);
			browser = prop.getProperty("browser");
			System.out.println("Browser used: " + browser);
			url = prop.getProperty("url");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@BeforeMethod
	public void init() {

		if (browser.equalsIgnoreCase("Chrome")) {
			System.setProperty("webdriver.chrome.driver", "drivers\\chromedriver.exe");
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("Edge")) {
			System.setProperty("webdriver.edge.driver", "drivers\\msedgedriver.exe");
			driver = new EdgeDriver();
		} else {
			System.out.println("Please insert a valid browser..");
		}

		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.get(url);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@Test(priority = 2)
	public void loginTest() {

		driver.findElement(USER_NAME_FIELD).sendKeys(userName);
		driver.findElement(PASSWORD_FIELD).sendKeys(password);
		driver.findElement(SIGNIN_BUTTON_FIELD).click();
		Assert.assertEquals(driver.findElement(DASHBOARD_HEARDER_FIELD).getText(), "Dashboard",
				"Dashboard page not found!");

	}

	@Test(priority = 1)
	public void validateAlertMsgs() {

		driver.findElement(SIGNIN_BUTTON_FIELD).click();
		String actualUserNameAlertMsg = driver.switchTo().alert().getText();
		Assert.assertEquals(actualUserNameAlertMsg, userNameAlertMsg, "Alert doesn't match!!");
		driver.switchTo().alert().accept();

		driver.findElement(USER_NAME_FIELD).sendKeys(userName);
		driver.findElement(SIGNIN_BUTTON_FIELD).click();
		String actualPasswordAlertMsg = driver.switchTo().alert().getText();
		Assert.assertEquals(actualPasswordAlertMsg, passwordAlertMsg, "Alert doesn't match!!");
		driver.switchTo().alert().accept();

	}

	@Test
	public void addCustomer() {

		loginTest();
		driver.findElement(CUSTOMER_MENU_FIELD).click();
		driver.findElement(ADD_CUSTOMER_MENU_FIELD).click();
		Assert.assertEquals(driver.findElement(ADD_CUSTOMER_HEADER_FIELD).getText(), newCustomerHearderText,
				"New Customer page not found!");

		driver.findElement(FULL_NAME_FIELD).sendKeys(fullName + generateRandomNum(999));

		selectFromDrowdown(COMPANY_DROPDOWN_FIELD, company);

		driver.findElement(EMAIL_FIELD).sendKeys(generateRandomNum(9999) + eamil);
		driver.findElement(PHONE_FIELD).sendKeys(phoneNo + generateRandomNum(999));

		selectFromDrowdown(COUNTRY_DROPDOWN_FIELD, country);

	}

	private void selectFromDrowdown(By field, String visibleText) {
		Select sel = new Select(driver.findElement(field));
		sel.selectByVisibleText(visibleText);

	}

	private int generateRandomNum(int boundryNum) {
		Random rnd = new Random();
		int generatedNum = rnd.nextInt(999);
		return generatedNum;

	}

	@AfterMethod
	public void tearDown() {
		driver.close();
		driver.quit();
	}

}
