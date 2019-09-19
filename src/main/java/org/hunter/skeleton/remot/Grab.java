package org.hunter.skeleton.remot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.util.Assert;

import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author wujianchuan
 * 数据抓取对象
 */
public class Grab<R> {
    private StringBuilder url;
    private RequestBody<String, Object> requestBody = new RequestBody<>();
    private StringBuilder pathParams = new StringBuilder();
    private Map<String, String> headers = new HashMap<>(2);

    private int connectionRequestTimeout = 5000;
    private int connectTimeout = 5000;
    private int socketTimeout = 5000;

    private static final CloseableHttpClient CLIENT;
    private static SSLConnectionSocketFactory sslFactory = null;

    static {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (chain, authType) -> true).build();
            sslFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        CLIENT = HttpClients.custom().setSSLSocketFactory(sslFactory).build();
    }

    public static <T> Grab<T> newInstance(String url) {
        return new Grab<>(url);
    }

    private Grab(String url) {
        Assert.notNull(url, "url cannot be null.");
        this.url = new StringBuilder(url);
    }

    /**
     * 拼接请求参数
     *
     * @param key   键
     * @param value 值
     * @return Grab Instance
     */
    public Grab<R> appendParams(String key, Object value) {
        if (this.pathParams.length() == 0) {
            this.pathParams.append("?").append(key).append("=").append(value);
        } else {
            this.pathParams.append("&").append(key).append("=").append(value);
        }
        return this;
    }

    /**
     * 拼接请求体
     *
     * @param key   键
     * @param value 值
     * @return Grab Instance
     */
    public Grab<R> appendBody(String key, Object value) {
        this.requestBody.put(key, value);
        return this;
    }

    /**
     * 拼接请求体
     *
     * @param attributes 多个键值对
     * @return Grab Instance
     */
    public Grab<R> appendBodyAll(Map<String, Object> attributes) {
        this.requestBody.putAll(attributes);
        return this;
    }

    /**
     * 植入加密方式
     *
     * @param encryption 加密方式
     * @return Grab
     */
    public Grab<R> setEncryption(EncryptionComputing encryption) {
        this.requestBody.setEncryption(encryption);
        return this;
    }

    /**
     * 拼接请求头
     *
     * @param key   键
     * @param value 值
     * @return Grab Instance
     */
    public Grab<R> appendHeader(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    public Grab<R> setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
        return this;
    }

    public Grab<R> setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public Grab<R> setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }

    /**
     * 发送 GET 请求
     *
     * @param callback 回调函数 参数类型为 String.class
     * @throws IOException IO 异常
     */
    public R get(Function<String, R> callback) throws IOException {
        HttpGet httpGet = new HttpGet(this.url.append(this.pathParams).toString());
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(this.connectTimeout)
                .setConnectionRequestTimeout(this.connectionRequestTimeout)
                .setSocketTimeout(this.socketTimeout)
                .build();
        httpGet.setConfig(requestConfig);
        if (!this.headers.isEmpty()) {
            for (Map.Entry<String, String> entry : this.headers.entrySet()) {
                httpGet.addHeader(entry.getKey(), entry.getValue());
            }
        }
        HttpResponse httpResponse = CLIENT.execute(httpGet);
        return this.getResult(httpResponse, callback);
    }

    /**
     * 发送 POST 请求
     *
     * @param callback 回调函数 参数类型为 String.class
     * @throws IOException IO 异常
     */
    public R post(Function<String, R> callback) throws Exception {
        HttpPost httpPost = new HttpPost(this.url.append(this.pathParams).toString());
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(this.connectTimeout)
                .setConnectionRequestTimeout(this.connectionRequestTimeout)
                .setSocketTimeout(this.socketTimeout)
                .build();
        httpPost.setConfig(requestConfig);
        if (!this.headers.isEmpty()) {
            for (Map.Entry<String, String> entry : this.headers.entrySet()) {
                httpPost.addHeader(entry.getKey(), entry.getValue());
            }
        }
        if (!this.requestBody.body.isEmpty()) {
            StringEntity stringEntity = new StringEntity(this.requestBody.writeValueAsString(), StandardCharsets.UTF_8);
            httpPost.setEntity(stringEntity);
        }
        HttpResponse httpResponse = CLIENT.execute(httpPost);
        return this.getResult(httpResponse, callback);
    }


    /**
     * 请求体
     *
     * @param <K> key 类型
     * @param <V> value 类型
     */
    static class RequestBody<K, V> {
        private Map<K, V> body = new HashMap<>(6);
        private EncryptionComputing encryption;

        /**
         * 添加请求参数
         *
         * @param key   键
         * @param value 值
         * @return Request Body
         */
        public RequestBody put(K key, V value) {
            this.body.put(key, value);
            return this;
        }

        /**
         * 添加请求参数
         *
         * @param attributes 多个键值对
         * @return Request Body
         */
        public RequestBody putAll(Map<K, V> attributes) {
            this.body.putAll(attributes);
            return this;
        }

        /**
         * 格式化为Json Str
         *
         * @return json str
         * @throws Exception e
         */
        public String writeValueAsString() throws Exception {
            if (!this.body.isEmpty()) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));
                return this.encrypt(objectMapper.writeValueAsString(this.body));
            } else {
                return null;
            }
        }

        /**
         * 植入加密算法
         *
         * @param encryption 加密算法
         * @return Request Body
         */
        public RequestBody setEncryption(EncryptionComputing encryption) {
            this.encryption = encryption;
            return this;
        }

        private String encrypt(String input) throws Exception {
            if (this.encryption != null) {
                return this.encryption.encrypt(input);
            } else {
                return input;
            }
        }
    }

    /**
     * 处理响应信息
     *
     * @param httpResponse http response
     * @param callback     callback
     * @return R
     * @throws IOException e
     */
    private R getResult(HttpResponse httpResponse, Function<String, R> callback) throws IOException {
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8));
        if (statusCode != HttpStatus.SC_OK) {
            String message = reader.readLine();
            throw new RemoteException(message);
        }
        StringBuilder result = new StringBuilder();
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                reader.close();
                return callback.apply(result.toString());
            }
            result.append(line);
        }
    }
}
