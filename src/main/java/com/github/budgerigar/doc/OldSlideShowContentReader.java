package com.github.budgerigar.doc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.hslf.usermodel.HSLFTextShape;
import com.github.doodler.common.Constants;

/**
 * 
 * @Description: OldSlideShowContentReader
 * @Author: Fred Feng
 * @Date: 01/12/2024
 * @Version 1.0.0
 */
public class OldSlideShowContentReader implements FileContentReader {

    @Override
    public Iterator<FileContent> readContent(Path path, Context context) throws Exception {
        try (HSLFSlideShow slideShow = new HSLFSlideShow(Files.newInputStream(path))) {
            final List<HSLFSlide> slides = slideShow.getSlides();
            if (CollectionUtils.isEmpty(slides)) {
                return IteratorUtils.emptyIterator();
            }
            return new Iterator<FileContent>() {

                String fileName = path.getFileName().toString();
                long lastModified = Files.getLastModifiedTime(path).toMillis();
                Iterator<HSLFSlide> actualIt = slides.iterator();

                @Override
                public boolean hasNext() {
                    return actualIt.hasNext();
                }

                @Override
                public FileContent next() {
                    HSLFSlide slide = actualIt.next();
                    String title = slide.getTitle();
                    String content = slide.getShapes().stream()
                            .filter(s -> s instanceof HSLFTextShape)
                            .map(s -> ((HSLFTextShape) s).getText())
                            .collect(Collectors.collectingAndThen(
                                    Collectors.joining(Constants.NEWLINE), StringBuilder::new))
                            .toString();
                    return new FileContent(fileName, title, "ppt", path.toUri().toString(),
                            lastModified, content);
                }

            };
        }
    }

    @Override
    public boolean supportExtensions(Path path, Context context) {
        String ext = FilenameUtils.getExtension(path.getFileName().toString());
        return "ppt".equalsIgnoreCase(ext);
    }
}
