package com.aires.multiselector.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client {
	private static final String HOST = "localhost";
    private static int PORT = 8099;
    private static SocketChannel socket;
    private static Client client;

    private static byte[] lock = new byte[1];
    //单例模式管理
    private Client() throws IOException{
        socket = SocketChannel.open();
        socket.connect(new InetSocketAddress(HOST, PORT));
        socket.configureBlocking(false);
    }

    public static Client getIntance(){
        synchronized(lock){
            if(client == null){
                try {
                    client = new Client();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return client;
        }
    }

    public void sendMsg(String msg){
        try {
            socket.write(ByteBuffer.wrap(msg.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String receiveMsg(){
        String msg = null;
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            StringBuffer buf = new StringBuffer();
            int count = 0;
            //不一定一次就能读满，连续读
            while((count = socket.read(buffer)) > 0){
                buf.append(new String(buffer.array(), 0, count));
            }
            //有数据
            if(buf.length() > 0){
                msg = buf.toString();
                if(buf.toString().equals("close")){
                    //不过不sleep会导致ioException的发生,因为如果这里直接关闭掉通道，在server里，
                    //该channel在read（buffer）时会发生读取异常，通过sleep一段时间，使得服务端那边的channel先关闭，客户端
                    //的channel后关闭，这样就能防止read(buffer)的ioException
                    //但是这是一种笨方法
                    //Thread.sleep(100);
                    //更好的方法是，在readBuffer中捕获异常后，手动进行关闭通道
                    socket.socket().close();
                    socket.close();
                    msg = null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }
public static void main(String args[]) {
	
	Client client=Client.getIntance();
	client.sendMsg("test");
}
}
