package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import java.text.SimpleDateFormat;
import java.time.Duration;

public class BookingHomePage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Page locators
    private static final By DESTINATION_FIELD = By.xpath("/html/body/div[3]/div[2]/div/form/div/div[1]/div/div/div[1]/div/div/input");
    private static final By DATE_PICKER = By.xpath("/html/body/div[3]/div[2]/div/form/div/div[2]/div/div");
    private static final By SEARCH_BUTTON = By.xpath("/html/body/div[3]/div[2]/div/form/div/div[4]/button");
    private static final By GENIUS_POPUP_CLOSE_BUTTON = By.xpath("//button[@aria-label='Dismiss sign-in info.']");

    // Constructor
    public BookingHomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, (10));
    }

    // Page actions
    public void navigate() {
        Reporter.log("Navigating to Booking.com homepage", true);
        driver.get("https://www.booking.com");
        wait.until(ExpectedConditions.presenceOfElementLocated(DESTINATION_FIELD));
        handleGeniusPopup();
        Reporter.log("✓ Successfully navigated to Booking.com", true);
    }

    public void handleGeniusPopup() {
        Reporter.log("  → Checking for Genius popup...", true);
        try {
            WebElement closeButton = wait.until(ExpectedConditions.elementToBeClickable(GENIUS_POPUP_CLOSE_BUTTON));
            executeClick(closeButton);
            wait.until(ExpectedConditions.invisibilityOfElementLocated(GENIUS_POPUP_CLOSE_BUTTON));
            Reporter.log("  → ✓ Closed Genius pop-up successfully", true);
        } catch (Exception e) {
            Reporter.log("  → ⓘ Genius pop-up not found or already closed", true);
        }
    }

    public void enterDestination(String destination) {
        Reporter.log("Entering destination", true);
        WebElement destinationInput = wait.until(ExpectedConditions.elementToBeClickable(DESTINATION_FIELD));
        destinationInput.clear();
        destinationInput.sendKeys(destination);
        Reporter.log("✓ Entered destination: " + destination, true);
    }

    public void openDatePicker() {
        Reporter.log("  → Opening date picker...", true);
        WebElement datePicker = wait.until(ExpectedConditions.elementToBeClickable(DATE_PICKER));
        executeClick(datePicker);
        Reporter.log("  → ✓ Date picker opened successfully", true);
    }

    public void selectDates(String checkInDate, String checkOutDate) {
        Reporter.log("Selecting travel dates", true);
        openDatePicker();
        selectDateFromCalendar(checkInDate);
        selectDateFromCalendar(checkOutDate);
        Reporter.log("✓ Selected dates: Check-in " + checkInDate + ", Check-out " + checkOutDate, true);
    }

    private void selectDateFromCalendar(String date) {
        try {
            String formattedDate = formatCalendarDate(date);
            Reporter.log("  → Selecting date: " + formattedDate, true);
            By dateLocator = By.xpath(String.format("//span[@aria-label='%s']", formattedDate));
            WebElement dateElement = wait.until(ExpectedConditions.elementToBeClickable(dateLocator));
            executeClick(dateElement);
            Reporter.log("  → ✓ Date selected successfully", true);
        } catch (Exception e) {
            Reporter.log("  → ✗ Failed to select date: " + e.getMessage(), true);
            throw e;
        }
    }

    private String formatCalendarDate(String date) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy", java.util.Locale.US);
        try {
            return outputFormat.format(inputFormat.parse(date));
        } catch (Exception e) {
            Reporter.log("  → ✗ Failed to format date: " + e.getMessage(), true);
            return date;
        }
    }

    public BookingSearchResultsPage clickSearch() {
        Reporter.log("Submitting search", true);
        WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(SEARCH_BUTTON));
        executeClick(searchButton);
        Reporter.log("✓ Clicked Search button", true);
        return new BookingSearchResultsPage(driver);
    }

    // Utility methods
    private void executeClick(WebElement element) {
        try {
            element.click();
        } catch (WebDriverException e) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", element);
        }
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}