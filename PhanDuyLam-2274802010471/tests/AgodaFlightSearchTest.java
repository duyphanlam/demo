package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.FlightSearchPage;

public class AgodaFlightSearchTest {

    WebDriver driver;
    FlightSearchPage flightSearchPage;

    @BeforeMethod
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.agoda.com/vi-vn");
        flightSearchPage = new FlightSearchPage(driver);
        flightSearchPage.navigateToFlightTab();
    }

    @Test(priority = 1)
    public void testValidFlightSearch() throws InterruptedException {
        flightSearchPage.enterDeparture("Hà Nội");
        flightSearchPage.enterDestination("Hồ Chí Minh");
        flightSearchPage.openDepartureCalendar();
        flightSearchPage.selectDepartureDate25April2025();
        flightSearchPage.closeDepartureCalendarTest1();
        flightSearchPage.openReturnCalendar();
        flightSearchPage.selectReturnDate27April2025();
        flightSearchPage.clickSearchButton();

        Assert.assertTrue(flightSearchPage.isSearchResultsDisplayed() || 
                flightSearchPage.getCurrentUrl().contains("flights") || 
                flightSearchPage.getCurrentUrl().contains("search"),
                "Expected to be on search results page");
        System.out.println("Test 1: Valid flight search - PASS");
    }

    @Test(priority = 2)
    public void testEmptyDepartureLocation() throws InterruptedException {
        flightSearchPage.enterDestination("Hồ Chí Minh");
        flightSearchPage.openDepartureCalendar();
        flightSearchPage.selectDepartureDate25April2025();
        flightSearchPage.closeDepartureCalendarTest2();
        flightSearchPage.openReturnCalendar();
        flightSearchPage.selectReturnDate27April2025();
        flightSearchPage.clickSearchButton();

        boolean hasValidationError = flightSearchPage.isValidationErrorDisplayedTest2();
        Assert.assertTrue(hasValidationError, "Expected validation error for empty departure");
        System.out.println("Test 2: Empty departure location - PASS");
    }

    @Test(priority = 3)
    public void testEmptyDestinationLocation() throws InterruptedException {
        flightSearchPage.enterDeparture("Hà Nội");
        flightSearchPage.closeDepartureFocusTest3();
        flightSearchPage.openDepartureCalendar();
        flightSearchPage.selectDepartureDate25April2025();
        flightSearchPage.closeDepartureCalendarTest3();
        flightSearchPage.openReturnCalendar();
        flightSearchPage.selectReturnDate27April2025();
        flightSearchPage.clickSearchButton();

        boolean hasValidationError = flightSearchPage.isValidationErrorDisplayedTest3();
        Assert.assertTrue(hasValidationError, "Expected validation error for empty destination");
        System.out.println("Test 3: Empty destination location - PASS");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}