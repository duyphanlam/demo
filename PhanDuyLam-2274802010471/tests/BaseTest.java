package tests;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.annotations.*;

import java.time.Duration;

public class BaseTest {
    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        Reporter.log("\n=== INITIATING TEST SUITE: BOOKING.COM SEARCH TESTS ===", true);
        Reporter.log("Setting up test environment...", true);
        initializeDriver();
        Reporter.log("✓ Test environment setup complete", true);
    }

    private void initializeDriver() {
        Reporter.log("Initializing Chrome WebDriver...", true);
        try {
            System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
            Reporter.log("✓ ChromeDriver path set", true);
            
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-notifications");
            options.addArguments("--disable-popup-blocking");
            options.addArguments("--disable-blink-features=AutomationControlled");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            Reporter.log("✓ Chrome options configured", true);
            
            driver = new ChromeDriver(options);
            Reporter.log("✓ ChromeDriver instance created", true);
            
            driver.manage().window().maximize();
            Reporter.log("✓ Browser window maximized", true);
            
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            Reporter.log("✓ Implicit wait set to 10 seconds", true);
            
            wait = new WebDriverWait(driver, (10));
            Reporter.log("✓ Explicit wait set to 10 seconds", true);
            
            Reporter.log("✓ WebDriver initialized successfully", true);
        } catch (Exception e) {
            Reporter.log("✗ Failed to initialize WebDriver: " + e.getMessage(), true);
            throw e;
        }
    }

    @AfterMethod
    public void resetDriver() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                Reporter.log("Failed to quit driver: " + e.getMessage(), true);
            }
        }
        initializeDriver();
        Reporter.log("Reset WebDriver for next test case", true);
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            try {
                driver.quit();
                Reporter.log("Browser closed successfully", true);
            } catch (Exception e) {
                Reporter.log("Failed to close browser: " + e.getMessage(), true);
            } finally {
                driver = null;
            }
        }
    }

    // Utility methods
    protected void captureScreenshot(String testName) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String destination = "test-output/screenshots/" + testName + "_" + timestamp + ".png";
            FileUtils.copyFile(source, new File(destination));
            Reporter.log("Screenshot saved: " + destination);
        } catch (IOException e) {
            Reporter.log("Failed to capture screenshot: " + e.getMessage());
        }
    }
}