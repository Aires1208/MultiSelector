package com.aires.multiselector;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;


//import org.apache.log4j.Logger;


public class SocketReadHandler extends SocketHandler{
//	static Logger logger = Logger.getLogger(SocketReadHandler.class);
	private SelectionKey selectionKey;
	private  int BLOCK = 4096;    
	private  ByteBuffer receivebuffer = ByteBuffer.allocate(BLOCK);  
	
	public SocketReadHandler(ServerDispatcher dispatcher, ServerSocketChannel sc, Selector selector) throws IOException{
		super(dispatcher, sc, selector);
	}
	
	@Override
	public void runnerExecute(int readyKeyOps) throws IOException {
		System.out.println("Read:    ");
		// TODO Auto-generated method stub
		int count = 0;
		if (SelectionKey.OP_READ == readyKeyOps)
		{
            receivebuffer.clear();
            count = socketChannel.read(receivebuffer);   
            if (count > 0) {  
            	System.out.println("Server : Readable.");
//            	logger.debug("Server : Readable.");  
            	receivebuffer.flip();  
	            byte[] bytes = new byte[receivebuffer.remaining()];
	            receivebuffer.get(bytes);
	            String body = new String(bytes, "UTF-8"); 
	            System.out.println("Server : Receive :" + body);
//	            logger.debug("Server : Receive :" + body);  	            
	            socketChannel.register(dispatcher.getWriteSelector(), SelectionKey.OP_WRITE);
            }  
		}
	}
}

