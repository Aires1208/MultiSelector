package com.aires.multiselector;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;


//import org.apache.log4j.Logger;


public class SocketWriteHandler extends SocketHandler{
//	static Logger logger = Logger.getLogger(SocketReadHandler.class);
	private  int BLOCK = 4096;  
	private  ByteBuffer sendbuffer = ByteBuffer.allocate(BLOCK);  
	private static int Index = 1;
	public SocketWriteHandler(ServerDispatcher dispatcher, ServerSocketChannel sc, Selector selector) throws IOException{
		super(dispatcher, sc, selector);
	}
	
	@Override
	public void runnerExecute(int readyKeyOps) throws IOException {
		// TODO Auto-generated method stub
		if (readyKeyOps == SelectionKey.OP_WRITE)
		{
			System.out.println("Server : Writable.");
//            logger.debug("Server : Writable.");  
        	String data = String.format("%d", Index);
        	byte[] req = data.getBytes();
            sendbuffer.clear();  

			System.out.println(String.format("Server : Write %s", data));

//            logger.debug(String.format("Server : Write %s", data));
                                
            sendbuffer = ByteBuffer.allocate(req.length);
			sendbuffer.put(req);
			sendbuffer.flip();        			
            socketChannel.write(sendbuffer);    
            Index++;
            socketChannel.register(dispatcher.getReadSelector(), SelectionKey.OP_READ);
		}
	}
}

