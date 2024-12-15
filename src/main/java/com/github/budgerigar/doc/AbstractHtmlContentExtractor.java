package com.github.budgerigar.doc;

import java.util.Arrays;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * 
 * @Description: AbstractHtmlContentExtractor
 * @Author: Fred Feng
 * @Date: 01/12/2024
 * @Version 1.0.0
 */
public abstract class AbstractHtmlContentExtractor implements HtmlContentExtractor {

    protected String proxyHost;
    protected int proxyPort;
    protected int connectionTimeout = 10 * 1000;
    protected int readTimeout = 60 * 1000;
    protected HttpHeaders defaultHttpHeaders;

    protected AbstractHtmlContentExtractor() {
        defaultHttpHeaders = new HttpHeaders();
        defaultHttpHeaders.setAccept(Arrays.asList(MediaType.ALL));
        defaultHttpHeaders.set("User-Agent",
                userAgents.get(RandomUtils.nextInt(0, userAgents.size())));
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public void setDefaultHttpHeaders(HttpHeaders defaultHttpHeaders) {
        this.defaultHttpHeaders = defaultHttpHeaders;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public HttpHeaders getDefaultHttpHeaders() {
        return defaultHttpHeaders;
    }

}
