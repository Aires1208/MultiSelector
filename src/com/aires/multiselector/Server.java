package com.aires.multiselector;

import java.io.IOException;

public class Server {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		 int port = 8099;          
		 new Thread(new ServerReactor(port)).start();
	}


}
