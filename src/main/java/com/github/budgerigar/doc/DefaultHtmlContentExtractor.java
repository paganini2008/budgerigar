package com.github.budgerigar.doc;

import java.net.URI;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.springframework.http.HttpStatus;

/**
 * 
 * @Description: DefaultHtmlContentExtractor
 * @Author: Fred Feng
 * @Date: 01/12/2024
 * @Version 1.0.0
 */
public class DefaultHtmlContentExtractor extends AbstractHtmlContentExtractor {

    @Override
    public String extractHtml(URI uri) throws Exception {
        Connection connection = Jsoup.connect(uri.toString());
        connection.followRedirects(true);
        connection.timeout(readTimeout);
        connection.userAgent(userAgents.get(RandomUtils.nextInt(0, userAgents.size())));
        if (defaultHttpHeaders != null) {
            connection.headers(defaultHttpHeaders.toSingleValueMap());
        }
        if (StringUtils.isNotBlank(proxyHost) && proxyPort > 0) {
            connection.proxy(proxyHost, proxyPort);
        }
        Response response = connection.execute();
        if (HttpStatus.valueOf(response.statusCode()).is2xxSuccessful()) {
            return response.body();
        }
        return "";
    }

}
