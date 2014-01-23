package com.jwshah.dummy.ws.samples;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.jwshah.dummy.ws.samples.publications.PublicationsManager;

@ServerEndpoint("/websocket/publish")
public class PublishEndpoint {

    @OnMessage
    public void handlePublisMessage(Session session, String msg, boolean last) {
       {
            if (session.isOpen()) {
            	String topicId = null;
            	
            	try{
            		topicId = (session.getRequestParameterMap().get("topic")==null) ? null : session.getRequestParameterMap().get("topic").get(0);
            		if(topicId != null){
            			System.out.println("We have a client for: " + topicId);
                		msg = topicId + ":::" + msg;
                		PublicationsManager.getInstance().onPublish(session, msg);
                	}
            		else{
            			msg = "Not  A Valid Topic..."; 
            		}
            	}
            	catch(Exception e){
            		msg = "Not  A Valid Topic...";
            	}
            	
            }
        }
    }


}

