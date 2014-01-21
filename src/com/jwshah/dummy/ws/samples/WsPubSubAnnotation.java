package com.jwshah.dummy.ws.samples;

import java.io.IOException;
import java.nio.ByteBuffer;

import javax.websocket.OnMessage;
import javax.websocket.PongMessage;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.jwshah.dummy.ws.samples.pubsub.PublicationsManager;

@ServerEndpoint("/websocket/subscribe/{topic}")
public class WsPubSubAnnotation {

    @OnMessage
    public void echoTextMessage(Session session, String msg,  @PathParam("topic") String topic) {
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

    @OnMessage
    public void echoBinaryMessage(Session session, ByteBuffer bb,
            boolean last) {
        try {
            if (session.isOpen()) {
                session.getBasicRemote().sendBinary(bb, last);
            }
        } catch (IOException e) {
            try {
                session.close();
            } catch (IOException e1) {
                // Ignore
            }
        }
    }

    /**
     * Process a received pong. This is a NO-OP.
     *
     * @param pm    Ignored.
     */
    @OnMessage
    public void echoPongMessage(PongMessage pm) {
        // NO-OP
    }
}

