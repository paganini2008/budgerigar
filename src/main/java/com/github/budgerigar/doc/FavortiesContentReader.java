package com.github.budgerigar.doc;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.github.doodler.common.utils.DebugUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Description: FavortiesContentReader
 * @Author: Fred Feng
 * @Date: 01/12/2024
 * @Version 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class FavortiesContentReader implements FileContentReader {

    private final HtmlContentExtractor htmlContentExtractor;

    @Override
    public Iterator<FileContent> readContent(Path path, Context context) throws IOException {
        Charset charset = (Charset) context.getAttr(Context.FILE_CHARSET, defaultCharset);
        Document document = Jsoup.parse(path, charset.displayName());
        String bodyText = document.body().html();
        if (StringUtils.isBlank(bodyText)) {
            return IteratorUtils.emptyIterator();
        }
        Elements links = document.body().select("a");
        if (CollectionUtils.isEmpty(links)) {
            return IteratorUtils.emptyIterator();
        }
        return new Iterator<FileContent>() {

            Iterator<Element> actualIt = links.iterator();
            String fileName = path.getFileName().toString();
            long lastModfied = Files.getLastModifiedTime(path).toMillis();
            int n = 0;

            @Override
            public boolean hasNext() {
                return actualIt.hasNext();
            }

            @Override
            public FileContent next() {
                String content = null;
                Element link = actualIt.next();
                String title = link.text().trim();
                String path = link.absUrl("href");
                try {
                    content = htmlContentExtractor.extractHtml(URI.create(path));
                } catch (Exception e) {
                    if (log.isErrorEnabled()) {
                        log.error("[{}]: {}", path, e.getMessage(), e);
                    }
                    content = "";
                }
                return new FileContent(fileName, title, "html", path,
                        lastModfied + (n++) * 60 * 1000, content);
            }
        };
    }

    @Override
    public boolean supportExtensions(Path path, Context context) {
        String fileName = path.getFileName().toString();
        String ext = FilenameUtils.getExtension(fileName);
        if ("fav".equalsIgnoreCase(ext)) {
            return true;
        }
        boolean isHtml = "html".equalsIgnoreCase(ext) || "htm".equalsIgnoreCase(ext);
        if (isHtml) {
            String baseName = FilenameUtils.getBaseName(fileName);
            return baseName.startsWith("bookmarks");
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 1;
    }

    public static void main(String[] args) throws Exception {
        RestTemplateHtmlContentExtractor contentExtractor = new RestTemplateHtmlContentExtractor();
        contentExtractor.afterPropertiesSet();
        FavortiesContentReader contentReader = new FavortiesContentReader(contentExtractor);
        Iterator<FileContent> iterator =
                contentReader.readContent(Paths.get("g:/bookmarks_01_12_2024.html"), new Context());
        DebugUtils.info(iterator);
    }



}
