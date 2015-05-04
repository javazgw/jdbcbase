package com.base.myproject.server.cache;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

import com.base.myproject.server.timetask.QuartZJob;
import com.base.myproject.server.timetask.Testjob;

public class HTCache<K, V> {

	private static final float hashTableLoadFactor = 0.75f;

	private LinkedHashMap<K, V> map;
	private int cacheSize;

	public static HTCache<String ,HTcacheItem> htcache;

	/**
	 * Creates a new HT cache.
	 * 
	 * @param cacheSize
	 *            the maximum number of entries that will be kept in this cache.
	 */
	private HTCache(int cacheSize) {
		this.cacheSize = cacheSize;
		int hashTableCapacity = (int) Math
				.ceil(cacheSize / hashTableLoadFactor) + 1;
		map = new LinkedHashMap<K, V>(hashTableCapacity, hashTableLoadFactor,
				true) {
			// (an anonymous inner class)
			private static final long serialVersionUID = 1;

			@Override
			protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
				return size() > HTCache.this.cacheSize;
			}
		};
	}

	public static HTCache<String ,HTcacheItem> getinstance()
{
	if(htcache== null)
	{
		htcache = new HTCache<String ,HTcacheItem>(50);
	}
	return htcache;
}

	/**
	 * Retrieves an entry from the cache.<br>
	 * The retrieved entry becomes the MRU (most recently used) entry.
	 * 
	 * @param key
	 *            the key whose associated value is to be returned.
	 * @return the value associated to this key, or null if no value with this
	 *         key exists in the cache.
	 */
	public synchronized V get(K key) {
		return map.get(key);
	}

	/**
	 * Adds an entry to this cache. The new entry becomes the MRU (most recently
	 * used) entry. If an entry with the specified key already exists in the
	 * cache, it is replaced by the new entry. If the cache is full, the LRU
	 * (least recently used) entry is removed from the cache.
	 * 
	 * @param key
	 *            the key with which the specified value is to be associated.
	 * @param value
	 *            a value to be associated with the specified key.
	 */
	public synchronized void put(K key, V value) {
		map.put(key, value);
		
		HashMap<String, Object> hm = new HashMap<String, Object> ();
		hm.put("zgw", "zgw2");
		hm.put("zgw1", "zgw3");

		if(value instanceof HTcacheItem )
		{
			((HTcacheItem) value).exe();
			
		}
		
	
	}

	/**
	 * Clears the cache.
	 */
	public synchronized void clear() {
		map.clear();
	}

	/**
	 * Returns the number of used entries in the cache.
	 * 
	 * @return the number of entries currently in the cache.
	 */
	public synchronized int usedEntries() {
		return map.size();
	}

	/**
	 * Returns a <code>Collection</code> that contains a copy of all cache
	 * entries.
	 * 
	 * @return a <code>Collection</code> with a copy of the cache content.
	 */
	public synchronized Collection<Map.Entry<K, V>> getAll() {
		return new ArrayList<Map.Entry<K, V>>(map.entrySet());
	}

	public static void main(String[] argv)
	{
		HTcacheItem hti= new HTcacheItem("a123","item"){

			@Override
			void exe() {
				HashMap<String, Object> hm2 = new HashMap<String, Object> ();
				hm2.put("zgw", "zgw22222222");
				hm2.put("zgw1", "zgw3");
//				try {
//					QuartZJob.getInstance().addjob(Testjob.class, hm2,this.getCronexpression());
//				} catch (SchedulerException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
			}

		
			
		};
		hti.setAdddate(new Date());
		hti.setCronexpression("*/10 * * * * ?");
		
		HTCache.getinstance().put("A123", hti);
		System.out.println(HTCache.getinstance().get("A123"));
		System.out.println(HTCache.getinstance().get("A1233"));
		
	}
} // end class HTCache
