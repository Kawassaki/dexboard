package br.com.dextra.dexboard.utils;

import br.com.dextra.dexboard.servlet.QueryServlet;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MemCache {

    private MemcacheService memcacheService = MemcacheServiceFactory.getMemcacheService();
    private static final Logger LOG = LoggerFactory.getLogger(MemCache.class);

    public void doPut(String keyCache, String Data) {
        try {
            memcacheService.put(keyCache, Data);
        } catch (Throwable e) {
            LOG.trace("Error in put Google App Engine cache", e);
        }
    }

    public void doPutGeneric(Object keyCache, Object Data) {
        try {
            memcacheService.put(keyCache, Data);
        } catch (Throwable e) {
            LOG.trace("Error in put Google App Engine cache", e);
        }
    }

    public String doGet (String keyCache) {
        try {
            String data = (String) memcacheService.get(keyCache);
            return data;
        } catch (Throwable e) {
            LOG.trace("Error on get cache Google App engine", e);
            return null;
        }
    }

    public Object doGetGeneric (String keyCache) {
        try {
            Object data = memcacheService.get(keyCache);
            return data;
        } catch (Throwable e) {
            LOG.trace("Error on get cache Google App engine", e);
            return null;
        }
    }

    public void doDelete(String keyCache) {
        memcacheService.delete(keyCache);
    }
}
