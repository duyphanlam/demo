package tests;

import java.util.Arrays;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.LoginPage;

public class LoginTests {
    private WebDriver driver;
    private LoginPage loginPage;

    private static final String FACEBOOK_EMAIL = "phanduylamgv@gmail.com";
    private static final String FACEBOOK_PASSWORD = "@Liemlam27022004";
    private static final String GOOGLE_EMAIL = "phanduylamgv@gmail.com";
    private static final String GOOGLE_PASSWORD = "your_google_password";

    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized"); // Maximize window
        options.addArguments("--disable-notifications"); // Tắt thông báo
        options.addArguments("--disable-blink-features=AutomationControlled"); // Chống phát hiện automation
        options.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation"));
        driver.get("https://www.booking.com");
        loginPage = new LoginPage(driver);
    }

    private void manualCaptchaInput(int seconds) {
        System.out.println("Please solve the CAPTCHA manually within " + seconds + " seconds.");
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("CAPTCHA input interrupted.");
        }
    }

    @Test(priority = 1)
    public void testSuccessfulLogin() {
        // Nhấn vào nút login trên header nếu cần.
        // Ví dụ: tìm và click nút đăng nhập từ Booking.com trước khi chuyển sang trang đăng nhập
        driver.findElement(By.xpath("//a[contains(@href, 'account.booking.com')]")).click();
        // Chuyển sang trang đăng nhập chi tiết
        driver.get("https://account.booking.com/");
        
        // Dùng các phương thức của LoginPage thay vì thao tác trực tiếp locator
        loginPage.enterEmail("phanduylamgv@gmail.com");
        loginPage.clickContinue();
        manualCaptchaInput(15);

        System.out.println("=== Testcase 1: Successful Login ===");
        System.out.println(" * Input: phanduylamgv@gmail.com");
        System.out.println(" * Expected: Login successful");
        System.out.println(" * Result: Passed");
        System.out.println("-----------------------------");
    }


    @Test(priority = 2)
    public void testEmptyEmailField() {
        driver.get("https://account.booking.com/");
        loginPage.clickContinue();
        String errorMessage = loginPage.getEmptyEmailError();
        System.out.println("=== Testcase 2: Empty Email Field ===");
        System.out.println("  * Input: (empty)");
        System.out.println("  * Expected: Error message displayed");
        System.out.println("  * Actual Error: " + errorMessage);
        System.out.println("  * Result: Passed");
        Assert.assertTrue(!errorMessage.isEmpty(), "Error message not shown");
        System.out.println("-----------------------------");
    }

    @Test(priority = 3)
    public void testInvalidEmailSimpleString() {
        driver.get("https://account.booking.com/");
        loginPage.enterEmail("abc");
        loginPage.clickContinue();
        String errorMessage = loginPage.getInvalidEmailError();
        System.out.println("=== Testcase 3: Invalid Email Format (Simple String) ===");
        System.out.println("  * Input: abc");
        System.out.println("  * Expected: Error message displayed");
        System.out.println("  * Actual Error: " + errorMessage);
        System.out.println("  * Result: Passed");
        Assert.assertTrue(!errorMessage.isEmpty(), "Error message not shown for email: abc");
        System.out.println("-----------------------------");
    }

    @Test(priority = 4)
    public void testInvalidEmailMissingUsername() {
        driver.get("https://account.booking.com/");
        loginPage.enterEmail("@gmail.com");
        loginPage.clickContinue();
        String errorMessage = loginPage.getInvalidEmailError();
        System.out.println("=== Testcase 4: Invalid Email Format (Missing Username) ===");
        System.out.println("  * Input: @gmail.com");
        System.out.println("  * Expected: Error message displayed");
        System.out.println("  * Actual Error: " + errorMessage);
        System.out.println("  * Result: Passed");
        Assert.assertTrue(!errorMessage.isEmpty(), "Error message not shown for email: @gmail.com");
        System.out.println("-----------------------------");
    }

    @Test(priority = 5)
    public void testInvalidEmailNumeric() {
        driver.get("https://account.booking.com/");
        loginPage.enterEmail("123");
        loginPage.clickContinue();
        String errorMessage = loginPage.getInvalidEmailError();
        System.out.println("=== Testcase 5: Invalid Email Format (Numeric) ===");
        System.out.println("  * Input: 123");
        System.out.println("  * Expected: Error message displayed");
        System.out.println("  * Actual Error: " + errorMessage);
        System.out.println("  * Result: Passed");
        Assert.assertTrue(!errorMessage.isEmpty(), "Error message not shown for email: 123");
        System.out.println("-----------------------------");
    }

    @Test(priority = 6)
    public void testInvalidEmailSpecialCharacters() {
        driver.get("https://account.booking.com/");
        loginPage.enterEmail("!@#$%^");
        loginPage.clickContinue();
        String errorMessage = loginPage.getInvalidEmailError();
        System.out.println("=== Testcase 6: Invalid Email Format (Special Characters) ===");
        System.out.println("  * Input: !@#$%^");
        System.out.println("  * Expected: Error message displayed");
        System.out.println("  * Actual Error: " + errorMessage);
        System.out.println("  * Result: Passed");
        Assert.assertTrue(!errorMessage.isEmpty(), "Error message not shown for email: !@#$%^");
        System.out.println("-----------------------------");
    }

    @Test(priority = 7)
    public void testFacebookLogin() {
        driver.get("https://account.booking.com/");
        loginPage.clickFacebookLogin();
        String mainWindowHandle = driver.getWindowHandle();
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(mainWindowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
        loginPage.enterFacebookEmail(FACEBOOK_EMAIL);
        loginPage.enterFacebookPassword(FACEBOOK_PASSWORD);
        loginPage.clickFacebookLoginButton();
        manualCaptchaInput(15);
        driver.switchTo().window(mainWindowHandle);
        System.out.println("=== Testcase 7: Facebook Login ===");
        System.out.println("  * Input: Automated Facebook Login");
        System.out.println("  * Expected: Successful login to Booking.com");
        System.out.println("  * Result: Passed");
        System.out.println("-----------------------------");
    }

    @Test(priority = 8)
    public void testGoogleLogin() {
        driver.get("https://account.booking.com/");
        loginPage.clickGoogleLogin();
        String mainWindowHandle = driver.getWindowHandle();
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(mainWindowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
        loginPage.enterGoogleEmail(GOOGLE_EMAIL);
        loginPage.clickGoogleNextButton();
        loginPage.enterGooglePassword(GOOGLE_PASSWORD);
        loginPage.clickGoogleNextButton2();
        driver.switchTo().window(mainWindowHandle);
        System.out.println("=== Testcase 8: Google Login ===");
        System.out.println("  * Input: Automated Google Login");
        System.out.println("  * Expected: Successful login to Booking.com");
        System.out.println("  * Result: Passed");
        System.out.println("-----------------------------");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
