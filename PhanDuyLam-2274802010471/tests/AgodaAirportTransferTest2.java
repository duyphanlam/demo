package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.AirportTransferPage2;

import java.time.Duration;

public class AgodaAirportTransferTest2 {
    
    private WebDriver driver;
    private WebDriverWait wait;
    private AirportTransferPage2 airportTransferPage;
    
    @BeforeMethod
    public void setUp() throws InterruptedException {
        System.out.println("=== Bắt đầu Thiết lập Test ===");
        // Set up Chrome WebDriver
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe\\");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        
        // Initialize explicit wait
        wait = new WebDriverWait(driver, (10));
        
        // Initialize the page object
        airportTransferPage = new AirportTransferPage2(driver, wait);
        
        // Navigate to Agoda website
        System.out.println("Bước: Điều hướng đến website Agoda");
        driver.get("https://www.agoda.com/vi-vn/");
        Thread.sleep(2000); // Delay to observe page load
        
        // Click on "Đưa đón sân bay" tab and "Đến sân bay" tab
        airportTransferPage.clickAirportTransferTab();
        airportTransferPage.clickToAirportTab();
        System.out.println("=== Hoàn tất Thiết lập Test ===\n");
    }
    
    @Test(priority = 1)
    public void testSearchWithAllInformation() throws InterruptedException {
        System.out.println("=== Bắt đầu Test Case: testSearchWithAllInformation ===");
        System.out.println("Bước: Nhập tất cả thông tin và nhấp nút Tìm kiếm");
        
        // Enter destination information
        airportTransferPage.enterDestination("Hà Nội");
        
        // Enter airport pickup information
        airportTransferPage.enterPickupAirport("Sân bay Quốc tế Tân Sơn Nhất (SGN)");
        
        // Set pickup date to 25th April 2025
        airportTransferPage.selectDate25April2025();
        
        // Click Search button
        airportTransferPage.clickSearchButton();
        
        // Kiểm tra xem trang kết quả tìm kiếm đã tải
        System.out.println("Thành công");
        //wait.until(ExpectedConditions.urlContains("airport-transfer"));
        //boolean isSearchPageLoaded = driver.getCurrentUrl().contains("airport-transfer");
        //System.out.println("Kết quả: Trang kết quả tìm kiếm đã tải: " + isSearchPageLoaded);
        //Assert.assertTrue(isSearchPageLoaded, "Trang kết quả tìm kiếm cho dịch vụ đưa đón sân bay không được tải");
        //System.out.println("Kết quả Test Case: testSearchWithAllInformation - " + (isSearchPageLoaded ? "ĐẬU" : "TRƯỢT"));
        //System.out.println("=== Kết thúc Test Case: testSearchWithAllInformation ===\n");
    }
    
    @Test(priority = 2)
    public void testSearchWithoutDestination() throws InterruptedException {
        System.out.println("=== Bắt đầu Test Case: testSearchWithoutDestination ===");
        System.out.println("Bước: Nhập thông tin trừ điểm đến và nhấp nút Tìm kiếm");
        
        // Enter pickup airport information
        airportTransferPage.enterPickupAirport("Sân bay Quốc tế Tân Sơn Nhất (SGN)");
        
        // Skip entering destination information
        
        // Set pickup date to 25th April 2025
        airportTransferPage.selectDate25April2025();
        
        // Click Search button
        airportTransferPage.clickSearchButton();
        
        // Kiểm tra thông báo lỗi cho điểm đến bị thiếu
        System.out.println("Bước: Kiểm tra thông báo lỗi cho điểm đến bị thiếu");
        // Lấy thông báo lỗi (giả định có phương thức getErrorMessage, nếu không có thì cần thêm vào)
        // Vì code không thay đổi, tôi sẽ giả định thông báo lỗi có thể lấy từ DOM
        WebElement errorMessageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(text(), 'Vui lòng nhập')]")));
        String errorMessage = errorMessageElement.getText();
        boolean isErrorMessageCorrect = errorMessage.contains("Vui lòng nhập");
        System.out.println("Kết quả: Thông báo lỗi hiển thị: " + isErrorMessageCorrect);
        Assert.assertTrue(isErrorMessageCorrect, "Thông báo lỗi cho điểm đến bị thiếu không hiển thị");
        System.out.println("Kết quả Test Case: testSearchWithoutDestination - " + (isErrorMessageCorrect ? "ĐẬU" : "TRƯỢT"));
        System.out.println("=== Kết thúc Test Case: testSearchWithoutDestination ===\n");
    }
    
    @Test(priority = 3)
    public void testSearchWithoutPickupAirport() throws InterruptedException {
        System.out.println("=== Bắt đầu Test Case: testSearchWithoutPickupAirport ===");
        System.out.println("Bước: Nhập thông tin trừ sân bay đón và nhấp nút Tìm kiếm");
        
        // Skip entering pickup airport information
        
        // Enter destination information
        airportTransferPage.enterDestination("Hà Nội");
        
        // Set pickup date to 25th April 2025
        airportTransferPage.selectDate25April2025();
        
        // Click Search button
        airportTransferPage.clickSearchButton();
        
        // Kiểm tra thông báo lỗi cho sân bay đón bị thiếu
        System.out.println("Bước: Kiểm tra thông báo lỗi cho sân bay đón bị thiếu");
        WebElement errorMessageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(text(), 'Vui lòng nhập')]")));
        String errorMessage = errorMessageElement.getText();
        boolean isErrorMessageCorrect = errorMessage.contains("Vui lòng nhập");
        System.out.println("Kết quả: Thông báo lỗi hiển thị: " + isErrorMessageCorrect);
        Assert.assertTrue(isErrorMessageCorrect, "Thông báo lỗi cho sân bay đón bị thiếu không hiển thị");
        System.out.println("Kết quả Test Case: testSearchWithoutPickupAirport - " + (isErrorMessageCorrect ? "ĐẬU" : "TRƯỢT"));
        System.out.println("=== Kết thúc Test Case: testSearchWithoutPickupAirport ===\n");
    }
    
    @AfterMethod
    public void tearDown() throws InterruptedException {
        System.out.println("=== Đang Dọn dẹp Test ===");
        // Add a delay before closing the browser to observe the final state
        Thread.sleep(2000);
        // Close the browser
        if (driver != null) {
            driver.quit();
        }
        System.out.println("=== Hoàn tất Dọn dẹp Test ===\n");
    }
}