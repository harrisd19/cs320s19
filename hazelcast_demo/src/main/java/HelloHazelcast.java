import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICacheManager;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.CompleteConfiguration;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;

public class HelloHazelcast {

    HelloHazelcast() {

        CachingProvider cachingProvider = Caching.getCachingProvider();
        CacheManager cacheManager = cachingProvider.getCacheManager();
        System.out.println(cacheManager);
        Cache<Object, Object> cache = cacheManager
                .getCache( "default", Object.class, Object.class );
    }

    public static void main(String[] args) {
        new HelloHazelcast();
    }
}

