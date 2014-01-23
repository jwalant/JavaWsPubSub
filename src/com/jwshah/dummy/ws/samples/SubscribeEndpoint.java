package com.jwshah.dummy.ws.samples;

import java.io.IOException;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.jwshah.dummy.ws.samples.publications.PublicationsManager;

@ServerEndpoint("/websocket/subscribe/{topic}")
public class SubscribeEndpoint {

    @OnMessage
    public void handleSubscribeMessage(Session session, String msg,  @PathParam("topic") String topic) {
        {
            if (session.isOpen()) {
            	if(topic!=null && !topic.trim().isEmpty()){
	            	System.out.println("We have a clinet for: " + topic);
	            	PublicationsManager.getInstance().onSubscribe(session,topic.trim());
	             }
            	else{
            		try {
						session.getBasicRemote().sendText("Please use a valid topic name to subscribe");
					} catch (IOException e) {
						//Ignore topic was null anyways cannot do much about this client
					}
            	}
            }
        } 
    }
}

