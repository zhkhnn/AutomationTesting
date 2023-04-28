import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;

public class Main{
    WebDriver driver;
    @BeforeSuite
    public void setup(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1000));
        driver.get("https://www.saucedemo.com/");
    }
    @Test(dataProvider = "loginData", priority = 0)
    public void loginTest(String userId, String password, String expectedUrl){
        WebElement userIDFld = driver.findElement(By.name("user-name"));
        WebElement passwordFld = driver.findElement(By.name("password"));
        WebElement loginFld = driver.findElement(By.name("login-button"));

        userIDFld.sendKeys(userId);
        passwordFld.sendKeys(password);
        loginFld.click();

        Assert.assertEquals(driver.getCurrentUrl(), expectedUrl);
    }
    @DataProvider(name = "loginData")
    public Object[][] loginData(){
        return new Object[][]{
                {"standard_user", "secret_sauce", "https://www.saucedemo.com/inventory.html"}
        };
    }
    @Test(priority = 1)
    public void cartTest(){
        WebElement btn1 = driver.findElement(By.xpath("//*[@id='add-to-cart-sauce-labs-backpack']"));
        WebElement btn2 = driver.findElement(By.xpath("//*[@id='add-to-cart-sauce-labs-bike-light']"));
        btn1.click();
        btn2.click();

        WebElement cart = driver.findElement(By.className("shopping_cart_link"));
        cart.click();

        WebElement product1 = driver.findElement(By.id("item_4_title_link"));
        WebElement product2 = driver.findElement(By.id("item_0_title_link"));
        Assert.assertEquals(product1.getText(), "Sauce Labs Backpack");
        Assert.assertEquals(product2.getText(), "Sauce Labs Bike Light");
    }
    @Test(dataProvider = "checkoutData", priority = 2)
    public void checkoutTest(String firstname, String lastname, String zipcode,String expectedUrl){
        WebElement checkoutBtn = driver.findElement(By.id("checkout"));
        checkoutBtn.click();

        WebElement fname = driver.findElement(By.name("firstName"));
        WebElement lname = driver.findElement(By.name("lastName"));
        WebElement code = driver.findElement(By.name("postalCode"));
        WebElement continuebtn = driver.findElement(By.name("continue"));

        fname.sendKeys(firstname);
        lname.sendKeys(lastname);
        code.sendKeys(zipcode);
        continuebtn.click();

        WebElement finishbtn = driver.findElement(By.name("finish"));
        finishbtn.click();

        Assert.assertEquals(driver.getCurrentUrl(), expectedUrl);
    }

    @DataProvider(name = "checkoutData")
    public Object[][] checkoutData(){
        return new Object[][]{
                {"Seitmurat", "Seitkul", "160001", "https://www.saucedemo.com/checkout-complete.html"}
        };
    }
}
