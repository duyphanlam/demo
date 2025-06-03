package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class FlightSearchPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By flightTab = By.xpath("//*[@id=\"tab-flight-tab\"]/div/div[1]/h6");
    private By roundTripOption = By.xpath("//*[@id=\"tabpanel-flight-tab\"]/div/div[1]/div[1]/div/div[1]/div/div/div/div[2]/button");
    private By departureField = By.xpath("//div[contains(@data-selenium,'origin-input')]//input | //input[contains(@placeholder,'Bay từ')]");
    private By departureSuggestion = By.xpath("//*[@id=\"autocompleteSearch-origin\"]/div/div/ul/li[1]/ul/li/span");
    private By destinationField = By.xpath("//div[contains(@data-selenium,'destination-input')]//input | //input[contains(@placeholder,'Bay đến')]");
    private By destinationSuggestion = By.xpath("//*[@id=\"autocompleteSearch-destination\"]/div/div/ul/li[1]/ul/li/span");
    private By departureDateField = By.xpath("//*[@id=\"dateSelector-departure\"]/div");
    private By departureDay25April2025 = By.xpath("//*[@id=\"DatePicker__AccessibleV2\"]/div/div[2]/div[2]/div[3]/div[4]/div[5]");
    private By returnDateField = By.xpath("//*[@id=\"flight-return\"]/div");
    private By returnDay27April2025 = By.xpath("//*[@id=\"DatePicker__AccessibleV2\"]/div/div[2]/div[1]/div[3]/div[4]/div[7]");
    private By searchButton = By.xpath("//*[@id=\"Tabs-Container\"]/button");
    private By searchResults = By.xpath("//*[@id=\"agoda-spa\"]/main/div[3]/div[1]/div");
    private By resultsText = By.xpath("//div[contains(text(),'Kết quả')]");
    private By bodyClick = By.xpath("//body");
    private By closeDepartureCalendarTest1 = By.xpath("//*[@id=\"tabpanel-flight-tab\"]/div/div[1]/div[2]/div[2]");
    private By closeDepartureCalendarTest2 = By.xpath("//*[@id=\"flight-return\"]/div/div/div/div");
    private By closeDepartureFocusTest3 = By.xpath("//*[@id=\"flight-departure\"]/div/div/div/div[1]");
    private By closeDepartureCalendarTest3 = By.xpath("//*[@id=\"tabpanel-flight-tab\"]/div/div[1]/div[2]/div[2]");
    private By errorMessageTest2 = By.xpath("/html/body/div[12]/div/div");
    private By errorMessageTest3 = By.xpath("/html/body/div[13]/div/div/div/div");

    // Constructor
    public FlightSearchPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, (30));
    }

    // Methods
    public void navigateToFlightTab() {
        wait.until(ExpectedConditions.elementToBeClickable(flightTab)).click();
        WebElement roundTrip = wait.until(ExpectedConditions.elementToBeClickable(roundTripOption));
        if (roundTrip.isDisplayed() && !roundTrip.isSelected()) {
            roundTrip.click();
        }
    }

    public void enterDeparture(String departure) {
        WebElement field = wait.until(ExpectedConditions.elementToBeClickable(departureField));
        field.clear();
        field.sendKeys(departure);
        wait.until(ExpectedConditions.visibilityOfElementLocated(departureSuggestion)).click();
    }

    public void enterDestination(String destination) {
        WebElement field = wait.until(ExpectedConditions.elementToBeClickable(destinationField));
        field.clear();
        field.sendKeys(destination);
        wait.until(ExpectedConditions.visibilityOfElementLocated(destinationSuggestion)).click();
    }

    public void openDepartureCalendar() {
        WebElement field = wait.until(ExpectedConditions.elementToBeClickable(departureDateField));
        ((RemoteWebDriver) driver).executeScript("arguments[0].scrollIntoView(true);", field);
        field.click();
    }

    public void selectDepartureDate25April2025() throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(departureDay25April2025)).click();
        Thread.sleep(2000);
    }

    public void closeDepartureCalendarTest1() {
        driver.findElement(closeDepartureCalendarTest1).click();
    }

    public void closeDepartureCalendarTest2() {
        driver.findElement(closeDepartureCalendarTest2).click();
    }

    public void closeDepartureFocusTest3() {
        driver.findElement(closeDepartureFocusTest3).click();
    }

    public void closeDepartureCalendarTest3() {
        driver.findElement(closeDepartureCalendarTest3).click();
    }

    public void openReturnCalendar() {
        WebElement field = wait.until(ExpectedConditions.elementToBeClickable(returnDateField));
        ((RemoteWebDriver) driver).executeScript("arguments[0].scrollIntoView(true);", field);
        field.click();
    }

    public void selectReturnDate27April2025() throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(returnDay27April2025)).click();
        Thread.sleep(2000);
    }

    public void clickSearchButton() {
        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
    }

    public boolean isSearchResultsDisplayed() {
        return wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(searchResults),
                ExpectedConditions.visibilityOfElementLocated(resultsText)
        ));
    }

    public boolean isValidationErrorDisplayedTest2() {
        return wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(errorMessageTest2)
        ));
    }

    public boolean isValidationErrorDisplayedTest3() {
        return wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(errorMessageTest3)
        ));
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}