package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AirportTransferPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators (XPaths) as provided in the original code
    private final By airportTransferTab = By.xpath("//*[@id=\"tab-journey-tab\"]/div/div[1]");
    private final By pickupAirportField = By.xpath("//input[contains(@placeholder,'Sân bay đón khách')]");
    private final By pickupAirportSuggestion = By.xpath("//*[@id=\"autocompleteSearch\"]/div/div/ul/li[1]");
    private final By destinationField = By.xpath("//input[contains(@placeholder,'Địa điểm đến')]");
    private final By destinationSuggestion = By.xpath("//*[@id=\"autocompleteSearch\"]/div/div/ul/li[1]");
    private final By datePicker = By.xpath("//*[@id=\"journey-calendar\"]/div/div/div");
    private final By nextMonthButton = By.xpath("//*[@id=\"DatePicker__AccessibleV2\"]/div/div[2]/div[2]/div[1]");
    private final By date25April2025 = By.xpath("//*[@id=\"DatePicker__AccessibleV2\"]/div/div[2]/div[2]/div[3]/div[4]/div[5]");
    private final By searchButton = By.xpath("//*[@id=\"Tabs-Container\"]/button");

    // Constructor
    public AirportTransferPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    // Methods to interact with the page (mirroring the original code logic)
    public void clickAirportTransferTab() throws InterruptedException {
        System.out.println("Hành động: Nhấp vào tab 'Đưa đón sân bay'");
        wait.until(ExpectedConditions.elementToBeClickable(airportTransferTab)).click();
        Thread.sleep(2000); // Delay to observe tab switch
    }

    public void enterPickupAirport(String airport) throws InterruptedException {
        System.out.println("Hành động: Nhập sân bay đón: " + airport);
        WebElement pickupField = wait.until(ExpectedConditions.elementToBeClickable(pickupAirportField));
        pickupField.click();
        Thread.sleep(2000); // Delay to observe click
        pickupField.sendKeys(airport);
        Thread.sleep(2000); // Delay to observe text input
        wait.until(ExpectedConditions.visibilityOfElementLocated(pickupAirportSuggestion)).click();
        Thread.sleep(2000); // Delay to observe selection
    }

    public void enterDestination(String destination) throws InterruptedException {
        System.out.println("Hành động: Nhập điểm đến: " + destination);
        WebElement destField = wait.until(ExpectedConditions.elementToBeClickable(destinationField));
        destField.click();
        Thread.sleep(2000); // Delay to observe click
        destField.sendKeys(destination);
        Thread.sleep(2000); // Delay to observe text input
        wait.until(ExpectedConditions.visibilityOfElementLocated(destinationSuggestion)).click();
        Thread.sleep(2000); // Delay to observe selection
    }

    public void selectDate25April2025() throws InterruptedException {
        System.out.println("Hành động: Mở lịch để chọn ngày");
        driver.findElement(datePicker).click();
        Thread.sleep(2000); // Delay to observe calendar open
        System.out.println("Hành động: Nhấp vào nút 'Next' để chuyển tháng");
        wait.until(ExpectedConditions.elementToBeClickable(nextMonthButton)).click();
        Thread.sleep(2000); // Delay to observe month change
        System.out.println("Hành động: Chọn ngày 25/04/2025");
        wait.until(ExpectedConditions.elementToBeClickable(date25April2025)).click();
        Thread.sleep(2000); // Delay to observe date selection
    }

    public void clickSearchButton() throws InterruptedException {
        System.out.println("Hành động: Nhấp vào nút 'Tìm kiếm'");
        WebElement button = driver.findElement(searchButton);
        button.click();
        Thread.sleep(2000); // Delay to observe search button click
    }
}