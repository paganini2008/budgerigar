package com.github.budgerigar.es;

import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import com.github.budgerigar.BgDocument;
import com.github.budgerigar.BgDocumentDeleteQuery;
import com.github.budgerigar.BgDocumentIndexer;
import com.github.budgerigar.BgDocumentPageQuery;
import com.github.budgerigar.BgDocumentQuery;
import com.github.budgerigar.BgDocumentVo;
import com.github.doodler.common.PageVo;

/**
 * 
 * @Description: ElasticSearchBgDocumentIndexer
 * @Author: Fred Feng
 * @Date: 14/12/2024
 * @Version 1.0.0
 */
@ConditionalOnProperty(name = "budgerigar.document.indexer", havingValue = "elasticsearch")
@Service
public class ElasticSearchBgDocumentIndexer implements BgDocumentIndexer {

    @Override
    public boolean hasDocument(String name, String title, String path) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int saveDocument(BgDocument document) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int persistDocument(BgDocument document) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int updateDocument(BgDocument document) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public BgDocumentVo getDocument(int id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int deleteDocument() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int deleteDocument(int id) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int deleteDocument(BgDocumentDeleteQuery query) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<BgDocumentVo> queryForDocument(BgDocumentQuery query) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PageVo<BgDocumentVo> pageForDocument(BgDocumentPageQuery query) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int deleteDocument(String name, String title, String path) {
        // TODO Auto-generated method stub
        return 0;
    }

}
