import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class App 
{
    public static void main(String[] args) throws Exception {

        final int MAX_CRAWL_DEPTH = 2;
        final int NUMBER_OF_CRAWELRS = 3;
        final String CRAWL_STORAGE_FOLDER = "Files/data/crawl/root";

        /*
         * Instantiate crawl config
         */
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(CRAWL_STORAGE_FOLDER);
        config.setMaxDepthOfCrawling(MAX_CRAWL_DEPTH);

        /*
         * Instantiate controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);


        /*
         * Add seed URLs
         */
        controller.addSeed("https://www.irctc.co.in/nget/train-search");
       // controller.addSeed("https://wiki.trezor.io/Welcome");
     //   controller.addSeed("https://snap.stanford.edu/");
        /*
         * Start the crawl.
         */
        controller.start(TestCrawler.class, NUMBER_OF_CRAWELRS);
    }
}
