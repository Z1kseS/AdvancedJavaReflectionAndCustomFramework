package framework.core.context;

import java.util.HashMap;
import java.util.Map;

import framework.core.annotations.Statefull;

public class GenericWebAnnotationApplicationContext {

	private Map<String, Object> statefullObjects = new HashMap<String, Object>();

	private GenericAnnotationApplicationContext genericAnnotationApplicationContext;

	public GenericWebAnnotationApplicationContext(String packageName) {
		this.genericAnnotationApplicationContext = new GenericAnnotationApplicationContext(packageName);
	}

	@SuppressWarnings("unchecked")
	public <K> K getBean(String sessionId, String name, Class<?> K) throws Exception {
		if (K.isAnnotationPresent(Statefull.class)) {
			K object = (K) statefullObjects.get(sessionId);
			if (object == null)
				object = (K) genericAnnotationApplicationContext.getBeanFactory().getBean(name, K);
			statefullObjects.put(sessionId, object);
			return object;
		}
		return (K) genericAnnotationApplicationContext.getBeanFactory().getBean(name, K);
	}

}
