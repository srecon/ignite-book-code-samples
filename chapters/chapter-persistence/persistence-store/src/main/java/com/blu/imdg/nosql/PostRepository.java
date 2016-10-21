package com.blu.imdg.nosql;

import com.blu.imdg.nosql.model.MongoPost;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * Created by developer on 9/26/16.
 */
public interface PostRepository extends MongoRepository<MongoPost, String>{

}
