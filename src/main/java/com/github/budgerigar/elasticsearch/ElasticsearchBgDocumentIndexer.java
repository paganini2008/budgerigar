package com.github.budgerigar.elasticsearch;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import com.github.budgerigar.BgDocumentIndexer;
import com.github.budgerigar.pojo.BgDocumentDeleteQuery;
import com.github.budgerigar.pojo.BgDocumentDto;
import com.github.budgerigar.pojo.BgDocumentPageQuery;
import com.github.budgerigar.pojo.BgDocumentQuery;
import com.github.budgerigar.pojo.BgDocumentVo;
import com.github.doodler.common.PageVo;
import com.github.doodler.common.elasticsearch.KeywordBasedElasticsearchPageReader;
import com.github.doodler.common.page.PageContent;
import com.github.doodler.common.page.PageRequest;
import com.github.doodler.common.page.PageResponse;
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
public class ElasticsearchBgDocumentIndexer implements BgDocumentIndexer {

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
            boolQueryBuilder.should(QueryBuilders.matchQuery("title", keyword))
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
        DefaultElasticsearchPageReader elasticsearchPageReader =
                new DefaultElasticsearchPageReader(elasticsearchRestTemplate, query);
        PageResponse<BgDocumentVo> pageResponse = elasticsearchPageReader
                .list(PageRequest.of(query.getPage(), query.getMaxResults()));
        PageVo pageVo = new PageVo();
        PageContent<BgDocumentVo> pageContent = pageResponse.getContent();
        pageVo.setPage(pageResponse.getPageNumber());
        pageVo.setPageSize(pageResponse.getPageSize());
        pageVo.setTotalRecords(pageResponse.getTotalRecords());
        pageVo.setNextToken(pageContent.getNextToken());
        pageVo.setContent(pageContent.getContent());
        return pageVo;
    }

    /**
     * 
     * @Description: DefaultElasticsearchPageReader
     * @Author: Fred Feng
     * @Date: 01/01/2025
     * @Version 1.0.0
     */
    private static class DefaultElasticsearchPageReader
            extends KeywordBasedElasticsearchPageReader<BgDocument, BgDocumentVo> {

        DefaultElasticsearchPageReader(ElasticsearchRestTemplate elasticsearchRestTemplate,
                BgDocumentPageQuery query) {
            super(elasticsearchRestTemplate, BgDocument.class, BgDocumentVo.class,
                    query.getKeyword(), new String[] {"title", "content"});
            this.query = query;
        }

        private final BgDocumentPageQuery query;

        @Override
        protected QueryBuilder getFilter() {
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            if (query.isFile()) {
                boolQueryBuilder.filter(QueryBuilders.prefixQuery("path", "file:"));
            }
            if (StringUtils.isNotBlank(query.getExtension())) {
                boolQueryBuilder.filter(QueryBuilders.termQuery("extension", query.getExtension()));
            }
            return boolQueryBuilder;
        }

        @Override
        protected FieldSortBuilder getSort() {
            return SortBuilders.fieldSort("lastModified").order(SortOrder.DESC);
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
