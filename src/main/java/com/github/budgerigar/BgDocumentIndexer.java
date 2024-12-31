package com.github.budgerigar;

import java.io.Serializable;
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

    boolean hasDocument(String name, String title, String ext, String path);

    int saveDocument(BgDocumentDto document);

    int persistDocument(BgDocumentDto document);

    int updateDocument(BgDocumentDto document);

    BgDocumentVo getDocument(Serializable id);

    int deleteDocument();

    int deleteDocument(Serializable id);

    int deleteDocument(BgDocumentDeleteQuery query);

    int deleteDocument(String name, String title, String ext, String path);

    List<BgDocumentVo> queryForDocument(BgDocumentQuery query);

    PageVo pageForDocument(BgDocumentPageQuery query);

}
