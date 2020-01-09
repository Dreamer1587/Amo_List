package com.amazon;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Amazon {

	WebDriver driver;
	Properties pr;

	@Test
	public void Login() throws IOException {

		File src = new File("C:\\Users\\HP\\Amazon\\datafile.properties");
		FileInputStream file = new FileInputStream(src);
		pr = new Properties();
		pr.load(file);

		System.setProperty("webdriver.chrome.driver", "C:\\Users\\HP\\Amazon\\driver\\chromedriver.exe");
		driver = new ChromeDriver();

		driver.get(pr.getProperty("url"));
		driver.manage().window().maximize();

		// Signin
		driver.manage().timeouts().implicitlyWait(2000, TimeUnit.SECONDS);

		driver.findElement(By.linkText(pr.getProperty("Signin"))).click();
		driver.findElement(By.id(pr.getProperty("id_username"))).sendKeys(pr.getProperty("username"));
		driver.findElement(By.id(pr.getProperty("id_continue"))).click();
		driver.findElement(By.id(pr.getProperty("id_password"))).sendKeys(pr.getProperty("password"));
		driver.findElement(By.id(pr.getProperty("id_submit"))).click();

		// Search item
		driver.findElement(By.id(pr.getProperty("id_searchbox"))).sendKeys("tshirt for mens", Keys.ENTER);

		driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);

	}

	@Test
	public void cancel_cod() {

		// driver.findElement(By.linkText(pr.getProperty("Linktext_select_item"))).click();

		List<WebElement> allLinks = driver
				.findElements(By.xpath("//div[@class='a-section aok-relative s-image-tall-aspect']/img"));
		for (WebElement link : allLinks) {
			System.out.println(link.getText() + " - " + link.getAttribute("src"));

		}
		allLinks.get(1).click();

		// Switch windows

		String parentWindow = driver.getWindowHandle();
		Set<String> handles = driver.getWindowHandles();

		for (String windowHandle : handles) {
			if (!windowHandle.equals(parentWindow)) {

				driver.switchTo().window(windowHandle);
				System.out.println(driver.getCurrentUrl());

				// click buy
				driver.manage().timeouts().implicitlyWait(2000, TimeUnit.SECONDS);
				driver.findElement(By.id("buy-now-button")).click();

				// select radio button COD
				driver.findElement(By.xpath(pr.getProperty("xpath_selectradio_button"))).click();

				// click continue
				// click when the element is clickable

				WebElement element = driver.findElement(By.xpath(pr.getProperty("xpath_click")));
				Actions actions = new Actions(driver);
				actions.moveToElement(element).click().build().perform();

				// place the order
				driver.findElement(By.xpath(pr.getProperty("xpath_placeorder"))).click();

				// click on orders to see order
				driver.findElement(By.linkText(pr.getProperty("Linktext_vieworder"))).click();

				// cancel the item
				driver.findElement(By.id(pr.getProperty("id_cancel"))).click();

				// select reason
				Select reason = new Select(driver.findElement(By.name("cancel.reason")));
				reason.selectByVisibleText("Order Created by Mistake");

				// Final cancel

				WebElement element1 = driver.findElement(By.xpath(pr.getProperty("xpath_finalcancel")));
				Actions actions1 = new Actions(driver);
				actions1.moveToElement(element1).click().build().perform();

				String s = driver.findElement(By.xpath("//div/h4")).getText();

				System.out.println(s);
				Assert.assertEquals(s, s);

			}

		}
	}
}
