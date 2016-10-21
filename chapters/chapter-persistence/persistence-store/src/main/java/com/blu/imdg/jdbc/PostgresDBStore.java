package com.blu.imdg.jdbc;

import com.blu.imdg.jdbc.model.Post;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import org.apache.ignite.IgniteException;
import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.apache.ignite.lifecycle.LifecycleAware;

/**
 * Let's have a look at simple implemention of CacheStore for Postgres DB. As says documentation CacheStoreAdapter
 * provides default implementation for loadAll(), writeAll(), and deleteAll() methods which simply iterates through all
 * keys one by one. Created by developer on 9/25/16.
 */
public class PostgresDBStore extends CacheStoreAdapter<String, Post> implements LifecycleAware {

    @Override public Post load(String key) throws CacheLoaderException {
        try (Connection conn = getConnection()) {
            try (PreparedStatement st = conn.prepareStatement("SELECT * FROM Posts WHERE id=?")) {
                st.setString(1, key);

                ResultSet rs = st.executeQuery();

                return rs.next() ? new Post(rs.getString(1), rs.getString(2), rs.getString(3), rs.getDate(4).toLocalDate(), rs.getString(5)) : null;
            }
        }
        catch (SQLException e) {
            throw new CacheLoaderException("Failed to load: " + key, e);
        }
    }

    @Override public void write(Cache.Entry<? extends String, ? extends Post> entry) throws CacheWriterException {
        try (Connection conn = getConnection()) {
            try (PreparedStatement st = conn.prepareStatement("INSERT INTO POSTS(ID,TITLE,DESCRIPTION,CREATIONDATE,AUTHOR) VALUES (?,?,?,?,?);")) {
                com.blu.imdg.jdbc.model.Post post = entry.getValue();

                st.setString(1, post.getId());
                st.setString(2, post.getTitle());
                st.setString(3, post.getDescription());
                st.setDate(4, Date.valueOf(post.getCreationDate()));
                st.setString(5, post.getAuthor());

                st.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new CacheWriterException("Failed to write post entry: " + entry, e);
        }
    }

    @Override public void delete(Object key) throws CacheWriterException {
        try (Connection conn = getConnection()) {
            try (PreparedStatement st = conn.prepareStatement("DELETE FROM POSTS WHERE ID= ?")) {

                st.setString(1, (String)key);

                st.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new CacheWriterException("Failed to delete entry: " + key, e);
        }
    }

    private Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
        conn.setAutoCommit(true);

        return conn;
    }

    @Override public void start() throws IgniteException {
        try (Connection conn = getConnection();
        ) {
            PreparedStatement statement =
                conn.prepareStatement("create table POSTS(\n" +
                    "  id VARCHAR(150),\n" +
                    "  title VARCHAR(255),\n" +
                    "  description TEXT,\n" +
                    "  creationDate DATE,\n" +
                    "  author VARCHAR(150)\n" +
                    ");");
            int result = statement.executeUpdate();
            statement = conn.prepareStatement("CREATE UNIQUE INDEX posts_id_uindex ON posts (id);\n" +
                "ALTER TABLE posts ADD CONSTRAINT posts_id_pk PRIMARY KEY (id);\n" +
                "ALTER TABLE posts ALTER COLUMN id SET NOT NULL;");
            result = statement.executeUpdate();
        }
        catch (SQLException e) {

        }
    }

    @Override public void stop() throws IgniteException {
        try (Connection conn = getConnection();
        ) {
            PreparedStatement statement =
                conn.prepareStatement("DROP table POSTS;");
            int result = statement.executeUpdate();
            statement = conn.prepareStatement("DROP INDEX posts_id_uindex;");
            result = statement.executeUpdate();
        }
        catch (SQLException e) {

        }

    }
}
