package server;

public class Test {
	
	public static void main(String[] args){
		Server host = new Server(55274);
		new Thread(host).start();
		
	}
	
}
