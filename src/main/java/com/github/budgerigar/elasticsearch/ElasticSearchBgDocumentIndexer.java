package com.github.budgerigar.elasticsearch;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import com.github.budgerigar.BgDocumentDeleteQuery;
import com.github.budgerigar.BgDocumentDto;
import com.github.budgerigar.BgDocumentIndexer;
import com.github.budgerigar.BgDocumentPageQuery;
import com.github.budgerigar.BgDocumentQuery;
import com.github.budgerigar.BgDocumentVo;
import com.github.doodler.common.PageVo;
import com.github.doodler.common.jdbc.page.PageContent;
import com.github.doodler.common.jdbc.page.PageRequest;
import com.github.doodler.common.jdbc.page.PageResponse;
import com.github.doodler.common.utils.BeanCopyUtils;
import lombok.SneakyThrows;

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

    @Autowired
    private BgDocumentService bgDocumentService;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public boolean hasDocument(String name, String title, String ext, String path) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(name)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("name", name));
        }
        if (StringUtils.isNotBlank(title)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("title", title));
        }
        if (StringUtils.isNotBlank(ext)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("extention", ext));
        }
        if (StringUtils.isNotBlank(path)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("path", path));
        }
        NativeSearchQuery searchQuery = searchQueryBuilder.withQuery(boolQueryBuilder).build();
        return bgDocumentService.getCount(searchQuery, BgDocument.class) > 0;
    }

    @Override
    public int saveDocument(BgDocumentDto document) {
        if (document.getId() != null) {
            return updateDocument(document);
        } else {
            return persistDocument(document);
        }
    }

    @Override
    public int persistDocument(BgDocumentDto document) {
        if (document.getId() == null) {
            document.setId(UUID.randomUUID().toString());
        }
        BgDocument bgDocument = BeanCopyUtils.copyBean(document, BgDocument.class);
        bgDocument = bgDocumentService.save(bgDocument, BgDocument.INDEX_NAME);
        return bgDocument != null ? 1 : 0;
    }

    @Override
    public int updateDocument(BgDocumentDto document) {
        BgDocument bgDocument = BeanCopyUtils.copyBean(document, BgDocument.class);
        bgDocumentService.updateById((String) document.getId(), bgDocument, BgDocument.INDEX_NAME);
        return 1;
    }

    @Override
    public BgDocumentVo getDocument(Serializable id) {
        BgDocument bgDocument = bgDocumentService.getById((String) id, BgDocument.class);
        return bgDocument != null ? BeanCopyUtils.copyBean(bgDocument, BgDocumentVo.class) : null;
    }

    @Override
    public int deleteDocument() {
        return (int) bgDocumentService.deleteAll(BgDocument.class);
    }

    @Override
    public int deleteDocument(Serializable id) {
        id = bgDocumentService.deleteById((String) id, BgDocument.INDEX_NAME);
        return id != null ? 1 : 0;
    }

    @Override
    public int deleteDocument(BgDocumentDeleteQuery query) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(query.getName())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("name", query.getName()));
        }
        if (StringUtils.isNotBlank(query.getTitle())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("title", query.getTitle()));
        }
        if (StringUtils.isNotBlank(query.getExtention())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("extention", query.getExtention()));
        }
        if (StringUtils.isNotBlank(query.getPath())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("path", query.getPath()));
        }
        NativeSearchQuery searchQuery = searchQueryBuilder.withQuery(boolQueryBuilder).build();
        return (int) bgDocumentService.delete(searchQuery, BgDocument.class);
    }

    @Override
    public List<BgDocumentVo> queryForDocument(BgDocumentQuery query) {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        configureQueryBuilder(queryBuilder, query);
        NativeSearchQuery searchQuery = queryBuilder.build();
        List<BgDocument> list = bgDocumentService.search(searchQuery, BgDocument.class);
        return BeanCopyUtils.copyBeanList(list, BgDocumentVo.class);
    }

    private void configureQueryBuilder(NativeSearchQueryBuilder queryBuilder,
            BgDocumentQuery query) {
        String keyword = query.getKeyword();
        if (StringUtils.isNotBlank(keyword)) {
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            boolQueryBuilder.should(QueryBuilders.matchQuery("name", keyword))
                    .should(QueryBuilders.matchQuery("title", keyword))
                    .should(QueryBuilders.matchQuery("content", keyword));
            queryBuilder.withQuery(boolQueryBuilder);
        }
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (query.isFile()) {
            boolQueryBuilder.filter(QueryBuilders.prefixQuery("path", "file:"));
        }
        if (StringUtils.isNotBlank(query.getExtension())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("extension", query.getExtension()));
        }
        if (query.getMaxResults() > 0) {
            queryBuilder.withMaxResults(query.getMaxResults());
        }
    }

    @SneakyThrows
    @Override
    public PageVo pageForDocument(BgDocumentPageQuery query) {
        DefaultElasticsearchPageReader elasticsearchPageReader = new DefaultElasticsearchPageReader(
                query, BgDocument.class, elasticsearchRestTemplate);
        PageResponse<Object> pageResponse = elasticsearchPageReader
                .list(PageRequest.of(query.getPage(), query.getMaxResults()));
        PageVo pageVo = new PageVo();
        PageContent<Object> pageContent = pageResponse.getContent();
        pageVo.setPage(pageResponse.getPageNumber());
        pageVo.setPageSize(pageResponse.getPageSize());
        pageVo.setTotalRecords(pageResponse.getTotalRecords());
        pageVo.setNextToken(pageContent.getNextToken());
        pageVo.setContent(pageContent.getContent());
        return pageVo;
    }

    private class DefaultElasticsearchPageReader extends BasicElasticsearchPageReader<BgDocument> {

        DefaultElasticsearchPageReader(BgDocumentQuery query, Class<BgDocument> documentClass,
                ElasticsearchRestTemplate elasticsearchRestTemplate) {
            super(documentClass, elasticsearchRestTemplate);
            this.query = query;
        }

        private final BgDocumentQuery query;

        @Override
        protected void configureQueryBuilder(NativeSearchQueryBuilder searchQueryBuilder) {
            ElasticSearchBgDocumentIndexer.this.configureQueryBuilder(searchQueryBuilder, query);
        }

        @Override
        protected Object convertObject(BgDocument document) {
            return BeanCopyUtils.copyBean(document, BgDocumentVo.class);
        }

    }

    @Override
    public int deleteDocument(String name, String title, String ext, String path) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(name)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("name", name));
        }
        if (StringUtils.isNotBlank(title)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("title", title));
        }
        if (StringUtils.isNotBlank(ext)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("extention", ext));
        }
        if (StringUtils.isNotBlank(path)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("path", path));
        }
        NativeSearchQuery searchQuery = searchQueryBuilder.withQuery(boolQueryBuilder).build();
        return (int) bgDocumentService.delete(searchQuery, BgDocument.class);
    }



}
