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

	private int port;
	private String server_folder;
	private HttpServer server;

	public Registor() {

	}

	public Registor(int port, String server_folder) {
		this.port = port;
		this.server_folder = server_folder;
	}

	public void init() throws IOException {
		server = HttpServer.create(new InetSocketAddress(port), 0);
		server.setExecutor(null);
	}

	public void addSpecificContext(String URI, WebPage d) {
		System.out.println("Creatd page: "+URI);
		server.createContext(URI, d);
	}

	public void addStaticContext() {

		System.out.println("Server folder: " + server_folder);
		String filelist[] = listFile(server_folder);

		for (int i = 0; i != filelist.length; ++i) {
			System.out.println("Creatd page: "+filelist[i].substring(server_folder.length()).replace('\\', '/'));
		
			
			server.createContext(filelist[i].substring(server_folder.length()).replace('\\', '/'),
					new WebPage(filelist[i]));
		}

	}

	public void lego() {
		server.start();
	}

	public void pruu() {
		server.stop(0);

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

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the server_folder
	 */
	public String getServer_folder() {
		return server_folder;
	}

	/**
	 * @param server_folder the server_folder to set
	 */
	public void setServer_folder(String server_folder) {
		this.server_folder = server_folder;
	}
}
