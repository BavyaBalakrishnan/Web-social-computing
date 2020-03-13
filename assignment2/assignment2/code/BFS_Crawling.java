import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Pattern;

import ch.qos.logback.core.boolex.Matcher;

public class BFS_Crawling {
	public static Queue<String> queue= new LinkedList<>();
	public static Set<String> marked=new HashSet<>();
	public static String regex="http[s]*://(\\w+\\.)*(\\w+)";
	public static void bfsAlgorithm(String root) throws IOException{
		queue.add(root);
		while(!queue.isEmpty()) {
			String crawledUrl=queue.poll();
			System.out.println("\n***Site crawled:"+crawledUrl+"***");
			if(marked.size()>100)
				return;
			boolean ok=false;
			URL url=null;
			BufferedReader br=null;
			while(!ok) {
				try {
					url=new URL(crawledUrl);
					br=new BufferedReader(new InputStreamReader(url.openStream()));
					ok=true;
				}catch (MalformedURLException e) {
					System.out.println("**Malformed URL**"+crawledUrl);
					crawledUrl=queue.poll();
					ok=false;
					
				}catch (IOException ioe) {
					System.out.println("**IOException for URL**"+crawledUrl);
					crawledUrl=queue.poll();
					ok=false;
				}
			}
			StringBuilder sb=new StringBuilder();
			
			while((crawledUrl=br.readLine())!=null){
				sb.append(crawledUrl);
				
			}
			crawledUrl=sb.toString();
			Pattern pattern=Pattern.compile(regex);
			java.util.regex.Matcher matcher=pattern.matcher(crawledUrl);
			while(matcher.find()) {
				String m=matcher.group();
				if(!marked.contains(m)) {
					marked.add(m);
					System.out.println("Site added for Crawling:"+m);
					queue.add(m);
				}
				
			}
			
			
		}
	}
			
	public static void showResults() {
		System.out.println("\nResuls :");
		System.out.println("Web sites crawled:"+marked.size()+"\n");
		for(String s:marked) {
			System.out.println("**"+s);
		}
		
	}


	public static void main(String[] args) {
		try {
			//bfsAlgorithm("https://www.ssaurel.com/blog");
			bfsAlgorithm("https://snap.stanford.edu/");
			showResults();
			
		}catch(IOException e) {
			
		}
	}
}