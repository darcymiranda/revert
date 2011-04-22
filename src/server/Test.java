package server;

public class Test {
	
	public static void main(String[] args){
		Server host = new Server(19555);
		new Thread(host).start();
		
	}
	
}
