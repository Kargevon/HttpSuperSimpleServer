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

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import defauld.mainlaunch;

public class WebPage implements HttpHandler {

	private String path;
	
	public WebPage() {
		
	}
	
	public WebPage(String path) {
		
	}

	@Override
	public void handle(HttpExchange t) throws IOException {
		
		
	}
	
	
}
class Page implements HttpHandler {

	private String path;

	public Page(String path) {
		this.path = path;
	}

	private String postmahad(HttpExchange t) throws IOException {
	
		InputStreamReader isr = new InputStreamReader(t.getRequestBody(), "utf-8");
		BufferedReader br = new BufferedReader(isr);

		// From now on, the right way of moving from bytes to utf-8 characters:

		int b;
		StringBuilder buf = new StringBuilder(512);
		while ((b = br.read()) != -1) {
			buf.append((char) b);
		}

		br.close();
		isr.close();
		// System.out.println(buf.toString());
		String result = "";
		try {
			result = mainlaunch.dosomethingwith(buf.toString());
		} catch (SQLException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void handle(HttpExchange t) throws IOException {
		printInfo(t);
		// System.out.println(path+" "+t.getRequestMethod());

		// if (t.getRequestMethod().equals("GET")) {

		File index = new File(path);
		t.sendResponseHeaders(200, index.length());

		OutputStream outputStream = t.getResponseBody();
		Path p = index.toPath();

		Files.copy(p, outputStream);
		outputStream.close();
		t.close();
		/*
		 * } else if (t.getRequestMethod().equals("POST")) {
		 * 
		 * String response = postmahad(t); t.sendResponseHeaders(200,
		 * response.length()); OutputStream out = t.getResponseBody();
		 * out.write(response.getBytes()); }
		 */

	}
}