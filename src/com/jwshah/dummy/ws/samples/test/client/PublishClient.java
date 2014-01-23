package com.jwshah.dummy.ws.samples.test.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

@ClientEndpoint
public class PublishClient {
	
	private static CountDownLatch latch;
	private final ExecutorService pool; 
	private ClockTickService runningService;
	
	public PublishClient(){
		pool = Executors.newFixedThreadPool(1);
	}

	@OnOpen
	public void onOpen(Session session) {
		System.out.println("Connection successfully opened...");
		try {
			session.getBasicRemote().sendText("Start publishing... " + session.getRequestParameterMap().get("topic"));
			this.runningService = new ClockTickService(session);
			this.runningService.setRunning(true);
			pool.submit(this.runningService);
		} catch (IOException e) {
			System.out.println("Not able to send Text Message");
		}
	}

	@OnMessage
	public void onTextMessage(String message) {
		System.out.println(message);
	}

	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		System.out.println(String.format("Session %s close because of %s", session.getId(), closeReason));
		this.runningService.setRunning(false);
		latch.countDown();
	}

	public static void main(String[] args) throws InterruptedException {
		String topic = "test_random_1";
		final String SERVER_ENDPOINT = "ws://localhost:8080/WsPubSubDummy/websocket/publish?topic=" + topic;
		javax.websocket.WebSocketContainer container = 
				javax.websocket.ContainerProvider.getWebSocketContainer();
		while(true){
			latch = new CountDownLatch(1);
			int retry = 3;
			while(retry-- > 0){
				try {
					container.connectToServer(PublishClient.class, 
							new URI(SERVER_ENDPOINT));
					break;
				} catch (DeploymentException | IOException | URISyntaxException e) {
					e.printStackTrace();
					System.out.println(" Will try " + (3 - retry) + " more times....");
					Thread.sleep(1000);
					
				}
			}
			latch.await();
		}


	}
	
	public static String getCurrentTimeStamp(){
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss");
		String formattedDate = sdf.format(date);
		return formattedDate+" ";
	}
	
	
	
	private class ClockTickService implements Runnable{

		Session s;
		private boolean running = true;
		
		
		public boolean isRunning() {
			return running;
		}


		public void setRunning(boolean running) {
			this.running = running;
		}


		public ClockTickService(Session s){
			this.s = s;
		}
		
		
		@Override
		public void run() {
			while(isRunning()){
				try {
					s.getBasicRemote().sendText("Message:::" + getCurrentTimeStamp());
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						System.out.println("Exiting ClockTick....");
						break;
					}
				} catch (IOException e) {
					System.out.println("Not able to publish timestamp...");
				}
			}
			
		}
		
	}

}
