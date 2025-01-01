package com.github.budgerigar.doc;

import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * @Description: FsDocumentConfig
 * @Author: Fred Feng
 * @Date: 15/12/2024
 * @Version 1.0.0
 */
@EnableConfigurationProperties({BgDocumentProperties.class})
@Configuration(proxyBeanMethods = false)
public class FileSystemDocumentConfig {

    @Bean
    public FileMonitor fileMonitor(BgDocumentProperties bgDocumentProperties,
            List<FileMonitorHandler> fileMonitorHandlers) {
        FileMonitor fileMonitor = new FileMonitor();
        fileMonitor.setBaseDirs(bgDocumentProperties.getMonitor().getBaseDir());
        fileMonitor.setIntervalSeconds(bgDocumentProperties.getMonitor().getIntervalSeconds());
        fileMonitor.setFileMonitorHandlers(fileMonitorHandlers);
        return fileMonitor;
    }

    @Bean("defaultContentReader")
    public FileContentReader defaultContentReader() {
        return new DefaultContentReader();
    }

    @Bean("pdfContentReader")
    public FileContentReader pdfContentReader() {
        return new PDFContentReader();
    }

    @Bean("excelContentReader")
    public FileContentReader excelContentReader() {
        return new ExcelContentReader(new DefaultCellValueExtractor());
    }

    @Bean("word2003ContentReader")
    public FileContentReader word2003ContentReader() {
        return new Word2003ContentReader();
    }

    @Bean("word2007ContentReader")
    public FileContentReader word2007ContentReader() {
        return new Word2007ContentReader();
    }

    @Bean("markdownContentReader")
    public FileContentReader markdownContentReader() {
        return new MarkdownContentReader();
    }

    @Bean("htmlContentReader")
    public FileContentReader htmlContentReader() {
        return new HtmlContentReader();
    }

    @Bean("csvContentReader")
    public FileContentReader csvContentReader() {
        return new CsvContentReader();
    }

    @Bean("slideShowContentReader")
    public FileContentReader slideShowContentReader() {
        return new SlideShowContentReader();
    }

    @Bean("favortiesContentReader")
    public FileContentReader favortiesContentReader(HtmlContentExtractor htmlContentExtractor) {
        return new FavortiesContentReader(htmlContentExtractor);
    }

    @ConditionalOnMissingBean
    @Bean
    public HtmlContentExtractor htmlContentExtractor() {
        return new HtmlUnitContentExtractor();
    }

}
