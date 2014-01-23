package com.jwshah.dummy.ws.samples.test.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

@ClientEndpoint
public class SubscriberClient {

	private static CountDownLatch latch;

	@OnOpen
	public void onOpen(Session session) {
		System.out.println("Connection successfully opened...");
		try {
			session.getBasicRemote().sendText("I want to subscribe to topic..." );
		} catch (IOException e) {
			System.out.println("Not able to send Text Message");
		}
	}
	
//	private String getTopicFromSession(Session s){
//		URI requesturi = s.get
//		String topic = requesturi.getPath().split("topic")[1];
//		return topic;
//	}

	@OnMessage
	public void onTextMessage(String message) {
		System.out.println(message);
	}

	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		System.out.println(String.format("Session %s close because of %s", session.getId(), closeReason));
		latch.countDown();
	}

	public static void main(String[] args) throws InterruptedException {
		final String SERVER_ENDPOINT = "ws://localhost:8080/WsPubSubDummy/websocket/subscribe/test_random_1";
		javax.websocket.WebSocketContainer container = 
				javax.websocket.ContainerProvider.getWebSocketContainer();
		while(true){
			latch = new CountDownLatch(1);
			int retry = 3;
			while(retry-- > 0){
				try {
					container.connectToServer(SubscriberClient.class, 
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

}
