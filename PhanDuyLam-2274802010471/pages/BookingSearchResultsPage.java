package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import java.time.Duration;

public class BookingSearchResultsPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Constructor
    public BookingSearchResultsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, (10));
    }

    // Page actions
    public boolean isSearchResultsPageLoaded() {
        try {
            wait.until(ExpectedConditions.urlContains("searchresults"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public boolean urlContainsDate(String date) {
        return driver.getCurrentUrl().contains(date);
    }
}