package testZone;

import java.util.Map;

public class SpecificPage extends HttpSuperSimpleServer.WebPage {

	private void d() {
		
	}
	
	@Override
	protected String doSomethingWithGetRequest(Map<String, String> mp) {
		// TODO Auto-generated method stub
		return "dodiki!";
	}
	@Override
	protected String doSomethingWithPostRequest(Map<String, String> mp) {
		// TODO Auto-generated method stub
		return super.doSomethingWithPostRequest(mp);
	}
	
}

