import java.awt.List;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;
import javax.swing.text.Document;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;

public class DFS_Crawling {
	private static LinkedBlockingQueue<String> toCrawl_URLs = new LinkedBlockingQueue<String>();
	private static String base_URL;
	private static String HTTPSbase_URL;
	private static HashSet<String> alreadyCrawledSet = new HashSet<String>();
	private static LinkedList<String> deadLinks = new LinkedList<String>();

	public static void main(String[] args) throws IOException, InterruptedException {

	    // should output a site map, showing the static assets for each page. 

	   // Validate.isTrue(args.length == 1, "usage: supply url to fetch");

	    base_URL = "https://snap.stanford.edu/";
	    //base_URL = "https://www.ssaurel.com/blog";
	    HTTPSbase_URL = base_URL.replace("http://", "https://");

	    alreadyCrawledSet.add(base_URL);
	    toCrawl_URLs.add(base_URL);

	    while (!toCrawl_URLs.isEmpty() ) {
	        String url = toCrawl_URLs.take();
	        crawlURL(url);
	    }
	    showResults();

	}

	private static void crawlURL(String url) throws IOException, InterruptedException {
	   // print("%s", url);
	    System.out.println("\n***Site Crawled:"+url+"***");
	    try {
	    	org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
	        org.jsoup.select.Elements links = doc.select("a[href]");


	        for (org.jsoup.nodes.Element link : links) {
	            String linkURL = link.attr("abs:href");
	            if (!isalreadyCrawled(linkURL)) {
	                alreadyCrawledSet.add(linkURL);
	                toCrawl_URLs.put(linkURL);
	            }
	        }
	    } catch (HttpStatusException e) {
	        deadLinks.add(url);
	    }
	}   

	private static boolean isalreadyCrawled(String url) {
	    if (alreadyCrawledSet.contains(url)) {
	        return true;
	    } else {
	        return false;
	    }
	}
	public static void showResults() {
		System.out.println("\nResuls :");
		System.out.println("Web sites crawled:"+alreadyCrawledSet.size()+"\n");
		//for(String s:alreadyCrawledSet) {
			//System.out.println("**"+s);
		//}
		
	}
}
