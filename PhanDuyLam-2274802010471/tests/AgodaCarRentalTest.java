package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.AgodaCarRentalPage;

public class AgodaCarRentalTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private AgodaCarRentalPage carRentalPage;

    @BeforeClass
    public void setUp() {
        // Đảm bảo đường dẫn đến ChromeDriver đúng
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        // Tăng thời gian chờ lên 30 giây để đảm bảo trang tải đầy đủ
        wait = new WebDriverWait(driver, (30));
        // Khởi tạo đối tượng AgodaCarRentalPage
        carRentalPage = new AgodaCarRentalPage(driver, wait);
    }

    @Test(priority = 1)
    public void testAgodaCarRental() throws Exception {
        driver.get("https://www.agoda.com/vi-vn/");
        try {
            // Step 1: Click vào dropdown "Phương tiện di chuyển"
            carRentalPage.clickTransportButton();

            // Step 2: Chờ dropdown hiển thị
            carRentalPage.waitForTransportDropdown();

            // Step 3: Click vào tùy chọn "Thuê xe"
            carRentalPage.clickCarRentalOption();

            // Step 4: Nhấn nút Search
            carRentalPage.clickSearchButton();

            // Step 5: Chờ cho URL thay đổi thành kết quả tìm kiếm
            carRentalPage.waitForResultsPage();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}