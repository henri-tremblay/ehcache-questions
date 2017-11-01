package pro.tremblay.ehcachequestions.stackoverflow.q41350991;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class JsonObjectCacheManagerTest {

    private JsonObjectCacheManager cacheManager;

    private Map<String, Object> names;

    @Before
    public void setup(){
        names = new HashMap();
        names.put("name1", "Spirit Air Lines");
        names.put("name2", "American Airlines");
        names.put("name3", "United Airlines");
        cacheManager = new JsonObjectCacheManager();
    }
    @Test
    public void isPresentReturnsTrueIfObjectsPutInCache() throws Exception {
        //put in cache
        cacheManager.putInCache("names",names);
        assertTrue(cacheManager.isKeyPresent("names"));
    }

    @Test
    public void cacheTest() throws Exception {
        //put in cache
        cacheManager.putInCache("names",names);

        //retrieve from cache
        Map<String, Object> namesFromCache = (Map<String, Object>) cacheManager.retrieveFromCache("names");
        //validate against the cached object
        assertEquals(3, namesFromCache.size());
        assertEquals("American Airlines", namesFromCache.get("name2"));

    }
}
