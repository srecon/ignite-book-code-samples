package com.blu.imdg.common;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * Created by mikl on 23.08.16.
 */
public class HttpAuditClient {

    public static Boolean sendResult(HttpClient client, Boolean result, String messageId) throws URISyntaxException, IOException {
        URI uri = new URIBuilder().setScheme("http").setHost("localhost").setPort(9998).setPath("/audit/log")
                .setParameter("msgId", messageId)
                .setParameter("validationResult", result.toString())
                .build();
        HttpGet httpget = new HttpGet(uri);
        try (CloseableHttpResponse resp = (CloseableHttpResponse) client.execute(httpget)) {
            System.out.println("msg=" + messageId + " result=" + result);
            return result;
        }
    }

    public static HttpClient createHttpClient(ConcurrentMap<Object, Object> nodeLocalMap) {
        return (HttpClient) nodeLocalMap.computeIfAbsent("httpClient", new Function<Object, HttpClient>() {
            @Override
            public HttpClient apply(Object o) {
                return createHttpClient();
            }
        });
    }

    public static HttpClient createHttpClient() {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        return HttpClients.custom()
                .setConnectionManager(cm)
                .build();
    }
}
