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

public class UpdateUserTest {
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

    @Test(priority = 16)
    public void testUpdateUserSuccessfully() throws InterruptedException {
        System.out.println("\n=== TC_16: Cập nhật thông tin người dùng thành công ===");
        System.out.println("🔹 Nhấn nút Chỉnh sửa của người dùng đầu tiên");
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[3]/div/section/div[2]/div[2]/div/div/table/tbody/tr[1]/td[7]/a[1]")).click();
        Thread.sleep(2000);

        // Cập nhật thông tin người dùng
        System.out.println("🔹 Nhập Mã giảng viên: lsmphen");
        driver.findElement(By.id("staff_id")).clear();
        driver.findElement(By.id("staff_id")).sendKeys("lsmphen");
        Thread.sleep(1000);

        System.out.println("🔹 Nhập Tên giảng viên: phenn10");
        driver.findElement(By.id("full_name")).clear();
        driver.findElement(By.id("full_name")).sendKeys("phenn10");
        Thread.sleep(1000);

        System.out.println("🔹 Nhập Email: phenn10.updated@vanlanguni.vn");
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys("phenn10.updated@vanlanguni.vn");
        Thread.sleep(1000);

        System.out.println("🔹 Chọn Loại giảng viên: Cơ hữu");
        WebElement lecturerTypeDropdown = driver.findElement(By.xpath("//select[@id='type']/following::span[contains(@class, 'select2')]"));
        lecturerTypeDropdown.click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[contains(text(), 'Cơ hữu')]"))).click();
        Thread.sleep(1000);

        System.out.println("🔹 Chọn Role: Giảng viên");
        WebElement roleDropdown = driver.findElement(By.xpath("//select[@id='role_id']/following::span[contains(@class, 'select2')]"));
        roleDropdown.click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[contains(text(), 'Giảng viên')]"))).click();
        Thread.sleep(1000);

        // Nhấn Lưu
        System.out.println("🔹 Nhấn nút Lưu");
        driver.findElement(By.xpath("/html/body/div[3]/div[2]/form/div[7]/button[2]")).click();
        Thread.sleep(2000);

        // Verify the popup message
        System.out.println("🔹 Kiểm tra thông báo popup");
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
            WebElement popup = shortWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"toast-container\"]/div/div")));
            String popupMessage = popup.getText();
            System.out.println("🔹 Nội dung popup: " + popupMessage);
            String expectedMessage = "Cập nhật thành công";
            Assert.assertTrue(popupMessage.contains(expectedMessage), "❌ Popup không chứa thông báo thành công! Tìm thấy: " + popupMessage);
            System.out.println("✅ Popup hiển thị thông báo thành công!");
        } catch (Exception e) {
            System.out.println("❌ Không thể tìm thấy popup hoặc popup đã biến mất: " + e.getMessage());
            Assert.fail("Không thể xác minh thông báo popup sau khi lưu!");
        }
    }

    @Test(priority = 17)
    public void testCancelUpdateUser() throws InterruptedException {
        System.out.println("\n=== TC_17: Người dùng bấm nút Hủy ===");
        System.out.println("🔹 Nhấn nút Chỉnh sửa của người dùng đầu tiên");
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[3]/div/section/div[2]/div[2]/div/div/table/tbody/tr[1]/td[7]/a[1]")).click();
        Thread.sleep(2000);

        // Lưu giá trị ban đầu
        WebElement idField = driver.findElement(By.id("staff_id"));
        String originalId = idField.getAttribute("value");
        System.out.println("🔹 Giá trị ban đầu Mã giảng viên: " + originalId);

        WebElement nameField = driver.findElement(By.id("full_name"));
        String originalName = nameField.getAttribute("value");
        System.out.println("🔹 Giá trị ban đầu Tên giảng viên: " + originalName);

        WebElement emailField = driver.findElement(By.id("email"));
        String originalEmail = emailField.getAttribute("value");
        System.out.println("🔹 Giá trị ban đầu Email: " + originalEmail);

        // Cập nhật thông tin
        System.out.println("🔹 Nhập Mã giảng viên: TEMP_ID");
        driver.findElement(By.id("staff_id")).clear();
        driver.findElement(By.id("staff_id")).sendKeys("TEMP_ID");
        Thread.sleep(1000);

        System.out.println("🔹 Nhập Tên giảng viên: Temp Name");
        driver.findElement(By.id("full_name")).clear();
        driver.findElement(By.id("full_name")).sendKeys("Temp Name");
        Thread.sleep(1000);

        System.out.println("🔹 Nhập Email: temp.email@vanlanguni.vn");
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys("temp.email@vanlanguni.vn");
        Thread.sleep(1000);

        System.out.println("🔹 Chọn Loại giảng viên: Cơ hữu");
        WebElement lecturerTypeDropdown = driver.findElement(By.xpath("//select[@id='type']/following::span[contains(@class, 'select2')]"));
        lecturerTypeDropdown.click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[contains(text(), 'Cơ hữu')]"))).click();
        Thread.sleep(1000);

        System.out.println("🔹 Chọn Role: Giảng viên");
        WebElement roleDropdown = driver.findElement(By.xpath("//select[@id='role_id']/following::span[contains(@class, 'select2')]"));
        roleDropdown.click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[contains(text(), 'Giảng viên')]"))).click();
        Thread.sleep(1000);

        // Nhấn Hủy
        System.out.println("🔹 Nhấn nút Hủy");
        driver.findElement(By.xpath("/html/body/div[3]/div[2]/form/div[7]/button[1]")).click();
        Thread.sleep(2000);

        // Mở lại form để kiểm tra
        System.out.println("🔹 Nhấn lại nút Chỉnh sửa để kiểm tra");
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[3]/div/section/div[2]/div[2]/div/div/table/tbody/tr[1]/td[7]/a[1]")).click();
        Thread.sleep(2000);

        // Kiểm tra giá trị không thay đổi
        System.out.println("🔹 Kiểm tra giá trị sau khi hủy");
        idField = driver.findElement(By.id("staff_id"));
        String currentId = idField.getAttribute("value");
        System.out.println("🔹 Giá trị hiện tại Mã giảng viên: " + currentId);
        Assert.assertEquals(currentId, originalId, "❌ Mã giảng viên đã thay đổi sau khi hủy!");

        nameField = driver.findElement(By.id("full_name"));
        String currentName = nameField.getAttribute("value");
        System.out.println("🔹 Giá trị hiện tại Tên giảng viên: " + currentName);
        Assert.assertEquals(currentName, originalName, "❌ Tên giảng viên đã thay đổi sau khi hủy!");

        emailField = driver.findElement(By.id("email"));
        String currentEmail = emailField.getAttribute("value");
        System.out.println("🔹 Giá trị hiện tại Email: " + currentEmail);
        Assert.assertEquals(currentEmail, originalEmail, "❌ Email đã thay đổi sau khi hủy!");

        System.out.println("✅ Giá trị không thay đổi sau khi hủy!");

        // Đóng modal
        System.out.println("🔹 Đóng modal");
        driver.findElement(By.xpath("/html/body/div[3]/div[1]/button")).click();
        Thread.sleep(2000);
    }

    @Test(priority = 18)
    public void testUpdateUserWithExistingLecturerId() throws InterruptedException {
        System.out.println("\n=== TC_18: Cập nhật người dùng không thành công vì mã giảng viên đã tồn tại ===");
        System.out.println("🔹 Nhấn nút Chỉnh sửa của người dùng đầu tiên");
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[3]/div/section/div[2]/div[2]/div/div/table/tbody/tr[1]/td[7]/a[1]")).click();
        Thread.sleep(2000);

        // Cập nhật thông tin với mã giảng viên đã tồn tại
        System.out.println("🔹 Nhập Mã giảng viên: 3333 (đã tồn tại)");
        driver.findElement(By.id("staff_id")).clear();
        driver.findElement(By.id("staff_id")).sendKeys("3333");
        Thread.sleep(1000);

        System.out.println("🔹 Nhập Tên giảng viên: Nguyen Van Test");
        driver.findElement(By.id("full_name")).clear();
        driver.findElement(By.id("full_name")).sendKeys("Nguyen Van Test");
        Thread.sleep(1000);

        System.out.println("🔹 Nhập Email: nguyen.test@vanlanguni.vn");
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys("nguyen.test@vanlanguni.vn");
        Thread.sleep(1000);

        System.out.println("🔹 Chọn Loại giảng viên: Cơ hữu");
        WebElement lecturerTypeDropdown = driver.findElement(By.xpath("//select[@id='type']/following::span[contains(@class, 'select2')]"));
        lecturerTypeDropdown.click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[contains(text(), 'Cơ hữu')]"))).click();
        Thread.sleep(1000);

        System.out.println("🔹 Chọn Role: Giảng viên");
        WebElement roleDropdown = driver.findElement(By.xpath("//select[@id='role_id']/following::span[contains(@class, 'select2')]"));
        roleDropdown.click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[contains(text(), 'Giảng viên')]"))).click();
        Thread.sleep(1000);

        // Nhấn Lưu
        System.out.println("🔹 Nhấn nút Lưu");
        driver.findElement(By.xpath("/html/body/div[3]/div[2]/form/div[7]/button[2]")).click();
        Thread.sleep(2000);

        // Verify the error popup
        System.out.println("🔹 Kiểm tra thông báo lỗi popup");
        try {
            WebDriverWait modalWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement errorMessageElement = modalWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[4]/div")));
            String errorMessage = errorMessageElement.getText();
            System.out.println("🔹 Nội dung thông báo lỗi: " + errorMessage);
            String expectedErrorMessage = "Mã giảng viên này đã có trong hệ thống";
            Assert.assertTrue(errorMessage.contains(expectedErrorMessage), "❌ Modal dialog không chứa thông báo lỗi! Tìm thấy: " + errorMessage);
            System.out.println("✅ Modal dialog hiển thị thông báo lỗi!");

            // Close the popup
            System.out.println("🔹 Nhấn nút OK để đóng popup");
            driver.findElement(By.xpath("/html/body/div[4]/div/div[6]/button[1]")).click();
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("❌ Không thể tìm thấy modal dialog hoặc thông báo lỗi: " + e.getMessage());
            Assert.fail("Không thể xác minh thông báo lỗi trong modal dialog sau khi lưu!");
        }

        // Close the modal
        System.out.println("🔹 Đóng modal");
        driver.findElement(By.xpath("/html/body/div[3]/div[1]/button")).click();
        Thread.sleep(2000);
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