package com.bloombergblack.qaautomation;
import static org.testng.Assert.*;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.List;

public class LoginPageTest {
    WebDriver driver;
    Bloombergblack bblackObject = new Bloombergblack();
    PasswordProperties passwordProperties_object = new PasswordProperties();

    @BeforeClass
    public void driver_run(){
        this.driver = new FirefoxDriver(profile);
        driver.get("https://secure.bloombergblack.com/login");
    }
    @AfterClass
    public void driver_close(){
        this.driver.close();
    }
    @Test
    public void title_is_correct(){
        String title = this.driver.getTitle();
        assertEquals(title, "BloombergBlack | Premium Wealth Management Service");
    }
    @Test
    public void user_name_input_box(){
        try{
            WebElement username_inputBox = this.driver.findElement(By.id("user_session_username"));
            assertNotNull(username_inputBox);
        } catch (NoSuchElementException e)  {
            assertTrue(false);
        }
    }
    @Test
    public void pswd_inputBox(){
        try{
            WebElement pswd = this.driver.findElement(By.id("user_session_password"));
            assertNotNull(pswd);
        } catch(NoSuchElementException e){
            assertTrue(false);
        }
    }
    @Test
    public void compare_username_pswd_boxSize(){

        try{
            WebElement username_box = this.driver.findElement(By.id("user_session_username"));
            Dimension d =  username_box.getSize();
            assertNotNull(username_box);
            int username_box_width = d.getWidth();
            System.out.println("Username box Width = " + username_box_width);

            WebElement pswd_box = this.driver.findElement(By.id("user_session_password"));
            Dimension p = pswd_box.getSize();
            assertNotNull(pswd_box);
            int pswd_box_width = p.getWidth();
            System.out.println("Password box width = " + pswd_box_width);

            assertEquals(username_box_width, pswd_box_width);
        } catch (NoSuchElementException e){
            assert(false);
        }

    }
    @Test
    public void compare_amount(){
        bblackObject.log_in(driver);

        WebElement bloomberge_amount = driver.findElement(By.className("medium_number"));
        String bloomberge_account_amount = bloomberge_amount.getText();
        if(bloomberge_account_amount.equals("$0.00")){
            bblackObject.create_assign_account(driver);
        }

        bloomberge_account_amount = bloomberge_account_amount.replace("$", "");
        bloomberge_account_amount = bloomberge_account_amount.replace(",","");

        System.out.print("Bloomberg Account Amount = " + bloomberge_account_amount);

        WebDriver fidelity_driver = new FirefoxDriver();
        fidelity_driver.get("https://www.fidelity.com/");

        WebElement logIn = fidelity_driver.findElement(By.id("navbar_log"));
        logIn.click();

        bblackObject.waitForElement(fidelity_driver, By.id("userId"));

        WebElement username = fidelity_driver.findElement(By.id("userId"));
        username.sendKeys(passwordProperties_object.fidelity_log_in_username());

        WebElement password = fidelity_driver.findElement(By.id("password"));
        password.sendKeys(passwordProperties_object.fidelity_log_in_pswd());
        password.submit();

        bblackObject.waitForElement(fidelity_driver, By.className("acc-message"));

        List<WebElement> amount_spans = fidelity_driver.findElements(By.className("acc-message"));
        WebElement amount_span = amount_spans.get(0);
        String amount_text = amount_span.getText();
        amount_text = amount_text.replace("$","").replace(",","");

        WebElement logOut = fidelity_driver.findElement(By.linkText("LOG OUT"));
        logOut.click();
        bblackObject.waitForElement(fidelity_driver, By.className("ofFirst"));
        fidelity_driver.close();

        System.out.println(" Fidelity amount = " + amount_text);

        BigDecimal bloomberg_amount  = new BigDecimal(bloomberge_account_amount);
        BigDecimal fidelity_amount = new BigDecimal(amount_text);

        BigDecimal difference = bloomberg_amount.subtract(fidelity_amount).abs();

        System.out.println("Difference = " +  difference);
        assertTrue(difference.compareTo(new BigDecimal(1)) <= 0);
    }

}

