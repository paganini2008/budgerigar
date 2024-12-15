package com.github.budgerigar.doc;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import org.apache.commons.io.FilenameUtils;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.text.TextContentRenderer;

/**
 * 
 * @Description: MarkdownContentReader
 * @Author: Fred Feng
 * @Date: 30/11/2024
 * @Version 1.0.0
 */
public class MarkdownContentReader implements FileContentReader {

    @Override
    public Iterator<FileContent> readContent(Path path, Context context) throws IOException {
        Charset charset = (Charset) context.getAttr(Context.FILE_CHARSET, defaultCharset);
        Parser parser = Parser.builder().build();
        Node document = parser.parseReader(Files.newBufferedReader(path, charset));
        TextContentRenderer renderer = TextContentRenderer.builder().build();
        return new SingleElementIterator<FileContent>(
                new FileContent(path, renderer.render(document)));

    }

    @Override
    public boolean supportExtensions(Path path, Context context) {
        String ext = FilenameUtils.getExtension(path.getFileName().toString());
        return "md".equalsIgnoreCase(ext);
    }

    public static void main(String[] args) throws IOException {
        MarkdownContentReader contentReader = new MarkdownContentReader();
        Iterator<FileContent> content =
                contentReader.readContent(Paths.get("G:\\租户数据源使用说明.md"), new Context());
        System.out.println(content);

    }

}
