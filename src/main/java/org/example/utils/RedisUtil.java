package org.example.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {

    public static String set(String key, String value) {
        Jedis jedis = null;
        
        try {
            jedis = getConnection();
            return jedis.set(key, value);
        } catch (Exception e) {
            throw e;
        } finally {
            if(jedis != null) {
                jedis.close();                
            }
        }
    }

    public static String set(String key, String value, int expireSecond) {
        Jedis jedis = null;        
        try {
            jedis = getConnection();
            return jedis.setex(key, expireSecond, value);
        } catch (Exception e) {
            throw e;
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }
    public static String get(String key) {
        Jedis jedis = null;       
        try {
            jedis = getConnection();
            return jedis.get(key);
        } catch (Exception e) {
            throw e;
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    public static boolean exists(String key) {
        Jedis jedis = null;       
        try {        	
            jedis = getConnection();            
            return jedis.exists(key);
        } catch (Exception e) {
            throw e;
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }	
    }
    
    public static long delete(String key) {
        Jedis jedis = null;       
        try {
            jedis = getConnection();
            
            if(jedis.exists(key)) {
            	return jedis.del(key);
            }
            return (long)-1;
        } catch (Exception e) {
            throw e;
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }	
    }
    
    public static long pttl(String key) {
        Jedis jedis = null;       
        try {
            jedis = getConnection();
            
            return jedis.pttl(key);
        } catch (Exception e) {
            throw e;
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }	
    }
    
    public static void setex(String key, int cacheKeepSecond, byte[] data) {
    	Jedis jedis = null;       
        try {
            jedis = getConnection();
            jedis.setex(key.getBytes(), cacheKeepSecond, data);
        } catch (Exception e) {
            throw e;
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }
    
    private static Jedis getConnection() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        JedisPool jedisPool = null;
        try {
            jedisPool = new JedisPool(poolConfig, "127.0.0.1", 6379, 1000);
            return jedisPool.getResource();
        } catch (Exception e) {
            throw e;
        } finally {
            if(jedisPool != null){
                jedisPool.close();
            }
        }
    }
}