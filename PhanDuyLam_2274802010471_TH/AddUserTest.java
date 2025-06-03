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

public class AddUserTest {
    WebDriver driver;
    WebDriverWait wait;

    // Locators (Using original absolute XPaths as requested)
    private By initialLoginButton = By.xpath("//button[text()='Đăng nhập']");
    private By emailField = By.xpath("//input[@type='email']");
    private By nextButton = By.xpath("//input[@value='Next']");
    private By passwordField = By.xpath("//input[@type='password']");
    private By loginButton = By.xpath("//input[@value='Sign in']");
    private By welcomeMessage = By.xpath("/html/body/div[2]/div[2]/div[3]/div/section/div/div/div/h3");
    private By userManagementMenu = By.xpath("/html/body/div[2]/div[1]/div[2]/ul/li[3]/a");

    private String USERNAME; // Will be populated from CSV
    private String PASSWORD; // Will be populated from CSV

    @BeforeClass
    public void setUp() {
        System.out.println("=== Khởi động trình duyệt ===");
        // Set up Chrome options to handle SSL errors
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--allow-insecure-localhost");
        options.setAcceptInsecureCerts(true);

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Navigate to the testing site
        String BASE_URL = "https://cntttest.vanlanguni.edu.vn:18081/Phancong02";
        driver.get(BASE_URL);
        System.out.println("Mở trang web: " + BASE_URL);

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
            wait.until(ExpectedConditions.elementToBeClickable(initialLoginButton)).click();
            System.out.println("🔹 Đã nhấn nút 'Đăng nhập' trên trang landing.");
        } catch (Exception e) {
            System.out.println("❌ Không tìm thấy nút 'Đăng nhập' ban đầu: " + e.getMessage());
            Assert.fail("❌ Không tìm thấy nút 'Đăng nhập' ban đầu: " + e.getMessage());
        }

