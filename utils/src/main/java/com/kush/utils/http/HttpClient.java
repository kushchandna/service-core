package com.kush.utils.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class HttpClient<T> {

    private static final String DEFAULT_ENCODING = "UTF-8";

    private final String url;
    private final HttpResponseReader<T> responseReader;

    public HttpClient(String url, HttpResponseReader<T> responseReader) {
        this.responseReader = responseReader;
        this.url = url;
    }

    public T getObject(Map<String, Object> parameters) throws IOException {
        String queryString = buildQueryString(parameters);
        String queryUrl = prepareQueryUrl(queryString);
        return readResult(queryUrl);
    }

    private String prepareQueryUrl(String queryString) {
        String queryUrl = url;
        if (!queryString.isEmpty()) {
            queryUrl += "?" + queryString;
        }
        return queryUrl;
    }

    private T readResult(String queryUrl) throws IOException, ClientProtocolException {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(queryUrl);
        try (CloseableHttpResponse response = client.execute(request)) {
            InputStream responseStream = response.getEntity().getContent();
            return responseReader.read(responseStream);
        }
    }

    private static String buildQueryString(Map<String, Object> map) throws IOException {
        StringBuilder sb = new StringBuilder(map.size() * 8);
        Iterator<Entry<String, Object>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, Object> entry = it.next();
            String key = entry.getKey();
            if (key == null) {
                throw new NullPointerException("Null key found in map");
            }
            sb.append(URLEncoder.encode(key, DEFAULT_ENCODING));
            sb.append("=");
            Object value = entry.getValue();
            String valueAsString = value != null ? URLEncoder.encode(value.toString(), DEFAULT_ENCODING) : "";
            sb.append(valueAsString);
            if (it.hasNext()) {
                sb.append("&");
            }
        }
        return sb.toString();
    }
}