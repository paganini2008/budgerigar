package com.github.budgerigar.elasticsearch;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import com.github.doodler.common.jdbc.page.DefaultPageContent;
import com.github.doodler.common.jdbc.page.PageContent;
import com.github.doodler.common.jdbc.page.PageReader;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @Description: BasicElasticsearchPageReader
 * @Author: Fred Feng
 * @Date: 23/12/2024
 * @Version 1.0.0
 */
@RequiredArgsConstructor
public abstract class BasicElasticsearchPageReader<D> implements PageReader<Object> {

    private final Class<D> documentClass;
    private final ElasticsearchRestTemplate elasticsearchRestTemplate;

    private String[] highlightFields;
    private String[] highlightTags = {"<font class=\"searchKeyword\">", "</font>"};

    public void setHighlightFields(String[] highlightFields) {
        this.highlightFields = highlightFields;
    }

    public void setHighlightTags(String[] highlightTags) {
        this.highlightTags = highlightTags;
    }

    @Override
    public PageContent<Object> list(int pageNumber, int offset, int limit, Object nextToken)
            throws SQLException {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withQuery(QueryBuilders.matchAllQuery());
        searchQueryBuilder.withPageable(PageRequest.of(pageNumber - 1, limit));
        if (ArrayUtils.isNotEmpty(highlightFields)) {
            for (String hField : highlightFields) {
                searchQueryBuilder.withHighlightFields(new HighlightBuilder.Field(hField));
            }
            searchQueryBuilder.withHighlightBuilder(new HighlightBuilder().requireFieldMatch(false)
                    .preTags(highlightTags[0]).postTags(highlightTags[1])
                    .fragmentSize(Integer.MAX_VALUE).numOfFragments(3));
        }
        configureQueryBuilder(searchQueryBuilder);
        SearchHits<D> hits =
                elasticsearchRestTemplate.search(searchQueryBuilder.build(), documentClass);
        if (hits.isEmpty()) {
            return new DefaultPageContent<>();
        }
        List<Object> dataList = new ArrayList<Object>();
        for (SearchHit<D> hit : hits.getSearchHits()) {
            dataList.add(convertObject(hit.getContent()));
        }
        return new DefaultPageContent<>(dataList, nextToken);
    }

    protected void configureQueryBuilder(NativeSearchQueryBuilder searchQueryBuilder) {}

    protected Object convertObject(D document) {
        return document;
    }

}
