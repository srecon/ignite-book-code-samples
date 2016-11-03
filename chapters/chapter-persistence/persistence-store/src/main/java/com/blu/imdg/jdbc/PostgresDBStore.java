package com.blu.imdg.jdbc;

import com.blu.imdg.jdbc.model.Post;
import org.apache.ignite.IgniteException;
import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.apache.ignite.lifecycle.LifecycleAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple implementation of CacheStore for Postgres DB. CacheStoreAdapter
 * provides default implementation for loadAll(), writeAll(), and deleteAll() methods which simply iterates through all
 * keys one by one. Created by developer on 9/25/16.
 */
public class PostgresDBStore extends CacheStoreAdapter<String, Post> implements LifecycleAware {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Post load(String key) throws CacheLoaderException {
        Map<String, Object> inputParam = new HashMap<>();
        inputParam.put("id", key);
        return jdbcTemplate.queryForObject("SELECT * FROM POSTS WHERE id=?", inputParam, new RowMapper<Post>() {
            @Override
            public Post mapRow(ResultSet rs, int i) throws SQLException {
                return new Post(rs.getString(1), rs.getString(2), rs.getString(3), rs.getDate(4).toLocalDate(), rs.getString(5));
            }
        });

    }

    @Override
    public void write(Cache.Entry<? extends String, ? extends Post> entry) throws CacheWriterException {
        com.blu.imdg.jdbc.model.Post post = entry.getValue();
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("ID", post.getId());
        parameterMap.put("TITLE", post.getTitle());
        parameterMap.put("DESCRIPTION", post.getDescription());
        parameterMap.put("CREATIONDATE", post.getCreationDate());
        parameterMap.put("AUTHOR", post.getAuthor());
        jdbcTemplate.update("INSERT INTO POSTS(ID,TITLE,DESCRIPTION,CREATIONDATE,AUTHOR) VALUES (:ID,:TITLE,:DESCRIPTION,:CREATIONDATE,:AUTHOR);", parameterMap);

    }

    @Override
    public void delete(Object key) throws CacheWriterException {
        Map<String, String> deleteMap = new HashMap<>();
        deleteMap.put("ID", (String) key);
        jdbcTemplate.update("DELETE FROM POSTS WHERE ID= ?", deleteMap);

    }

    @Override
    public void start() throws IgniteException {
        ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("postgres-context.xml");
        jdbcTemplate = context.getBean(NamedParameterJdbcTemplate.class);
    }

    @Override
    public void stop() throws IgniteException {

    }
}
