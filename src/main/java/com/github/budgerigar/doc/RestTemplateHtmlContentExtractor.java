package com.github.budgerigar.doc;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.github.doodler.common.http.StringRestTemplate;

/**
 * 
 * @Description: RestTemplateHtmlContentExtractor
 * @Author: Fred Feng
 * @Date: 01/12/2024
 * @Version 1.0.0
 */
public class RestTemplateHtmlContentExtractor extends AbstractHtmlContentExtractor
        implements InitializingBean {

    private RestTemplate restTemplate;

    public RestTemplateHtmlContentExtractor() {}

    public RestTemplateHtmlContentExtractor(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String extractHtml(URI uri) throws Exception {
        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET,
                new HttpEntity<>(defaultHttpHeaders), String.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        }
        throw new HttpClientErrorException(responseEntity.getStatusCode());

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (restTemplate == null) {
            RestTemplate restTemplate = new StringRestTemplate();
            SimpleClientHttpRequestFactory clientHttpRequestFactory =
                    new SimpleClientHttpRequestFactory();
            clientHttpRequestFactory.setConnectTimeout(connectionTimeout);
            clientHttpRequestFactory.setReadTimeout(readTimeout);
            if (StringUtils.isNotBlank(proxyHost) && proxyPort > 0) {
                Proxy proxy =
                        new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
                clientHttpRequestFactory.setProxy(proxy);
            }
            restTemplate.setRequestFactory(clientHttpRequestFactory);
            this.restTemplate = restTemplate;
        }
    }

}
