import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

public class FindBrokenLinksExample {
	
	private WebDriver driver;
	private int invalidLinksCount;

	@Before
	public void setUp() {

		driver = new FirefoxDriver();
		//System.setProperty("webdriver.gecko.driver","//Users//cuneytcakir//Downloads//geckodriver");
		driver.get("http://www.ntv.com.tr/");
		
        
	}

	@Test
	public void validateInvalidLinks() {

		try {
			
			invalidLinksCount = 0;
			List<WebElement> anchorTagsList = driver.findElements(By
					.tagName("a"));
			System.out.println("Total no. of links are "
					+ anchorTagsList.size());
			WriteValidExcelFile objExcelFile = new WriteValidExcelFile();
			for (WebElement anchorTagElement : anchorTagsList) {
				if (anchorTagElement != null) {
					String url = anchorTagElement.getAttribute("href");
					if (url != null && !url.contains("javascript")) {
						verifyURLStatus(url);						
						objExcelFile.writeExcel(System.getProperty("user.dir"),"NTVURL.xlsx","valid", url);
				       	System.out.println(url);			        
					} else {
						invalidLinksCount++;
						objExcelFile.writeExcel(System.getProperty("user.dir"),"NTVURL.xlsx","invalid", url);
						System.out.println(url);
					}
				}
			}

			System.out.println("Total no. of invalid links are "
					+ invalidLinksCount);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	@After
	public void tearDown() {
		if (driver != null)
			driver.quit();
	}

	public void verifyURLStatus(String URL) {

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(URL);
		try {
			HttpResponse response = client.execute(request);
			// verifying response code and The HttpStatus should be 200 if not,
			// increment invalid link count
			////We can also check for 404 status code like response.getStatusLine().getStatusCode() == 404
			if (response.getStatusLine().getStatusCode() != 200)
				invalidLinksCount++;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}