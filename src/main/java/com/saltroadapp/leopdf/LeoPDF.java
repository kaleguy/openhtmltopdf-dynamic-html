package com.saltroadapp.leopdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.openhtmltopdf.svgsupport.BatikSVGDrawer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import static org.apache.commons.io.FileUtils.writeStringToFile;

public class LeoPDF {
    public static void main(String[] args) {
        OutputStream os = null;
        try {
            os = new FileOutputStream("./out/out.pdf");
        }  catch (Exception e) {
            System.out.println(e);
            System.exit(1);
        }
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.useSVGDrawer(new BatikSVGDrawer()); // for charts

        String headerHtml = "<html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"styles.css\"/>";

        // set up chrome
        String chromeDriverPath = "chromedriver";
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        System.setProperty("webdriver.chrome.logfile", "chromedriver.log");
        System.setProperty("webdriver.chrome.verboseLogging", "true");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--disable-gpu", "--window-size-1920,1200", "--ignore-certificate-errors", "--silent");
        WebDriver driver = new ChromeDriver(options);

        // load the index.html file into chrome
        String workingDir = System.getProperty("user.dir");
        driver.get("file://" + workingDir + "\\reports\\index.html");
        String pageTitle = driver.getTitle();
        System.out.println("page Title: " + pageTitle);

        System.out.println("============= BROWSER LOGS =================");
        LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
        for (LogEntry entry: logEntries) {
            System.out.println(new Date(entry.getTimestamp())
                    + " " + entry.getLevel()
                    + " " + entry.getMessage()
                    + " " + entry.toString());
        }

        // Now get the html from the page loaded by Chrome. This will be different from the html
        // in the static file, because scripts will have been run, including scripts to load data
        // from JSON, build charts, etc.
        String html = driver.findElement(By.className("report")).getAttribute("outerHTML");
        String bookmarks = driver.findElement(By.className("bookmarks")).getAttribute("outerHTML");
        System.out.println(bookmarks);
        html = headerHtml + bookmarks + "</head><body>" + html + "</body></html>";

        // Write the Html to a temp file
        File in = new File("in/temp.html");
        try {
            writeStringToFile(in, html);
        } catch (Exception e) {
            System.out.println(e);
        }

        // Run PDF Builder on the temp file
        builder.withFile(in);
        builder.toStream(os);
        try {
            builder.run();
        } catch (Exception e) {
            System.out.println(e);
        }
        driver.close();
        driver.quit();



    }
}
