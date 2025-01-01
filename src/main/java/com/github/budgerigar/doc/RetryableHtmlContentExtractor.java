package com.github.budgerigar.doc;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.springframework.retry.ExhaustedRetryException;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestClientException;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Description: RetryableHtmlContentExtractor
 * @Author: Fred Feng
 * @Date: 01/01/2025
 * @Version 1.0.0
 */
@Slf4j
public class RetryableHtmlContentExtractor implements HtmlContentExtractor, RetryListener {

    private final HtmlContentExtractor htmlContentExtractor;
    private final RetryTemplate retryTemplate;

    public RetryableHtmlContentExtractor(HtmlContentExtractor htmlContentExtractor) {
        this(htmlContentExtractor, 3);
    }

    public RetryableHtmlContentExtractor(HtmlContentExtractor htmlContentExtractor,
            int maxAttempts) {
        this.htmlContentExtractor = htmlContentExtractor;
        this.retryTemplate = createRetryTemplate(maxAttempts);
    }

    protected RetryableHtmlContentExtractor(HtmlContentExtractor htmlContentExtractor,
            RetryTemplate retryTemplate) {
        this.htmlContentExtractor = htmlContentExtractor;
        this.retryTemplate = retryTemplate;
    }

    @Override
    public String extractHtml(URI uri) throws Exception {
        return retryTemplate.execute(context -> {
            return htmlContentExtractor.extractHtml(uri);
        }, context -> {
            Throwable e = context.getLastThrowable();
            if (e instanceof RestClientException) {
                throw (RestClientException) e;
            }
            throw new ExhaustedRetryException(uri.toString(), e);
        });
    }

    protected RetryTemplate createRetryTemplate(int maxAttempts) {
        RetryTemplate retryTemplate = new RetryTemplate();
        Map<Class<? extends Throwable>, Boolean> exceptionMap = new HashMap<>();
        exceptionMap.put(RestClientException.class, true);
        exceptionMap.put(IOException.class, true);
        RetryPolicy retryPolicy = maxAttempts > 0 ? new SimpleRetryPolicy(maxAttempts, exceptionMap)
                : new NeverRetryPolicy();
        retryTemplate.setRetryPolicy(retryPolicy);
        retryTemplate.setBackOffPolicy(new FixedBackOffPolicy());
        retryTemplate.setListeners(new RetryListener[] {this});
        return retryTemplate;
    }

    @Override
    public <T, E extends Throwable> boolean open(RetryContext context,
            RetryCallback<T, E> callback) {
        if (log.isInfoEnabled()) {
            log.info("Start to extract page html with retry.");
        }
        return true;
    }

    @Override
    public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback,
            Throwable throwable) {
        if (log.isInfoEnabled()) {
            log.info("Complete to extract page html. Retry count: {}", context.getRetryCount());
        }
    }

    @Override
    public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback,
            Throwable e) {
        if (log.isErrorEnabled()) {
            log.error(e.getMessage(), e);
        }
    }

}
