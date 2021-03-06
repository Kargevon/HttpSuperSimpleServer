package HttpSuperSimpleServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;



public class WebPage implements HttpHandler {

	private final boolean DEBUG = true;
	
	private String path;
	
	public WebPage() {
		
	}
	
	public WebPage(String path) {
		this.path=path;
	}

	protected String doSomethingWithGetRequest(Map<String, String> mp) {
		return "";
	}
	protected String doSomethingWithPostRequest(Map<String, String> mp) {
		return "";
	}
	
	private String POSTmanager(HttpExchange t) throws IOException {
		InputStreamReader isr = new InputStreamReader(t.getRequestBody(), "utf-8");
		BufferedReader br = new BufferedReader(isr);

		

		int b;
		StringBuilder buf = new StringBuilder(512);
		while ((b = br.read()) != -1) {
			buf.append((char) b);
		}

		br.close();
		isr.close();
		Map<String, String> mp = queryToMap(buf.toString());
	
		return doSomethingWithPostRequest(mp);
		
		
	}
	private String GETmanager(HttpExchange t) {
		Map<String, String> mp = queryToMap(t.getRequestURI().getQuery());
		
		
		
		return doSomethingWithGetRequest(mp);
	}
	
	@Override
	public void handle(HttpExchange t) throws IOException {
		
		printInfo(t);
		String response ="";
		if(t.getRequestMethod().equals("POST")) {
			
		//???????????? ???? ??????
			response=POSTmanager(t);
		}
		else if(t.getRequestMethod().equals("GET")) {
			//???????????? ??? ??????
			response=GETmanager(t);
		}
		
		OutputStream outputStream = t.getResponseBody();
		
		if(response.length()>0) {
			
			if(DEBUG)System.out.println("Response: "+response);
			//???? ????? ??????????? ?????, ?.?. ??????? ????????? ???????? ??? ???????, ?? ?????????? ??, ?? ??? ???????
			t.sendResponseHeaders(200, response.length());
			outputStream.write(response.getBytes());
		}else {
			//????? ??????? ???????? ????? ??
			if(DEBUG)System.out.println(path);
		File index = new File(path);
		t.sendResponseHeaders(200, index.length());
		Path p = index.toPath();
		Files.copy(p, outputStream);
		}
		outputStream.close();
		t.close();
		
	}
	protected static void printInfo(HttpExchange t) {

		String adr = t.getRemoteAddress().getAddress().toString();
		String prot = t.getProtocol();
		String uri = t.getRequestURI().toASCIIString();
		String meth = t.getRequestMethod();
		String code = t.getResponseCode() + "";
		System.out.println(
				"|adr: " + adr + " |prot: " + prot + " |uri: " + uri + " |method: " + meth + " |code: " + code + " |");

	}
	private Map<String, String> queryToMap(String query) {
	    if(query == null) {
	        return null;
	    }
	    Map<String, String> result = new HashMap<>();
	    for (String param : query.split("&")) {
	        String[] entry = param.split("=");
	        if (entry.length > 1) {
	            result.put(entry[0], entry[1]);
	        }else{
	            result.put(entry[0], "");
	        }
	    }
	    return result;
	}
}

