package com.github.budgerigar.doc;

import java.net.URI;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.github.doodler.common.context.ManagedBeanLifeCycle;
import com.github.doodler.common.utils.MapUtils;

/**
 * 
 * @Description: HtmlUnitContentExtractor
 * @Author: Fred Feng
 * @Date: 01/12/2024
 * @Version 1.0.0
 */
public class HtmlUnitContentExtractor extends AbstractHtmlContentExtractor
        implements ManagedBeanLifeCycle {

    private int javaScriptTimeout = 10 * 1000;

    private WebClient webClient;

    public void setJavaScriptTimeout(int javaScriptTimeout) {
        this.javaScriptTimeout = javaScriptTimeout;
    }

    @Override
    public String extractHtml(URI uri) throws Exception {
        Page page = webClient.getPage(uri.toURL());
        webClient.waitForBackgroundJavaScript(10000);
        int responseStatusCode = page.getWebResponse().getStatusCode();
        if (responseStatusCode == HttpStatus.OK.value()) {
            if (page instanceof HtmlPage) {
                return ((HtmlPage) page).asXml();
            } else if (page instanceof TextPage) {
                return ((TextPage) page).getContent();
            }
        }
        return "";
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (StringUtils.isNotBlank(proxyHost) && proxyPort > 0) {
            webClient = new WebClient(BrowserVersion.BEST_SUPPORTED, proxyHost, proxyPort);
        } else {
            webClient = new WebClient(BrowserVersion.BEST_SUPPORTED);
        }
        if (MapUtils.isNotEmpty(defaultHttpHeaders)) {
            for (String headerName : defaultHttpHeaders.keySet()) {
                webClient.addRequestHeader(headerName, defaultHttpHeaders.getFirst(headerName));
            }
        }
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setDownloadImages(false);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setTimeout(readTimeout);
        webClient.setCookieManager(new CookieManager());
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.setJavaScriptTimeout(javaScriptTimeout);
    }

    @Override
    public void destroy() throws Exception {
        if (webClient != null) {
            webClient.close();
        }
    }

}
