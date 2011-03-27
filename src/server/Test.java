package server;

public class Test {
	
	public static void main(String[] args){
		Server host = new Server("5.44.46.25", 9999);
		new Thread(host).start();
		
	}
	
}
