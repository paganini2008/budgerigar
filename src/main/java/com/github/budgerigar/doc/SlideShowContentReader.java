package com.github.budgerigar.doc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import com.github.doodler.common.Constants;
import com.github.doodler.common.utils.DebugUtils;
import lombok.SneakyThrows;

/**
 * 
 * @Description: SlideShowContentReader
 * @Author: Fred Feng
 * @Date: 30/11/2024
 * @Version 1.0.0
 */
public class SlideShowContentReader implements FileContentReader {

    @Override
    public Iterator<FileContent> readContent(Path path, Context context) throws Exception {
        try (XMLSlideShow slideShow = new XMLSlideShow(Files.newInputStream(path))) {
            final List<XSLFSlide> slides = slideShow.getSlides();
            if (CollectionUtils.isEmpty(slides)) {
                return IteratorUtils.emptyIterator();
            }
            return new Iterator<FileContent>() {

                String fileName = path.getFileName().toString();
                long lastModified = Files.getLastModifiedTime(path).toMillis();
                Iterator<XSLFSlide> actualIt = slides.iterator();

                @Override
                public boolean hasNext() {
                    return actualIt.hasNext();
                }

                @SneakyThrows
                @Override
                public FileContent next() {
                    XSLFSlide slide = actualIt.next();
                    String title = slide.getTitle();
                    String content = slide.getShapes().stream()
                            .filter(s -> s instanceof XSLFTextShape)
                            .map(s -> ((XSLFTextShape) s).getText())
                            .collect(Collectors.collectingAndThen(
                                    Collectors.joining(Constants.NEWLINE), StringBuilder::new))
                            .toString();
                    return new FileContent(fileName, title, "pptx", path.toUri().toString(),
                            lastModified, content);
                }

            };
        }
    }

    @Override
    public boolean supportExtensions(Path path, Context context) {
        String ext = FilenameUtils.getExtension(path.getFileName().toString());
        return "pptx".equalsIgnoreCase(ext);
    }

    public static void main(String[] args) throws Exception {
        SlideShowContentReader slideShowContentReader = new SlideShowContentReader();
        Iterator<FileContent> it =
                slideShowContentReader.readContent(Paths.get("G:\\文档们\\周报模板.pptx"), null);
        DebugUtils.info(it);
    }
}
