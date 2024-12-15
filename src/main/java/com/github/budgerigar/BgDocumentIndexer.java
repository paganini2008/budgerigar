package com.github.budgerigar;

import java.util.List;
import com.github.doodler.common.PageVo;

/**
 * 
 * @Description: BgDocumentIndexer
 * @Author: Fred Feng
 * @Date: 14/12/2024
 * @Version 1.0.0
 */
public interface BgDocumentIndexer {

    boolean hasDocument(String name, String title, String path);

    int saveDocument(BgDocument document);

    int persistDocument(BgDocument document);

    int updateDocument(BgDocument document);

    BgDocumentVo getDocument(int id);

    int deleteDocument();

    int deleteDocument(int id);

    int deleteDocument(BgDocumentDeleteQuery query);

    int deleteDocument(String name, String title, String path);

    List<BgDocumentVo> queryForDocument(BgDocumentQuery query);

    PageVo<BgDocumentVo> pageForDocument(BgDocumentPageQuery query);

}