        // Enter email and click Next
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(emailField)).clear();
            driver.findElement(emailField).sendKeys(USERNAME);
            System.out.println("🔹 Đã nhập email: " + USERNAME);
            wait.until(ExpectedConditions.elementToBeClickable(nextButton)).click();
            System.out.println("🔹 Đã nhấn nút Next");
        } catch (Exception e) {
            System.out.println("❌ Không thể nhập email hoặc nhấn Next: " + e.getMessage());
            Assert.fail("❌ Không thể nhập email hoặc nhấn Next: " + e.getMessage());
        }

        // Enter password and click Sign in
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField)).clear();
            driver.findElement(passwordField).sendKeys(PASSWORD);
            System.out.println("🔹 Đã nhập mật khẩu");
            wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
            System.out.println("🔹 Đã nhấn nút Đăng nhập");
        } catch (Exception e) {
            System.out.println("❌ Không thể nhập mật khẩu hoặc nhấn Sign in: " + e.getMessage());
            Assert.fail("❌ Không thể nhập mật khẩu hoặc nhấn Sign in: " + e.getMessage());
        }

        // Verify successful login by checking the welcome message
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(welcomeMessage));
            System.out.println("✅ Đăng nhập thành công! Đã tìm thấy thông báo chào mừng.");
        } catch (Exception e) {
            System.out.println("❌ Đăng nhập không thành công: " + e.getMessage());
            Assert.fail("❌ Đăng nhập không thành công: " + e.getMessage());
        }

        // Navigate to "Người dùng" menu
        try {
            wait.until(ExpectedConditions.elementToBeClickable(userManagementMenu)).click();
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

    private void displayResult(String testCase, String actual, String expected) {
        System.out.println("---------------- TEST CASE " + testCase + " ----------------");
        System.out.println("🔹 Hiển thị: " + actual);
        System.out.println("🔹 Mong đợi: " + expected);
        Assert.assertTrue(actual.equals(expected), "❌ Test case '" + testCase + "' failed: Expected '" + expected + "' but got '" + actual + "'");
        System.out.println("✅ Check: PASSED ✓");
        System.out.println("------------------------------------------------");
    }

    

    @Test(priority = 9)
    public void testAddUserSuccessfully() throws InterruptedException {
        System.out.println("\n=== TC_09: Thêm người dùng thành công ===");
        System.out.println("🔹 Nhấn nút 'Thêm người dùng'");
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[3]/div/section/div[2]/div[2]/div/div/div[1]/div[2]/div/div[2]/button")).click();
        Thread.sleep(2000);

        // Nhập thông tin người dùng
        String uniqueId = "LEC" + System.currentTimeMillis();
        System.out.println("🔹 Nhập Mã giảng viên: " + uniqueId);
        driver.findElement(By.id("staff_id")).clear();
        driver.findElement(By.id("staff_id")).sendKeys(uniqueId);
        Thread.sleep(1000);

        System.out.println("🔹 Nhập Tên giảng viên: Nguyen Van Test");
        driver.findElement(By.id("full_name")).clear();
        driver.findElement(By.id("full_name")).sendKeys("Nguyen Van Test");
        Thread.sleep(1000);

        String email = "nguyen.test" + System.currentTimeMillis() + "@vanlanguni.vn";
        System.out.println("🔹 Nhập Email: " + email);
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys(email);
        Thread.sleep(1000);

        System.out.println("🔹 Chọn Loại giảng viên: Giảng viên cơ hữu");
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
        driver.findElement(By.xpath("//button[text()='Lưu']")).click();
        Thread.sleep(2000);

        // Verify the popup message
        System.out.println("🔹 Kiểm tra thông báo popup");
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
            WebElement popup = shortWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"toast-container\"]/div/div")));
            String popupMessage = popup.getText();
            System.out.println("🔹 Nội dung popup: " + popupMessage);
            String expectedMessage = "Lưu thành công";
            Assert.assertTrue(popupMessage.contains(expectedMessage), "❌ Popup không chứa thông báo thành công! Tìm thấy: " + popupMessage);
            System.out.println("✅ Popup hiển thị thông báo thành công!");
        } catch (Exception e) {
            System.out.println("❌ Không thể tìm thấy popup hoặc popup đã biến mất: " + e.getMessage());
            Assert.fail("Không thể xác minh thông báo popup sau khi lưu!");
        }
    }

    @Test(priority = 10)
    public void testAddExistingUser() throws InterruptedException {
        System.out.println("\n=== TC_10: Thêm người dùng không thành công vì người dùng đã tồn tại ===");
        System.out.println("🔹 Nhấn nút 'Thêm người dùng'");
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[3]/div/section/div[2]/div[2]/div/div/div[1]/div[2]/div/div[2]/button")).click();
        Thread.sleep(2000);

        // Nhập thông tin người dùng với ID đã tồn tại
        System.out.println("🔹 Nhập Mã giảng viên: 3333 (đã tồn tại)");
        driver.findElement(By.id("staff_id")).clear();
        driver.findElement(By.id("staff_id")).sendKeys("3333");
        Thread.sleep(1000);

        System.out.println("🔹 Nhập Tên giảng viên: Nguyen Van B");
        driver.findElement(By.id("full_name")).clear();
        driver.findElement(By.id("full_name")).sendKeys("Nguyen Van B");
        Thread.sleep(1000);

        System.out.println("🔹 Nhập Email: nguyenvanb@vanlanguni.vn");
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys("nguyenvanb@vanlanguni.vn");
        Thread.sleep(1000);

        System.out.println("🔹 Chọn Loại giảng viên: Giảng viên cơ hữu");
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
        driver.findElement(By.xpath("//button[text()='Lưu']")).click();
        Thread.sleep(2000);

        // Verify the error popup
        System.out.println("🔹 Kiểm tra thông báo lỗi popup");
        try {
            WebDriverWait modalWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement errorMessageElement = modalWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[4]/div")));
            String errorMessage = errorMessageElement.getText();
            System.out.println("🔹 Nội dung thông báo lỗi: " + errorMessage);
            String expectedErrorMessage = "Người dùng đã có trong hệ thống!";
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
        driver.findElement(By.id("btnClose")).click();
        Thread.sleep(2000);
    }

    @Test(priority = 11)
    public void testAddUserEmptyLecturerCode() throws InterruptedException {
        System.out.println("\n=== TC_11: Thêm người dùng không thành công vì để trống mã giảng viên ===");
        System.out.println("🔹 Nhấn nút 'Thêm người dùng'");
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[3]/div/section/div[2]/div[2]/div/div/div[1]/div[2]/div/div[2]/button")).click();
        Thread.sleep(2000);

        // Nhập thông tin, bỏ trống mã giảng viên
        System.out.println("🔹 Nhập Tên giảng viên: Nguyen Van C");
        driver.findElement(By.id("full_name")).clear();
        driver.findElement(By.id("full_name")).sendKeys("Nguyen Van C");
        Thread.sleep(1000);

        System.out.println("🔹 Nhập Email: nguyenvanc@vanlanguni.vn");
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys("nguyenvanc@vanlanguni.vn");
        Thread.sleep(1000);

        System.out.println("🔹 Chọn Loại giảng viên: Giảng viên cơ hữu");
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
        driver.findElement(By.xpath("//button[text()='Lưu']")).click();
        Thread.sleep(2000);

        // Verify the validation error
        System.out.println("🔹 Kiểm tra thông báo lỗi validation");
        try {
            WebDriverWait errorWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement errorMessageElement = errorWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), 'Bạn chưa nhập mã giảng viên')]")));
            String errorMessage = errorMessageElement.getText();
            System.out.println("🔹 Nội dung thông báo lỗi: " + errorMessage);
            String expectedErrorMessage = "Bạn chưa nhập mã giảng viên";
            Assert.assertTrue(errorMessage.contains(expectedErrorMessage), "❌ Không tìm thấy thông báo lỗi validation! Tìm thấy: " + errorMessage);
            System.out.println("✅ Hiển thị thông báo lỗi validation!");
        } catch (Exception e) {
            System.out.println("❌ Không thể tìm thấy thông báo lỗi validation: " + e.getMessage());
            Assert.fail("Không thể xác minh thông báo lỗi validation!");
        }

        // Close the modal
        System.out.println("🔹 Đóng modal");
        driver.findElement(By.id("btnClose")).click();
        Thread.sleep(2000);
    }

    @Test(priority = 12)
    public void testAddUserEmptyLecturerName() throws InterruptedException {
        System.out.println("\n=== TC_12: Thêm người dùng không thành công vì để trống tên giảng viên ===");
        System.out.println("🔹 Nhấn nút 'Thêm người dùng'");
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[3]/div/section/div[2]/div[2]/div/div/div[1]/div[2]/div/div[2]/button")).click();
        Thread.sleep(2000);

        // Nhập thông tin, bỏ trống tên giảng viên
        String uniqueId = "LEC" + System.currentTimeMillis();
        System.out.println("🔹 Nhập Mã giảng viên: " + uniqueId);
        driver.findElement(By.id("staff_id")).clear();
        driver.findElement(By.id("staff_id")).sendKeys(uniqueId);
        Thread.sleep(1000);

        System.out.println("🔹 Nhập Email: nguyenvand@vanlanguni.vn");
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys("nguyenvand@vanlanguni.vn");
        Thread.sleep(1000);

        System.out.println("🔹 Chọn Loại giảng viên: Giảng viên cơ hữu");
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
        driver.findElement(By.xpath("//button[text()='Lưu']")).click();
        Thread.sleep(2000);

        // Verify the validation error
        System.out.println("🔹 Kiểm tra thông báo lỗi validation");
        try {
            WebDriverWait errorWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement errorMessageElement = errorWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), 'Bạn chưa nhập tên giảng viên')]")));
            String errorMessage = errorMessageElement.getText();
            System.out.println("🔹 Nội dung thông báo lỗi: " + errorMessage);
            String expectedErrorMessage = "Bạn chưa nhập tên giảng viên";
            Assert.assertTrue(errorMessage.contains(expectedErrorMessage), "❌ Không tìm thấy thông báo lỗi validation! Tìm thấy: " + errorMessage);
            System.out.println("✅ Hiển thị thông báo lỗi validation!");
        } catch (Exception e) {
            System.out.println("❌ Không thể tìm thấy thông báo lỗi validation: " + e.getMessage());
            Assert.fail("Không thể xác minh thông báo lỗi validation!");
        }

        // Close the modal
        System.out.println("🔹 Đóng modal");
        driver.findElement(By.id("btnClose")).click();
        Thread.sleep(2000);
    }

    @Test(priority = 13)
    public void testAddUserInvalidEmail() throws InterruptedException {
        System.out.println("\n=== TC_13: Thêm người dùng không thành công vì email không hợp lệ ===");
        System.out.println("🔹 Nhấn nút 'Thêm người dùng'");
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[3]/div/section/div[2]/div[2]/div/div/div[1]/div[2]/div/div[2]/button")).click();
        Thread.sleep(2000);

        // Nhập thông tin, dùng email không hợp lệ
        String uniqueId = "LEC" + System.currentTimeMillis();
        System.out.println("🔹 Nhập Mã giảng viên: " + uniqueId);
        driver.findElement(By.id("staff_id")).clear();
        driver.findElement(By.id("staff_id")).sendKeys(uniqueId);
        Thread.sleep(1000);

        System.out.println("🔹 Nhập Tên giảng viên: Nguyen Van E");
        driver.findElement(By.id("full_name")).clear();
        driver.findElement(By.id("full_name")).sendKeys("Nguyen Van E");
        Thread.sleep(1000);

        System.out.println("🔹 Nhập Email: invalid_email");
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys("invalid_email");
        Thread.sleep(1000);

        // Nhấn Lưu
        System.out.println("🔹 Nhấn nút Lưu");
        driver.findElement(By.xpath("//button[text()='Lưu']")).click();
        Thread.sleep(2000);

        // Verify the validation error
        System.out.println("🔹 Kiểm tra thông báo lỗi validation");
        try {
            WebDriverWait errorWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement errorMessageElement = errorWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), 'Vui lòng nhập email Văn Lang hợp lệ')]")));
            String errorMessage = errorMessageElement.getText();
            System.out.println("🔹 Nội dung thông báo lỗi: " + errorMessage);
            String expectedErrorMessage = "Vui lòng nhập email Văn Lang hợp lệ";
            Assert.assertTrue(errorMessage.contains(expectedErrorMessage), "❌ Không tìm thấy thông báo lỗi validation! Tìm thấy: " + errorMessage);
            System.out.println("✅ Hiển thị thông báo lỗi validation!");
        } catch (Exception e) {
            System.out.println("❌ Không thể tìm thấy thông báo lỗi validation: " + e.getMessage());
            Assert.fail("Không thể xác minh thông báo lỗi validation!");
        }

        // Close the modal
        System.out.println("🔹 Đóng modal");
        driver.findElement(By.id("btnClose")).click();
        Thread.sleep(2000);
    }

    @Test(priority = 14)
    public void testAddUserNoLecturerType() throws InterruptedException {
        System.out.println("\n=== TC_14: Thêm người dùng không thành công vì chưa chọn loại giảng viên ===");
        System.out.println("🔹 Quay lại trang 'Người dùng'");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div[1]/div[2]/ul/li[3]/a"))).click();
        Thread.sleep(2000);

        System.out.println("🔹 Nhấn nút 'Thêm người dùng'");
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[3]/div/section/div[2]/div[2]/div/div/div[1]/div[2]/div/div[2]/button")).click();
        Thread.sleep(2000);

        // Nhập thông tin, bỏ chọn loại giảng viên
        String uniqueId = "LEC" + System.currentTimeMillis();
        System.out.println("🔹 Nhập Mã giảng viên: " + uniqueId);
        driver.findElement(By.id("staff_id")).clear();
        driver.findElement(By.id("staff_id")).sendKeys(uniqueId);
        Thread.sleep(1000);

        System.out.println("🔹 Nhập Tên giảng viên: Nguyen Van F");
        driver.findElement(By.id("full_name")).clear();
        driver.findElement(By.id("full_name")).sendKeys("Nguyen Van F");
        Thread.sleep(1000);

        System.out.println("🔹 Nhập Email: nguyenvanf@vanlanguni.vn");
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys("nguyenvanf@vanlanguni.vn");
        Thread.sleep(1000);

        System.out.println("🔹 Chọn Role: Giảng viên");
        WebElement roleDropdown = driver.findElement(By.xpath("//select[@id='role_id']/following::span[contains(@class, 'select2')]"));
        roleDropdown.click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[contains(text(), 'Giảng viên')]"))).click();
        Thread.sleep(1000);

        // Nhấn Lưu
        System.out.println("🔹 Nhấn nút Lưu");
        driver.findElement(By.xpath("//button[text()='Lưu']")).click();
        Thread.sleep(2000);

        // Verify the validation error
        System.out.println("🔹 Kiểm tra thông báo lỗi validation");
        try {
            WebDriverWait errorWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement errorMessageElement = errorWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), 'Bạn chưa chọn loại giảng viên')]")));
            String errorMessage = errorMessageElement.getText();
            System.out.println("🔹 Nội dung thông báo lỗi: " + errorMessage);
            String expectedErrorMessage = "Bạn chưa chọn loại giảng viên";
            Assert.assertTrue(errorMessage.contains(expectedErrorMessage), "❌ Không tìm thấy thông báo lỗi validation! Tìm thấy: " + errorMessage);
            System.out.println("✅ Hiển thị thông báo lỗi validation!");
        } catch (Exception e) {
            System.out.println("❌ Không thể tìm thấy thông báo lỗi validation: " + e.getMessage());
            Assert.fail("Không thể xác minh thông báo lỗi validation!");
        }

        // Close the modal
        System.out.println("🔹 Đóng modal");
        driver.findElement(By.id("btnClose")).click();
        Thread.sleep(2000);
    }

    @Test(priority = 15)
    public void testAddUserNoRole() throws InterruptedException {
        System.out.println("\n=== TC_15: Thêm người dùng không thành công vì chưa chọn role ===");
        System.out.println("🔹 Nhấn nút 'Thêm người dùng'");
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[3]/div/section/div[2]/div[2]/div/div/div[1]/div[2]/div/div[2]/button")).click();
        Thread.sleep(2000);

        // Nhập thông tin, bỏ chọn role
        String uniqueId = "LEC" + System.currentTimeMillis();
        System.out.println("🔹 Nhập Mã giảng viên: " + uniqueId);
        driver.findElement(By.id("staff_id")).clear();
        driver.findElement(By.id("staff_id")).sendKeys(uniqueId);
        Thread.sleep(1000);

        System.out.println("🔹 Nhập Tên giảng viên: Nguyen Van G");
        driver.findElement(By.id("full_name")).clear();
        driver.findElement(By.id("full_name")).sendKeys("Nguyen Van G");
        Thread.sleep(1000);

        System.out.println("🔹 Nhập Email: nguyenvang@vanlanguni.vn");
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys("nguyenvang@vanlanguni.vn");
        Thread.sleep(1000);

        System.out.println("🔹 Chọn Loại giảng viên: Giảng viên cơ hữu");
        WebElement lecturerTypeDropdown = driver.findElement(By.xpath("//select[@id='type']/following::span[contains(@class, 'select2')]"));
        lecturerTypeDropdown.click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[contains(text(), 'Cơ hữu')]"))).click();
        Thread.sleep(1000);

        // Nhấn Lưu
        System.out.println("🔹 Nhấn nút Lưu");
        driver.findElement(By.xpath("//button[text()='Lưu']")).click();
        Thread.sleep(2000);

        // Verify the validation error
        System.out.println("🔹 Kiểm tra thông báo lỗi validation");
        try {
            WebDriverWait errorWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement errorMessageElement = errorWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), 'Bạn chưa chọn role')]")));
            String errorMessage = errorMessageElement.getText();
            System.out.println("🔹 Nội dung thông báo lỗi: " + errorMessage);
            String expectedErrorMessage = "Bạn chưa chọn role";
            Assert.assertTrue(errorMessage.contains(expectedErrorMessage), "❌ Không tìm thấy thông báo lỗi validation! Tìm thấy: " + errorMessage);
            System.out.println("✅ Hiển thị thông báo lỗi validation!");
        } catch (Exception e) {
            System.out.println("❌ Không thể tìm thấy thông báo lỗi validation: " + e.getMessage());
            Assert.fail("Không thể xác minh thông báo lỗi validation!");
        }

        // Close the modal
        System.out.println("🔹 Đóng modal");
        driver.findElement(By.id("btnClose")).click();
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