package com.jwshah.dummy.ws.samples.pubsub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.Session;

/**
 * Maintains a mapping of 'topic' to '
 * @author jwshah
 *
 */
public class PublicationsManager {
	
	private final ConcurrentHashMap<String,Publication> publicationsMap = new ConcurrentHashMap<String,Publication>();
	private static PublicationsManager instance;
	private PublicationsManager(){
		
	}
	public static synchronized PublicationsManager getInstance(){
		if(instance==null){
			instance = new PublicationsManager();
		}
		return instance;
	}

	/**
	 * @return the publicationsMap
	 */
	public ConcurrentHashMap<String,Publication> getPublicationsMap() {
		return publicationsMap;
	}
	
	/**Publish method should call this**/
	private Publication addAndReturnPublisher(String topic){
		if(topic!=null && !topic.isEmpty()){
			publicationsMap.putIfAbsent(topic, new Publication());
			return publicationsMap.get(topic);
		}
		else{
			return null;
		}
	}
	
	public void onPublish(Session e, String msg){
		if(this.getTopicFromSession(e)!=null){
			Publication p = this.addAndReturnPublisher(this.getTopicFromSession(e));
			try {
				e.getBasicRemote().flushBatch();
				e.getBasicRemote().sendText("Publisher Registered... for " + this.getTopicFromSession(e));
				ArrayList<String> messages = new ArrayList<String>();
				messages.add(msg);
				p.publish((messages));
			} catch (IOException e1) {
				e1.printStackTrace();
				System.out.println("Not able to publish, register again");
			}
			
		}
		//else do nothing
		
	}
	
	public void onSubscribe(Session e){
		if(this.getTopicFromSession(e)!=null){
			Publication p = this.addAndReturnPublisher(this.getTopicFromSession(e));
			try {
				e.getBasicRemote().sendText("Subscribing to Topic..." + this.getTopicFromSession(e));
				p.addSubsribers(e);
			} catch (IOException e1) {
				System.out.println("Error communicating with subsriber @ " + e.getQueryString());
				e1.printStackTrace();
			}
		}
		
	}
	public String getTopicFromSession(Session session){
		return (session.getRequestParameterMap().get("topic")==null) ? null : session.getRequestParameterMap().get("topic").get(0);
	}
	
}
