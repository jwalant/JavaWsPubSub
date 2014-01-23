package com.jwshah.dummy.ws.samples;

import java.util.HashSet;
import java.util.Set;

import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;

public class ExamplesConfig implements ServerApplicationConfig {

    @Override
    public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> scanned) {
        Set<Class<?>> results = new HashSet<Class<?>>();
        for (Class<?> clazz : scanned) {
            if (clazz.getPackage().getName().startsWith("com.jwshah.dummy.ws.")) {
                results.add(clazz);
            }
        }
        return results;
    }

	@Override
	public Set<ServerEndpointConfig> getEndpointConfigs(
			Set<Class<? extends Endpoint>> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
