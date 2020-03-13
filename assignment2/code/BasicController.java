//package testcrawler;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import java.util.Set;
import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;

import java.io.*;

public class BasicController extends WebCrawler{

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg|png|mp3|mp4|zip|gz|pdf))$");
    public static FileWriter fw;
    public static BufferedWriter br;
    public static HashMap<String, Integer> urlsMap = new HashMap<String, Integer>();
    
    static String[] listOfWords;
    static int numOfPages;
    static String seedUrl;
    
    private static ArrayList<String> resultUrl = new ArrayList<String>();
    private static ArrayList<String> resultUrlTitle = new ArrayList<String>();
    private static ArrayList<Integer> resultUrlWeight = new ArrayList<Integer>();
    private int minWeightIndex = -1;
    
    
    public static void getInput(String[] low, int nop, String seed){
    	listOfWords = low;
    	numOfPages = nop;
    	seedUrl = seed;
    	urlsMap.put(seedUrl, 0);
    	
    	System.out.println(Arrays.toString(listOfWords));
    	System.out.println(seedUrl);
    	
    }
    
    public static void showResult(){
    	System.out.println("=====================Search Result========================");
    	for(int i=0;i<resultUrl.size();i++){
    		System.out.println(resultUrlTitle.get(i));
    		System.out.println("url: "+resultUrl.get(i));
    		System.out.println("weight: "+resultUrlWeight.get(i));
    		System.out.println("------------------------------------------------------");
    	}
    }
    
    
//    public static void fileWriterInit(){
//    	try{
//    		fw = new FileWriter("C:\\Users\\nirameroda\\Documents\\crawler-data\\testout.txt");
//    		br = new BufferedWriter(fw);
//
//    	}
//    	catch(Exception e){
//    		e.printStackTrace();
//    	}
//    }
//    
//    public static void fileWriterClose(){
//    	try{
//    		br.close();
//    	}
//    	catch(Exception e){
//    		e.printStackTrace();
//    	}
//    }
    
    
    // for multi threaded crawler
    private final List<String> myCrawlDomains;

    public BasicController(List<String> myCrawlDomains) {
        this.myCrawlDomains = ImmutableList.copyOf(myCrawlDomains);
    }
    
    
    
    
    /**
     * Specify whether the given url should be crawled or not based on
     * the crawling logic. Here URLs with extensions css, js etc will not be visited
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
    	
    	String referringPageUrl = referringPage.getWebURL().getURL();
        System.out.println("URL of referring page: " + referringPageUrl);
  
        
        
        System.out.println("shouldVisit: " + url.getURL().toLowerCase());
        String href = url.getURL().toLowerCase();
        boolean result = (!FILTERS.matcher(href).matches() && href.startsWith(seedUrl));
        
        String shouldVisitDcsn = " ";
        if(result){
        	System.out.println("URL Should Visit");
        	shouldVisitDcsn = "URL Should Visit";
        }
        else{
        	//System.out.println("URL Should not Visit");
        	shouldVisitDcsn = "URL Should not Visit";
        }
        //System.out.println("-----------------|||||||------------------");
        
//        try{
//        	br.write("URL of referring page: " + referringPageUrl+"\n");
//        	br.write("shouldVisit: " + url.getURL().toLowerCase()+ "\n") ;
//        	br.write(shouldVisitDcsn+"\n");
//        	br.write("--------------------|||||||-------------------\n");
//        }
//        catch(Exception e){
//        	e.printStackTrace();
//        }

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
        
        String text="", html="";
        int linkSize=0;

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();      
            text = htmlParseData.getText().toLowerCase(); //extract text from page
            html = htmlParseData.getHtml(); //extract html from page
            Set<WebURL> links = htmlParseData.getOutgoingUrls();
            linkSize = links.size();
            
            
            String titleContent = htmlParseData.getTitle().toLowerCase();
            System.out.println("title: "+titleContent);
            int weightCount = 0;
            for(int i=0;i<listOfWords.length;i++){
            	if(StringUtils.countMatches(titleContent, listOfWords[i])>0)
            		weightCount += StringUtils.countMatches(titleContent, listOfWords[i]);
            	if(StringUtils.countMatches(text, listOfWords[i])>0)
            		weightCount += StringUtils.countMatches(text, listOfWords[i]);
            }
            
            if(weightCount>0 && resultUrl.size()<10){
            	resultUrl.add(url);
            	resultUrlTitle.add(htmlParseData.getTitle());
            	resultUrlWeight.add(weightCount);
            	
            	int min = Integer.MAX_VALUE;
        		int min_index = -1;
            	for(int j=0;j<resultUrlWeight.size();j++){
            		if(resultUrlWeight.get(j)<min){
            			min = resultUrlWeight.get(j);
            			min_index = j;
            		}
            	}
            	minWeightIndex = min_index;
            }
            if(weightCount>0 && resultUrl.size()==10){
            	int minWeightCount = resultUrlWeight.get(minWeightIndex);
            	if(weightCount>minWeightCount){
            		resultUrl.remove(minWeightIndex);
            		resultUrlTitle.remove(minWeightIndex);
            		resultUrlWeight.remove(minWeightIndex);
            		
            		resultUrl.add(url);
            		resultUrlTitle.add(htmlParseData.getTitle());
            		resultUrlWeight.add(weightCount);
            		
            		int min = Integer.MAX_VALUE;
            		int min_index = -1;
                	for(int j=0;j<resultUrlWeight.size();j++){
                		if(resultUrlWeight.get(j)<min){
                			min = resultUrlWeight.get(j);
                			min_index = j;
                		}
                	}
                	minWeightIndex = min_index;
            	}
            }
            System.out.println("Size: "+resultUrl.size());
            
//            
//            System.out.println("---------------------------------------------------------");
//            System.out.println("Page URL: " + url);
//            System.out.println("Text length: " + text.length());
//            System.out.println("Html length: " + html.length());
//            System.out.println("Number of outgoing links: " + links.size());
//            System.out.println("---------------------------------------------------------");

            //if required write content to file
        }
        
//        try{
//        	br.write("\n--------------------------------------------------\n");
//        	br.write("Page URL: "+url+" -- "+"Text length: "+text.length()+" -- "+"Number of outgoing links: "+linkSize+"\n");
//        	br.write("--------------------------------------------------\n");
//        }
//        catch(Exception e){
//        	e.printStackTrace();
//        }
    }
}
