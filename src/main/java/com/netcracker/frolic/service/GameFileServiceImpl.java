package com.netcracker.frolic.service;

import com.netcracker.frolic.cache.Cache;
import com.netcracker.frolic.cache.LRUCache;
import com.netcracker.frolic.cache.MockCache;
import com.netcracker.frolic.entity.GameFile;
import com.netcracker.frolic.repository.GameFileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Transactional
@PropertySource("classpath:cache.properties")
@Service("jpaGameFileService")
public class GameFileServiceImpl implements GameFileService {
    @Autowired private GameFileRepo gameFileRepo;
    private Cache<Long, GameFile> cache;

    @Value("${cacheGameFiles}")
    private boolean cacheIsOn;

    @Value("${gameFilesCacheCapacity}")
    private int cacheCapacity;

    @Transactional(readOnly = true)
    public Optional<GameFile> findById(long id) {
        return cache.containsObjectWithId(id) ? cache.getById(id)
                : gameFileRepo.findById(id);
    }

    public void deleteById(long id) {
        gameFileRepo.deleteById(id);
        cache.remove(id);
    }

    public GameFile save(GameFile gameFile)
    { return gameFileRepo.save(gameFile); }

    @PostConstruct private void initCache()
    { cache = cacheIsOn ? new LRUCache<>(cacheCapacity)
            : new MockCache<>(GameFile.class, cacheCapacity);
    }
}
