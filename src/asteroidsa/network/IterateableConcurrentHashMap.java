package asteroidsa.network;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class IterateableConcurrentHashMap<K, V> extends ConcurrentHashMap<K, V> {

	/**	  */
	private static final long serialVersionUID = 1L;
	/** */
	protected ArrayList<K> keyList = new ArrayList<K>();
	/** */
	protected ArrayList<V> valueList =  new ArrayList<V>();
	
	@Override
	public synchronized V put(K key, V value) {
		super.put(key, value);
		keyList.add(key);
		valueList.add(value);
		return value;
		
	}
	
	@Override
	public synchronized V replace(K key, V value) {
		if (containsKey(key)) {
			valueList.set(keyList.indexOf(key), value);
			return put(key, value);
		}
		return null;
	}
	
	@Override
	public synchronized boolean replace(K key, V oldValue, V newValue) {
		if (containsKey(key) && get(key).equals(oldValue)) {
			replace(key, newValue);
			return true;
		}
		return false;
	}
	
	@Override
	public synchronized V remove(Object key) {
		V value = super.remove(key);
		keyList.remove(key);
		valueList.remove(value);
		return value;
	}

	@Override
	public synchronized boolean remove(Object key, Object value) {
		if (containsKey(key) && get(key).equals(value)) {
			remove(key);
			return true;
		}
		return false;
	}
	
	@Override
	public synchronized void clear() {
		super.clear();
		keyList.clear();
		valueList.clear();
	}

	@Override	
	public V putIfAbsent(K key, V value) {
		if (!containsKey(key)) {
			return put(key, value);
		}
		return get(key);
	};
	
	
	public ArrayList<K> getKeyList() {
		return keyList;
	}

	public ArrayList<V> getValueList() {
		return valueList;
	}
	
}
