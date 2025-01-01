package com.github.budgerigar.elasticsearch;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.core.Map;
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
import org.springframework.data.elasticsearch.core.query.Query;
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
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Description: ElasticSearchBgDocumentIndexer
 * @Author: Fred Feng
 * @Date: 14/12/2024
 * @Version 1.0.0
 */
@Slf4j
@ConditionalOnProperty(name = "budgerigar.document.indexer", havingValue = "elasticsearch")
@Service
public class ElasticsearchBgDocumentIndexer implements BgDocumentIndexer {

    @Autowired
    private BgDocumentService bgDocumentService;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public boolean hasDocument(String name, String title, String ext, String path) {
        Query searchQuery = getIdentifiableQuery(name, title, ext, path);
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
    public int persistDocument(BgDocumentDto documentDto) {
        BgDocument existingDocument =
                bgDocumentService.searchOne(
                        getIdentifiableQuery(documentDto.getName(), documentDto.getTitle(),
                                documentDto.getExtention(), documentDto.getPath()),
                        BgDocument.class);
        if (existingDocument != null) {
            BgDocument updatedBgDocument = BeanCopyUtils.copyBean(documentDto, BgDocument.class);
            bgDocumentService.updateById(existingDocument.getId(), updatedBgDocument,
                    BgDocument.INDEX_NAME);
            log.info("Update BgDocument: {}", updatedBgDocument);
            return 0;
        } else {
            BgDocument newBgDocument = BeanCopyUtils.copyBean(documentDto, BgDocument.class);
            if (newBgDocument.getId() == null) {
                newBgDocument.setId(UUID.randomUUID().toString());
            }
            newBgDocument = bgDocumentService.save(newBgDocument, BgDocument.INDEX_NAME);
            log.info("Persist new BgDocument: {}", newBgDocument);
            return newBgDocument != null ? 1 : 0;
        }
    }

    @Override
    public int updateDocument(BgDocumentDto documentDto) {
        BgDocument bgDocument = BeanCopyUtils.copyBean(documentDto, BgDocument.class);
        bgDocumentService.updateById((String) documentDto.getId(), bgDocument,
                BgDocument.INDEX_NAME);
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

    private Query getIdentifiableQuery(String name, String title, String extenion, String path) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(name)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("name", name));
        }
        if (StringUtils.isNotBlank(title)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("title", title));
        }
        if (StringUtils.isNotBlank(extenion)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("extention", extenion));
        }
        if (StringUtils.isNotBlank(path)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("path", path));
        }
        return searchQueryBuilder.withQuery(boolQueryBuilder).build();
    }

    @Override
    public int deleteDocument(BgDocumentDeleteQuery query) {
        Query searchQuery = getIdentifiableQuery(query.getName(), query.getTitle(),
                query.getExtention(), query.getPath());
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
                    query.getKeyword(), Map.of("title", "must", "content", "should"));
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
