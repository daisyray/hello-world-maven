package com.daisy.java;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.testng.Assert.*;
import java.util.List;


public class AmazonTest {

    WebDriver driver ;

    @BeforeClass
    public void driver_run(){
        this.driver = new FirefoxDriver();
        driver.get("http://www.amazon.com");
        this.waitForElement(driver, By.id("twotabsearchtextbox"));
    }

    @AfterClass
    public void driver_close(){
        this.driver.close();
    }

    //@Test
    public void search_for_item() {
        WebElement search_bar = driver.findElement(By.id("twotabsearchtextbox"));
        search_bar.sendKeys("Computers");
        search_bar.submit();
    }

    private void waitForElement(WebDriver driver, final By findCriteria){
        (new WebDriverWait(driver, 20)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                WebElement element = d.findElement(findCriteria);
                return element != null;
                /*
                if (element == null) {
                    return false;
                } else {
                    return true;
                }
                */
            }
        });
    }

    @Test
    public boolean check_for_prime_shipping(int index) {
        this.search_for_item();
        this.waitForElement(driver,By.id("atfResults"));

        WebElement item_1 = driver.findElement(By.id("result_"+index));
        try {
            WebElement primeElement = item_1.findElement(By.className("a-icon-prime"));
            // if it comes here, it's a prime
            this.addThisToCart(primeElement);
            return true;
        } catch (NoSuchElementException e) {
            // it's not a prime
        }
        return false;
    }
    //Finds all the prime members
    private void findAllPrimeMembers() {
        WebElement ul = this.driver.findElement(By.id("s-results-list-atf"));
        List<WebElement> lis = ul.findElements(By.tagName("li"));
        int index = 0;
        int primeCount = 0;
        for (WebElement li : lis) {
            if (this.check_for_prime_shipping(index)) {
                primeCount++;
            }
        }
        System.out.println("Total Number of Primes : " + primeCount);
    }
    public void addThisToCart(WebElement item){
        item.click();
        this.waitForElement(driver, By.id("add-to-cart-button"));
        driver.findElement(By.id("add-to-cart-button")).click();
        try {
            this.waitForElement(driver, By.id("siNoCoverage-announce"));
            driver.findElement(By.id("siNoCoverage-announce")).click();
        }catch (NoSuchElementException e) {

        }


    }
    public void check_for_prime_2() {
        // find all prime elements and compare against a known number
        // add all primes to cart until cart total >= $1000
    }


}
