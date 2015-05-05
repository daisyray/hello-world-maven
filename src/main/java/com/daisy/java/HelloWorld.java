package com.daisy.java;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.testng.Assert.assertNotNull;


public class HelloWorld {
	public static void main(String[] args) {
        WebDriver driver = new FirefoxDriver();
        driver.get("http://www.amazon.com");

        driver.close();
	}

    public void check_for_prime_shipping(WebDriver driver) {

        WebElement search_bar = driver.findElement(By.id("twotabsearchtextbox"));
        search_bar.sendKeys("Computers");
        search_bar.submit();
        new WebDriverWait(driver, 10);
        WebElement list_of_items = driver.findElement(By.id("atfResults"));

        WebElement item_1 = list_of_items.findElement(By.id("result_0"));

        WebElement prime_class = item_1.findElement(By.className("a-icon-prime"));
        assertNotNull(prime_class);
    }
}
