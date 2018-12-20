package com.kush.utils.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.kush.utils.convertor.StringConvertor;

public class HttpClient {

    private static final String DEFAULT_ENCODING = "UTF-8";

    private final String url;
    private final StringConvertor stringConvertor;

    public HttpClient(String url, StringConvertor stringConvertor) {
        this.url = url.endsWith("/") ? url : url + '/';
        this.stringConvertor = stringConvertor;
    }

    public <T> T getObject(Map<String, Object> parameters, Class<T> returnType) throws IOException {
        String queryString = buildQueryString(parameters);
        String queryUrl = prepareQueryUrl(queryString);
        String resultAsString = getResultAsString(queryUrl);
        return stringConvertor.convert(resultAsString, returnType);
    }

    private String prepareQueryUrl(String queryString) {
        String queryUrl = url;
        if (!queryString.isEmpty()) {
            queryUrl += "?" + queryString;
        }
        return queryUrl;
    }

    private String getResultAsString(String queryUrl) throws IOException, ClientProtocolException {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(queryUrl);
        HttpResponse response = client.execute(request);
        InputStream responseStream = response.getEntity().getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));
        return reader.lines().collect(Collectors.joining());
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