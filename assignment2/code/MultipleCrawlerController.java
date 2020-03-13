

import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class MultipleCrawlerController {
    private static final Logger logger = LoggerFactory.getLogger(MultipleCrawlerController.class);

    public static void main(String[] args) throws Exception {
        // The folder where intermediate crawl data is stored (e.g. list of urls that are extracted from previously
        // fetched pages and need to be crawled later).
        String crawlStorageFolder = "C:\\Users\\nirameroda\\Documents\\crawler-data";
        
      //user input
        String url = "";
    	String[] listOfWords;
    	int maxNumOfPages = 10;
    	
    	Scanner sc = new Scanner(System.in);
    	
    	System.out.println("Enter url: ");
    	url = sc.nextLine();
    	System.out.println("Enter words space separated: \n");
    	String temp = sc.nextLine();
    	listOfWords = temp.split(" ");
    	System.out.println("Enter max number of pages to fetch: ");
    	maxNumOfPages = sc.nextInt();
    	sc.close();
        // end of user input
        
    //	BasicCrawler.fileWriterInit();
        BasicController.getInput(listOfWords, maxNumOfPages, url);

        CrawlConfig config1 = new CrawlConfig();
        //CrawlConfig config2 = new CrawlConfig();

        // The two crawlers should have different storage folders for their intermediate data.
        config1.setCrawlStorageFolder(crawlStorageFolder);
        //config2.setCrawlStorageFolder(crawlStorageFolder + "/crawler2");

        config1.setPolitenessDelay(100);
        //config2.setPolitenessDelay(2000);

        config1.setMaxPagesToFetch(maxNumOfPages);
        //config2.setMaxPagesToFetch(100);

        // We will use different PageFetchers for the two crawlers.
        PageFetcher pageFetcher1 = new PageFetcher(config1);
        //PageFetcher pageFetcher2 = new PageFetcher(config2);

        // We will use the same RobotstxtServer for both of the crawlers.
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher1);

        CrawlController controller1 = new CrawlController(config1, pageFetcher1, robotstxtServer);
        //CrawlController controller2 = new CrawlController(config2, pageFetcher2, robotstxtServer);

        List<String> crawler1Domains = ImmutableList.of(url);
        //List<String> crawler2Domains = ImmutableList.of("ht");

        controller1.addSeed(url);
        //controller1.addSeed("https://www.cnn.com/");
        //controller1.addSeed("https://www.ics.uci.edu/~lopes/");
        //controller1.addSeed("https://www.cnn.com/POLITICS/");

        //controller2.addSeed("https://en.wikipedia.org/wiki/Main_Page");
        //controller2.addSeed("https://en.wikipedia.org/wiki/Obama");
        //controller2.addSeed("https://en.wikipedia.org/wiki/Bing");

        CrawlController.WebCrawlerFactory<BasicController> factory1 = () -> new BasicController(crawler1Domains);
        //CrawlController.WebCrawlerFactory<BasicCrawler> factory2 = () -> new BasicCrawler(crawler2Domains);

        // The first crawler will have 5 concurrent threads and the second crawler will have 10 threads.
        long timeStart = System.currentTimeMillis();
        //controller1.startNonBlocking(factory1, 5);
        controller1.startNonBlocking(factory1, 3);
        //controller2.startNonBlocking(factory2, 7);
        long timeMid = System.currentTimeMillis();
        controller1.waitUntilFinish();
        long timeEnd = System.currentTimeMillis();
        logger.info("Crawler is finished.");
        System.out.println("Time start to mid: "+((timeMid-timeStart)/1000)+" seconds.");
        System.out.println("Time start to end: "+((timeEnd-timeStart)/1000)+" seconds.");
        //controller2.waitUntilFinish();
        //logger.info("Crawler 2 is finished.");
        //BasicCrawler.fileWriterClose();
        BasicController.showResult();
    }
}
