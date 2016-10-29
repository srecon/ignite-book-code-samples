package com.blu.imdg.nosql;

import com.blu.imdg.nosql.model.MongoPost;
import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import org.apache.ignite.IgniteException;
import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.apache.ignite.lifecycle.LifecycleAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;

/**
 * Let's have a look at simple implemention of CacheStore for MongoDB. Created by developer on 9/26/16.
 */
public class MongoDBStore extends CacheStoreAdapter<String, MongoPost> implements LifecycleAware {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private MongoOperations mongoOperations;

    private static Logger logger = LoggerFactory.getLogger(MongoDBStore.class);

    @Override public MongoPost load(String key) throws CacheLoaderException {

        logger.info(String.valueOf(postRepository));
        return postRepository.findOne(key);
    }

    @Override public void write(Cache.Entry<? extends String, ? extends MongoPost> entry) throws CacheWriterException {
        MongoPost post = entry.getValue();
        logger.info(String.valueOf(postRepository));
        postRepository.save(post);
    }

    @Override public void delete(Object key) throws CacheWriterException {
        logger.info(String.valueOf(postRepository));
        postRepository.delete((String)key);
    }

    @Override public void start() throws IgniteException {
        ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("mongo-context.xml");
        postRepository = context.getBean(PostRepository.class);
        logger.info(String.valueOf(postRepository));
        mongoOperations = context.getBean(MongoOperations.class);
        if (!mongoOperations.collectionExists(MongoPost.class))
            mongoOperations.createCollection(MongoPost.class);

    }

    @Override public void stop() throws IgniteException {
        if (mongoOperations.collectionExists(MongoPost.class))
            mongoOperations.dropCollection(MongoPost.class);
    }
}
