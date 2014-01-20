package com.jwshah.dummy.ws.samples.pubsub;
import javax.websocket.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
/**
 * Every unique publication topic will have a corresponding instance of this class, this maintains the current list of all subscriber sessions and supports methods to add/remove subscribers.
 * On every new event from a corresponding 'publish' session publish is called which sends a message to list of subscriber sessions.
 *
 */
public class Publication {
	
	private String topic;
	private final CopyOnWriteArraySet<Session> subsribers = new CopyOnWriteArraySet<Session>();
	/**
	 * @return the subsribers
	 */
	public CopyOnWriteArraySet<Session> getSubsribers() {
		return subsribers;
	}
	/**
	 * @param subsribers the subsribers to set
	 */
	public void addSubsribers(Session sub) {
		System.out.println("Adding Subsrciber to topic:" + topic);
		this.subsribers.add(sub);
		System.out.println("Total number of subscribers: " + this.subsribers.size());
	}
	
	public void removeSub(Session sub){
		System.out.println("Removing Subsrciber for topic:" + topic);
		this.subsribers.remove(sub);
		System.out.println("Total number of subscribers: " + this.subsribers.size());
	}
	/**
	 * @return the topic
	 */
	public String getTopic() {
		return topic;
	}
	/**
	 * @param topic the topic to set
	 */
	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	public void publish(ArrayList<String> message){
		for(Session e: this.subsribers){
			System.out.println("Publishing to all: " + this.subsribers.size() + " subscribers");
			long start = System.currentTimeMillis();
			try {
				e.getBasicRemote().sendText(message.toString());
				System.out.println("Send Succes: Time Taken:" + (System.currentTimeMillis() - start) + " ms");
			} catch (IOException e1) {
				System.out.println("A user for " + e.getRequestParameterMap().get("topicId").get(0) + " left.... ");
				this.removeSub(e);
				System.out.println("Send Error: Time Taken:" + (System.currentTimeMillis() - start) + " ms" +e1.getLocalizedMessage());
			}
			catch(Exception e2){
				System.out.println("Unknown error:");
				System.out.println("Send Succes: Time Taken:" + (System.currentTimeMillis() - start) + " ms" + e2.getLocalizedMessage());
				this.removeSub(e);
				e2.printStackTrace();
			}
		}
	}

	@Override
	public int hashCode(){
		if(this.getTopic()!=null)
			return this.topic.hashCode();
		else
			return -1;
	}
	
	@Override
	public boolean equals(Object o){
		if(o!=null && o instanceof Publication){
			if(((Publication)o).getTopic().equalsIgnoreCase(this.getTopic())){
				return true;
			}
			return false;
		}
		return false;
	}
}
