package pro.tremblay.ehcachequestions.bugs.b1955;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
//import org.ehcache.core.spi.service.StatisticsService;
//import org.ehcache.core.statistics.CacheStatistics;
//import org.ehcache.impl.internal.statistics.DefaultStatisticsService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

/**
 * @author Henri Tremblay
 */
public class FormLabelCache {

  Logger logger = Logger.getLogger(FormLabelCache.class.getCanonicalName());

  Cache<String, Map> cache;

//  StatisticsService statisticsService = new DefaultStatisticsService();

  private static final String FRMLAB_CACHE_ALIAS = "dmfas.cache.formLabel";

  @PostConstruct
  public void init() {

    logger.info("Starting module group rels cache..");

    CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
//      .using(statisticsService)
      .withCache(FRMLAB_CACHE_ALIAS,
        CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, Map.class,
          ResourcePoolsBuilder.newResourcePoolsBuilder()
            .heap(10L, MemoryUnit.MB))).build();
    cacheManager.init();

    // get the cache
    cache = cacheManager.getCache(FRMLAB_CACHE_ALIAS,String.class, Map.class);

  }

  public void addLables(String formId, List<FormLabel> labels) {

    Map<Integer, List<FormLabel>> labelMap = new HashMap<Integer, List<FormLabel>>();

    for (FormLabel label: labels) {
      if (!labelMap.containsKey(label.getLaId()))
        labelMap.put(label.getLaId(), new ArrayList<FormLabel>());
      labelMap.get(label.getLaId()).add(label);
    }
    logger.log(Level.FINE, "Adding labels for form {0}", formId);
    cache.put(formId,labelMap);

    if (cache.containsKey(formId)) {
      logger.log(Level.FINE, "FOUND {0}", formId);
    }

//    CacheStatistics statistics = statisticsService.getCacheStatistics(FRMLAB_CACHE_ALIAS);
//    System.out.println("addLabel: " + statistics.getCacheEvictions());
  }

  public  List<FormLabel> getCodes(String formId, int laId) {
//    CacheStatistics statistics = statisticsService.getCacheStatistics(FRMLAB_CACHE_ALIAS);
//
//    if (cache.containsKey(formId)) {
//      Map<String, List<FormLabel>> data = (Map<String, List<FormLabel>>) cache.get(formId);
//      System.out.println("hit: " + statistics.getCacheEvictions());
//      return data.get(laId);
//    }
//    System.out.println("miss: " + statistics.getCacheEvictions());
    return null;
  }

}
