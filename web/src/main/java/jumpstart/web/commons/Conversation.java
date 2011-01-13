package jumpstart.web.commons;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Conversation {
	private String _id;
	private Map<Object, Object> _objects = null;
	
	public Conversation(String id) {
		_id = id;
	}
	
	public String getId() {
		return _id;
	}
	
	public void setObject(Object key, Object obj) {
		if (_objects == null) {
			_objects = new HashMap<Object, Object>(1);
		}
		_objects.put(key, obj);
	}

	public Object getObject(Object key) {
		if (_objects == null) {
			return null;
		}
		else {
			return _objects.get(key);
		}
	}
	
	public String toString() {
		final String DIVIDER = ", ";

		StringBuffer buf = new StringBuffer();
		buf.append(this.getClass().getSimpleName() + ": ");
		buf.append("[");
		buf.append("id="+ _id + DIVIDER);
		buf.append("objects=");
		if (_objects == null) {
			buf.append("null");
		}
		else {
			buf.append("{");
			for (Iterator<Object> iterator = _objects.keySet().iterator(); iterator.hasNext();) {
				Object key = (Object) iterator.next();
				buf.append("(" + key + "," + "<" + _objects.get(key) == null ? "null" : _objects.get(key).getClass().getSimpleName() + ">" + ")");
			}
			buf.append("}");
		}
		buf.append("]");
		return buf.toString();
	}
}
