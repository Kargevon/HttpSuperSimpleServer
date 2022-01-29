package HttpSuperSimpleServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import sun.font.CreatedFontTracker;

public class Registor {
	private static void printInfo(HttpExchange t) {

		String adr = t.getRemoteAddress().getAddress().toString();
		String prot = t.getProtocol();
		String uri = t.getRequestURI().toASCIIString();
		String meth = t.getRequestMethod();
		String code = t.getResponseCode() + "";
		System.out.println(
				"|adr: " + adr + " |prot: " + prot + " |uri: " + uri + " |method: " + meth + " |code: " + code + " |");

	}

	private int port;
	private String server_folder;
	private HttpServer server;

	public Registor() {

	}

	public Registor(int port, String server_folder) {
		this.port = port;
		this.server_folder = server_folder;
	}

	public void init() {
		server = HttpServer.create(new InetSocketAddress(port), 0);
		server.setExecutor(null);
	}

	private void addContext() {

		System.out.println("Server folder: "+server_folder);
		String filelist[] = listFile(server_folder);
		
		for(int i=0; i!=filelist.length; ++i) {
			
			server.createContext(filelist[i].substring(server_folder.length()).replace('\\', '/'), new Page());
		}
		
	}

	public void one(String pathtoserverfolder) throws Exception {

		File dirhome = new File(pathtoserverfolder);
		if (!dirhome.isDirectory()) {
			throw new Exception(pathtoserverfolder + " isnt a folder");
		}

		String filelist[] = FileProcessor.listFile(pathtoserverfolder);

		HttpServer serv = HttpServer.create(new InetSocketAddress(80), 0);
		serv.createContext("/", new Page(pathtoserverfolder + "/index.html"));
		for (int i = 0; i != filelist.length; ++i) {

			filelist[i] = filelist[i].substring(pathtoserverfolder.length());
			System.out.println("Created web page: " + filelist[i].replace(File.separatorChar, '/'));

			serv.createContext(filelist[i].replace('\\', '/'), new Page(pathtoserverfolder + filelist[i]));
		}

		serv.createContext("/suck", new Man());
		serv.createContext("/dick", new SaveEmail());

		serv.setExecutor(null);
		serv.start();

	}

	class Man implements HttpHandler {

		@Override
		public void handle(HttpExchange t) throws IOException {
			printInfo(t);
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
				// start of all SHIT
				result = mainlaunch.dosomethingwith(buf.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String response = result;
			// response = response.replaceAll("�", "�");
			System.out.println("response -- " + response);
			byte[] germanBytes = response.getBytes(StandardCharsets.UTF_16);

			String asciiEncodedString = new String(germanBytes);
			response = asciiEncodedString;

			// System.out.println("response -- " + response);
			t.sendResponseHeaders(200, response.length());
			OutputStream out = t.getResponseBody();
			out.write(response.getBytes());
			out.close();
			t.close();

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

	public static String[] listFile(String path) {

		ArrayList<String> arl = new ArrayList<String>();

		walk(path, arl);

		for (String st : arl) {
			// System.out.println("finded: " + st);
		}
		String[] temp = new String[arl.size()];
		return arl.toArray(temp);
	}

	private static void walk(String path, ArrayList<String> brl) {

		File root = new File(path);
		File[] list = root.listFiles();

		if (list == null)
			return;

		for (File f : list) {
			if (f.isDirectory()) {
				walk(f.getAbsolutePath(), brl);
				// System.out.println("Dir:" + f.getAbsoluteFile());

			} else {
				// System.out.println("File:" + f.getAbsoluteFile());
				brl.add(f.getAbsolutePath());

			}
		}
	}
}
