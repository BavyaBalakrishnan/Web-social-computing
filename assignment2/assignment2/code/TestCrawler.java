import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;

import javax.swing.text.Document;

import org.jsoup.Jsoup;

public class TestCrawler extends WebCrawler{

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg|png|mp3|mp4|zip|gz))$");
    
    //private final static Pattern pattern1=Pattern.compile("github");
    private final static Pattern pattern1=Pattern.compile("^.*?(maps|landing|statistics|github).*$"); 
    /**
     * Specify whether the given url should be crawled or not based on
     * the crawling logic. Here URLs with extensions css, js etc will not be visited
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        System.out.println("shouldVisit: " + url.getURL().toLowerCase());

        String href = url.getURL().toLowerCase();
      //  Document document = Jsoup.connect(href).get();
        try {
			org.jsoup.nodes.Document doc = Jsoup.connect(href).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        boolean result = !FILTERS.matcher(href).matches();
        boolean result1 = pattern1.matcher(href).matches();
        
       //boolean result=!FILTERS.matcher(href).matches()
         //       && href.startsWith("http://www.ics.uci.edu/");
        if(result&&result1)
            System.out.println("URL Should be Visited");
        else
            System.out.println("URL Should not be Visited");

        return result;
    }

    /**
     * This function is called when a page is fetched and ready
     * to be processed by the program.
     */
    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        System.out.println("URL: " + url);

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();      
            String text = htmlParseData.getText(); //extract text from page
            String html = htmlParseData.getHtml(); //extract html from page
            String title = htmlParseData.getTitle();
            
            
            Set<WebURL> links = htmlParseData.getOutgoingUrls();

            System.out.println("---------------------------------------------------------");
            System.out.println("Title: "+ title); 
            System.out.println("Page URL: " + url);
            System.out.println("Text length: " + text.length());
            System.out.println("Html length: " + html.length());
            System.out.println("Number of outgoing links: " + links.size());
            System.out.println("---------------------------------------------------------");

            //if required write content to file
        }
    }
}