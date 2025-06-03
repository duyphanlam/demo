package DOAN;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DeleteUser {
    WebDriver driver;
    WebDriverWait wait;
    private String USERNAME;
    private String PASSWORD;

    @BeforeClass
    public void setUp() {
        System.out.println("=== Khởi động trình duyệt ===");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--allow-insecure-localhost");
        options.setAcceptInsecureCerts(true);

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        String BASE_URL = "https://cntttest.vanlanguni.edu.vn:18081/Phancong02";
        driver.get(BASE_URL);
        System.out.println("🔹 Mở trang web: " + BASE_URL);

        // Handle SSL certificate warning if present
        try {
            WebElement advancedButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("details-button")));
            advancedButton.click();
            WebElement proceedLink = wait.until(ExpectedConditions.elementToBeClickable(By.id("proceed-link")));
            proceedLink.click();
        } catch (Exception e) {
            System.out.println("⏳ Không tìm thấy cảnh báo SSL hoặc đã vượt qua cảnh báo: " + e.getMessage());
        }

        // Read credentials from CSV file
        readCredentialsFromCsv();

        // Click the initial "Đăng nhập" button on the landing page
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Đăng nhập']"))).click();
            System.out.println("🔹 Đã nhấn nút 'Đăng nhập' trên trang landing.");
        } catch (Exception e) {
            System.out.println("❌ Không tìm thấy nút 'Đăng nhập' ban đầu: " + e.getMessage());
            Assert.fail("❌ Không tìm thấy nút 'Đăng nhập' ban đầu: " + e.getMessage());
        }

        // Enter email and click Next
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='email']"))).clear();
            driver.findElement(By.xpath("//input[@type='email']")).sendKeys(USERNAME);
            System.out.println("🔹 Đã nhập email: " + USERNAME);
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='Next']"))).click();
            System.out.println("🔹 Đã nhấn nút Next");
        } catch (Exception e) {
            System.out.println("❌ Không thể nhập email hoặc nhấn Next: " + e.getMessage());
            Assert.fail("❌ Không thể nhập email hoặc nhấn Next: " + e.getMessage());
        }

        // Enter password and click Sign in
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='password']"))).clear();
            driver.findElement(By.xpath("//input[@type='password']")).sendKeys(PASSWORD);
            System.out.println("🔹 Đã nhập mật khẩu");
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='Sign in']"))).click();
            System.out.println("🔹 Đã nhấn nút Đăng nhập");
        } catch (Exception e) {
            System.out.println("❌ Không thể nhập mật khẩu hoặc nhấn Sign in: " + e.getMessage());
            Assert.fail("❌ Không thể nhập mật khẩu hoặc nhấn Sign in: " + e.getMessage());
        }

        // Handle "Stay signed in?" prompt
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.id("idSIButton9"))).click();
            System.out.println("🔹 Đã nhấn 'Yes' trên prompt 'Stay signed in?'");
        } catch (Exception e) {
            System.out.println("❌ Không tìm thấy prompt 'Stay signed in?': " + e.getMessage());
        }

        // Verify successful login by checking the welcome message
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/div[2]/div[3]/div/section/div/div/div/h3")));
            System.out.println("✅ Đăng nhập thành công! Đã tìm thấy thông báo chào mừng.");
        } catch (Exception e) {
            System.out.println("❌ Đăng nhập không thành công: " + e.getMessage());
            Assert.fail("❌ Đăng nhập không thành công: " + e.getMessage());
        }

        // Navigate to "Người dùng" menu
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div[1]/div[2]/ul/li[3]/a"))).click();
            System.out.println("🔹 Đã nhấp vào mục 'Người dùng'.");
        } catch (Exception e) {
            System.out.println("❌ Không thể nhấp vào mục 'Người dùng': " + e.getMessage());
            Assert.fail("❌ Không thể nhấp vào mục 'Người dùng': " + e.getMessage());
        }
    }

    private void readCredentialsFromCsv() {
        String csvFilePath = "C:\\credentials.csv";
        File csvFile = new File(csvFilePath);
        if (!csvFile.exists()) {
            throw new RuntimeException("CSV file not found at path: " + csvFile.getAbsolutePath());
        }
        if (!csvFile.canRead()) {
            throw new RuntimeException("Cannot read CSV file (permission denied): " + csvFile.getAbsolutePath());
        }
        try (CSVReader csvReader = new CSVReader(new FileReader(csvFile))) {
            String[] headers = csvReader.readNext();
            if (headers == null) {
                throw new RuntimeException("CSV file is empty: " + csvFile.getAbsolutePath());
            }
            if (headers.length > 0 && headers[0].startsWith("\uFEFF")) {
                headers[0] = headers[0].substring(1);
            }
            if (headers.length < 2 || !headers[0].trim().equalsIgnoreCase("username") || !headers[1].trim().equalsIgnoreCase("password")) {
                throw new RuntimeException("Invalid CSV format: Expected headers 'username,password', but found: " + String.join(",", headers));
            }
            String[] credentials = csvReader.readNext();
            if (credentials == null || credentials.length < 2) {
                throw new RuntimeException("No credentials found in CSV file: " + csvFile.getAbsolutePath());
            }
            USERNAME = credentials[0].trim();
            PASSWORD = credentials[1].trim();
            if (USERNAME.isEmpty() || PASSWORD.isEmpty()) {
                throw new RuntimeException("Username or password is empty in CSV file: " + csvFile.getAbsolutePath());
            }
            System.out.println("🔹 Đã đọc thông tin đăng nhập từ file CSV: username=" + USERNAME);
        } catch (IOException e) {
            System.err.println("❌ Không thể đọc file CSV: " + e.getMessage());
            throw new RuntimeException("Không thể đọc file CSV: " + e.getMessage(), e);
        } catch (CsvValidationException e) {
            System.err.println("❌ Lỗi khi đọc file CSV: " + e.getMessage());
            throw new RuntimeException("Lỗi khi đọc file CSV: " + e.getMessage(), e);
        }
    }

