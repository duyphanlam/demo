package tests;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.*;
import pages.BookingHomePage;
import pages.BookingSearchResultsPage;

public class BookingSearchTest extends BaseTest {

    @Test(priority = 1)
    public void testSuccessfulSearch() {
        Reporter.log("=== TEST CASE 1: SUCCESSFUL SEARCH ===", true);
        Reporter.log("Input parameters:", true);
        Reporter.log("- Destination: Hanoi", true);
        Reporter.log("- Check-in date: 2025-04-25", true);
        Reporter.log("- Check-out date: 2025-04-30", true);
        
        Reporter.log("\nTest Steps:", true);
        
        // Initialize pages
        BookingHomePage homePage = new BookingHomePage(driver);
        
        // Execute test steps
        homePage.navigate();
        homePage.enterDestination("Hanoi");
        homePage.selectDates("2025-04-25", "2025-04-30");
        BookingSearchResultsPage resultsPage = homePage.clickSearch();
        
        Reporter.log("\nExpected Result:", true);
        Reporter.log("- Search results page loads successfully", true);
        Reporter.log("- URL contains destination and selected dates", true);
        
        // Verify results
        verifySearchResults(resultsPage, "Hanoi", "2025-04-25", "2025-04-30");
        Reporter.log("\n=== TEST CASE 1 COMPLETED SUCCESSFULLY ===\n", true);
    }

    @Test(priority = 2)
    public void testEmptyDestination() {
        Reporter.log("=== TEST CASE 2: EMPTY DESTINATION VALIDATION ===", true);
        Reporter.log("Input parameters:", true);
        Reporter.log("- Destination: <empty>", true);
        Reporter.log("- Check-in date: 2025-04-25", true);
        Reporter.log("- Check-out date: 2025-04-30", true);
        
        Reporter.log("\nTest Steps:", true);
        
        // Initialize pages
        BookingHomePage homePage = new BookingHomePage(driver);
        
        // Execute test steps
        homePage.navigate();
        homePage.selectDates("2025-03-30", "2025-03-31");
        homePage.clickSearch();
        
        Reporter.log("\nExpected Result:", true);
        Reporter.log("- User remains on homepage", true);
        Reporter.log("- No navigation to search results page", true);
        
        // Verify results
        verifyNoNavigation(homePage);
        Reporter.log("\n=== TEST CASE 2 COMPLETED SUCCESSFULLY ===\n", true);
    }

    private void verifySearchResults(BookingSearchResultsPage resultsPage, String destination, String checkInDate, String checkOutDate) {
        Reporter.log("Verifying search results...", true);
        String currentUrl = resultsPage.getCurrentUrl();
        
        Reporter.log("Actual Results:", true);
        Reporter.log("- Current URL: " + currentUrl, true);
        
        boolean searchResultsLoaded = resultsPage.isSearchResultsPageLoaded();
        boolean containsCheckInDate = checkInDate != null && resultsPage.urlContainsDate(checkInDate);
        boolean containsCheckOutDate = checkOutDate != null && resultsPage.urlContainsDate(checkOutDate);
        
        Assert.assertTrue(searchResultsLoaded, "Search results page not loaded");
        Reporter.log("- Search results page loaded: " + (searchResultsLoaded ? "✓ PASS" : "✗ FAIL"), true);
        
        if (checkInDate != null) {
            Assert.assertTrue(containsCheckInDate, "URL does not contain check-in date: " + checkInDate);
            Reporter.log("- URL contains check-in date: " + (containsCheckInDate ? "✓ PASS" : "✗ FAIL"), true);
        }
        
        if (checkOutDate != null) {
            Assert.assertTrue(containsCheckOutDate, "URL does not contain check-out date: " + checkOutDate);
            Reporter.log("- URL contains check-out date: " + (containsCheckOutDate ? "✓ PASS" : "✗ FAIL"), true);
        }
        
        Reporter.log("-> Verification completed: All search result criteria passed for destination: " + destination, true);
    }

    private void verifyNoNavigation(BookingHomePage homePage) {
        Reporter.log("Verifying no navigation occurred...", true);
        String currentUrl = homePage.getCurrentUrl();
        
        Reporter.log("Actual Results:", true);
        Reporter.log("- Current URL: " + currentUrl, true);
        
        boolean stayedOnHomepage = currentUrl.equals("https://www.booking.com/") || currentUrl.contains("booking.com/index");
        
        Assert.assertTrue(stayedOnHomepage, "Unexpected navigation occurred with empty destination");
        Reporter.log("- Stayed on homepage: " + (stayedOnHomepage ? "✓ PASS" : "✗ FAIL"), true);
        Reporter.log("-> Verification completed: No navigation occurred with empty destination", true);
    }
}