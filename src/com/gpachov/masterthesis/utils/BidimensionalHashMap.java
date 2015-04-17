package com.gpachov.masterthesis.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class BidimensionalHashMap<K1,K2, V> {
	private final int MAX_MAP_SIZE = 140_000;

	private LinkedHashMap<K1, Map<K2, V>> implementation = new LinkedHashMap<K1, Map<K2, V>>() {
		protected boolean removeEldestEntry(Map.Entry<K1, java.util.Map<K2, V>> eldest) {
			return implementation.entrySet().stream().flatMap(e -> e.getValue().entrySet().stream())
					.collect(Collectors.toList()).size() > MAX_MAP_SIZE;
		}
	};

	public long size() {
		return implementation.entrySet().stream().flatMap(e -> e.getValue().entrySet().stream()).count();
	}

	public boolean isEmpty() {
		return size() == 0;
	}
	
	public Map<K2,V> getRow(K1 attribute){
		implementation.computeIfAbsent(attribute, k -> new HashMap<K2, V>());
		return implementation.get(attribute);
	}

	public V get(K1 key1, K2 key2) {
		implementation.computeIfAbsent(key1, k -> new HashMap<K2, V>());
		V v1 = implementation.get(key1).get(key2);
		return v1;
	}

	public V put(K1 key1, K2 key2, V value) {
		implementation.computeIfAbsent(key1, k -> new HashMap<K2, V>());
		implementation.get(key1).put(key2, value);
		return value;
	}

}