//    @Test(priority = 19)
//    public void testDeleteUserSuccessfully() throws InterruptedException {
//        System.out.println("\n=== TC_19: Xóa người dùng thành công ===");
//        
//        // Click on trash icon for the first user row
//        System.out.println("🔹 Nhấn nút xóa (icon thùng rác) của người dùng ");
//        WebElement deleteButton = wait.until(ExpectedConditions.elementToBeClickable(
//                By.xpath("/html/body/div[2]/div[2]/div[3]/div/section/div[2]/div[2]/div/div/table/tbody/tr[1]/td[7]/a[2]/i")));
//        deleteButton.click();
//        Thread.sleep(1000);
//        
//        // Verify confirmation dialog appears
//        System.out.println("🔹 Kiểm tra hiển thị dialog xác nhận xóa");
//        WebElement confirmDialog = wait.until(ExpectedConditions.visibilityOfElementLocated(
//                By.xpath("/html/body/div[4]/div")));
//        
//        String confirmMessage = driver.findElement(By.xpath("/html/body/div[4]/div/div[2]")).getText();
//        System.out.println("🔹 Nội dung xác nhận: " + confirmMessage);
//        Assert.assertTrue(confirmMessage.contains("Bạn có chắc muốn xoá người dùng vlu.27262@vanlanguni.vn này?"), 
//                "❌ Dialog không hiển thị đúng thông báo xác nhận!");
//        
//        // Click "Xóa" button to confirm deletion
//        System.out.println("🔹 Nhấn nút 'Xóa' để xác nhận");
//        WebElement confirmDeleteButton = wait.until(ExpectedConditions.elementToBeClickable(
//                By.xpath("/html/body/div[4]/div/div[6]/button[1]")));
//        confirmDeleteButton.click();
//        Thread.sleep(2000);
//        
//        // Verify success message
//        System.out.println("🔹 Kiểm tra thông báo xóa thành công");
//        try {
//            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
//            WebElement successMessage = shortWait.until(ExpectedConditions.visibilityOfElementLocated(
//                    By.xpath("//*[@id='toast-container']/div/div")));
//            String message = successMessage.getText();
//            System.out.println("🔹 Nội dung thông báo: " + message);
//            Assert.assertTrue(message.contains("Xóa thành công"), 
//                    "❌ Không hiển thị thông báo xóa thành công! Tìm thấy: " + message);
//            System.out.println("✅ Xóa người dùng thành công!");
//        } catch (Exception e) {
//            System.out.println("❌ Không tìm thấy thông báo xóa thành công: " + e.getMessage());
//            Assert.fail("Không thể xác minh thông báo xóa thành công!");
//        }
//    }

    @Test(priority = 19)
    public void testDeleteUserUnsuccessfully() throws InterruptedException {
        System.out.println("\n=== TC_19: Xóa người dùng không thành công ===");
        
        // Find a lecturer that has been assigned in the system
        // For this test, we'll try with the second user in the list
        System.out.println("🔹 Nhấn nút xóa (icon thùng rác) của người dùng thứ hai");
        WebElement deleteButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("/html/body/div[2]/div[2]/div[3]/div/section/div[2]/div[2]/div/div/table/tbody/tr[2]/td[7]/a[2]/i")));
        deleteButton.click();
        Thread.sleep(1000);
        
        // Verify confirmation dialog appears
        System.out.println("🔹 Kiểm tra hiển thị dialog xác nhận xóa");
        WebElement confirmDialog = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("/html/body/div[3]/div")));
        
        String confirmMessage = driver.findElement(By.xpath("/html/body/div[3]/div/div[2]")).getText();
        System.out.println("🔹 Nội dung xác nhận: " + confirmMessage);
        Assert.assertTrue(confirmMessage.contains("Bạn có chắc muốn xoá người dùng này?"), 
                "❌ Dialog không hiển thị đúng thông báo xác nhận!");
        
        // Click "Xóa" button to confirm deletion
        System.out.println("🔹 Nhấn nút 'Xóa' để xác nhận");
        WebElement confirmDeleteButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("/html/body/div[3]/div/div[6]/button[1]")));
        confirmDeleteButton.click();
        Thread.sleep(2000);
        
        // Verify error message
        System.out.println("🔹 Kiểm tra thông báo lỗi khi xóa không thành công");
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement errorMessage = shortWait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("/html/body/div[3]/div")));
            String message = errorMessage.getText();
            System.out.println("🔹 Nội dung thông báo lỗi: " + message);
            Assert.assertTrue(message.contains("Không thể xoá do giảng viên này đã được phân công trong hệ thống!"), 
                    "❌ Không hiển thị thông báo lỗi! Tìm thấy: " + message);
            System.out.println("✅ Hiển thị thông báo lỗi khi xóa giảng viên đã được phân công!");
        } catch (Exception e) {
            System.out.println("❌ Không tìm thấy thông báo lỗi: " + e.getMessage());
            Assert.fail("Không thể xác minh thông báo lỗi khi xóa không thành công!");
        }
    }



    @AfterClass
    public void tearDown() {
        System.out.println("\n=== Kết thúc: Đóng trình duyệt ===");
        if (driver != null) {
            System.out.println("🔹 Trình duyệt sẽ được giữ mở để bạn kiểm tra kết quả.");
            System.out.println("🔹 Nhấn Enter để đóng trình duyệt khi bạn hoàn tất...");
            new java.util.Scanner(System.in).nextLine();
            driver.quit();
            System.out.println("✅ Đã đóng trình duyệt.");
        }
    }

}
