package com.github.budgerigar.db;

import static com.github.budgerigar.db.jooq.tables.BgrigarDocument.BGRIGAR_DOCUMENT;
import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.jooq.DSLContext;
import org.jooq.DeleteConditionStep;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SelectConditionStep;
import org.jooq.SelectForUpdateStep;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import com.github.budgerigar.BgDocumentIndexer;
import com.github.budgerigar.db.jooq.tables.records.BgrigarDocumentRecord;
import com.github.budgerigar.pojo.BgDocumentDeleteQuery;
import com.github.budgerigar.pojo.BgDocumentDto;
import com.github.budgerigar.pojo.BgDocumentPageQuery;
import com.github.budgerigar.pojo.BgDocumentQuery;
import com.github.budgerigar.pojo.BgDocumentVo;
import com.github.doodler.common.page.DefaultPageContent;
import com.github.doodler.common.page.PageContent;
import com.github.doodler.common.page.PageReader;
import com.github.doodler.common.page.PageRequest;
import com.github.doodler.common.page.PageResponse;
import com.github.doodler.common.page.PageVo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * 
 * @Description: JdbcBgDocumentIndexer
 * @Author: Fred Feng
 * @Date: 14/12/2024
 * @Version 1.0.0
 */
@ConditionalOnProperty(name = "budgerigar.document.indexer", havingValue = "jdbc",
        matchIfMissing = true)
@Component
public class JdbcBgDocumentIndexer implements BgDocumentIndexer {

    @Autowired
    private DSLContext dsl;

    @Override
    public boolean hasDocument(String name, String title, String ext, String path) {
        Integer result = dsl.selectCount().from(BGRIGAR_DOCUMENT)
                .where(BGRIGAR_DOCUMENT.NAME.eq(name).and(BGRIGAR_DOCUMENT.TITLE.eq(title))
                        .and(BGRIGAR_DOCUMENT.EXTENTION.eq(ext))
                        .and(BGRIGAR_DOCUMENT.PATH.eq(path)))
                .fetchOne(0, Integer.class);
        return result != null && result.intValue() > 0;
    }

    @Override
    public int saveDocument(BgDocumentDto document) {
        if (hasDocument(document.getName(), document.getTitle(), document.getExtention(),
                document.getPath())) {
            return updateDocument(document);
        } else {
            return persistDocument(document);
        }
    }

    @Override
    public int persistDocument(BgDocumentDto document) {
        return dsl.insertInto(BGRIGAR_DOCUMENT)
                .columns(BGRIGAR_DOCUMENT.NAME, BGRIGAR_DOCUMENT.TITLE, BGRIGAR_DOCUMENT.EXTENTION,
                        BGRIGAR_DOCUMENT.PATH, BGRIGAR_DOCUMENT.LAST_MODIFIED,
                        BGRIGAR_DOCUMENT.CONTENT)
                .values(document.getName(), document.getTitle(), document.getExtention(),
                        document.getPath(), document.getLastModified(), document.getContent())
                .returning(BGRIGAR_DOCUMENT.ID).execute();
    }

    @Override
    public int updateDocument(BgDocumentDto document) {
        BgDocumentVo vo = getDocument(document.getId());
        if (vo != null) {
            Map<?, ?> kwargs = getUpdatedFieldMap(document, vo);
            if (MapUtils.isNotEmpty(kwargs)) {
                return dsl.update(BGRIGAR_DOCUMENT).set(kwargs)
                        .where(BGRIGAR_DOCUMENT.ID.eq((Integer) document.getId())).execute();
            }
        }
        return 0;
    }

    private Map<?, ?> getUpdatedFieldMap(BgDocumentDto document, BgDocumentVo vo) {
        Map<TableField<?, ?>, Object> kwargs = new HashMap<>();
        if (!StringUtils.equals(document.getContent(), vo.getContent())) {
            kwargs.put(BGRIGAR_DOCUMENT.CONTENT, document.getContent());
        }
        if (!StringUtils.equals(document.getName(), vo.getName())) {
            kwargs.put(BGRIGAR_DOCUMENT.NAME, document.getName());
        }
        if (!StringUtils.equals(document.getTitle(), vo.getTitle())) {
            kwargs.put(BGRIGAR_DOCUMENT.TITLE, document.getTitle());
        }
        if (!StringUtils.equals(document.getPath(), vo.getPath())) {
            kwargs.put(BGRIGAR_DOCUMENT.PATH, document.getPath());
        }
        if (!StringUtils.equals(document.getExtention(), vo.getExtention())) {
            kwargs.put(BGRIGAR_DOCUMENT.EXTENTION, document.getExtention());
        }
        if (kwargs.size() > 0) {
            kwargs.put(BGRIGAR_DOCUMENT.LAST_MODIFIED, LocalDateTime.now());
        }
        return kwargs;
    }

    @Override
    public BgDocumentVo getDocument(Serializable id) {
        Record record = dsl.select().from(BGRIGAR_DOCUMENT)
                .where(BGRIGAR_DOCUMENT.ID.eq((Integer) id)).fetchOne();
        return transfer2Vo(record);
    }

    @Override
    public int deleteDocument() {
        return dsl.deleteFrom(BGRIGAR_DOCUMENT).execute();
    }

    @Override
    public int deleteDocument(Serializable id) {
        return dsl.deleteFrom(BGRIGAR_DOCUMENT).where(BGRIGAR_DOCUMENT.ID.eq((Integer) id))
                .execute();
    }

    @Override
    public int deleteDocument(String name, String title, String ext, String path) {
        DeleteConditionStep<BgrigarDocumentRecord> conditionStep =
                dsl.deleteFrom(BGRIGAR_DOCUMENT).where(BGRIGAR_DOCUMENT.NAME.eq(name));
        if (StringUtils.isNotBlank(title)) {
            conditionStep = conditionStep.and(BGRIGAR_DOCUMENT.TITLE.eq(title));
        }
        if (StringUtils.isNotBlank(ext)) {
            conditionStep = conditionStep.and(BGRIGAR_DOCUMENT.EXTENTION.eq(ext));
        }
        if (StringUtils.isNotBlank(path)) {
            conditionStep = conditionStep.and(BGRIGAR_DOCUMENT.PATH.eq(path));
        }
        return conditionStep.execute();
    }

    @Override
    public int deleteDocument(BgDocumentDeleteQuery query) {
        DeleteConditionStep<BgrigarDocumentRecord> conditionStep =
                dsl.deleteFrom(BGRIGAR_DOCUMENT).where(DSL.noCondition());
        if (StringUtils.isNotBlank(query.getName())) {
            conditionStep =
                    conditionStep.and(BGRIGAR_DOCUMENT.NAME.like("%" + query.getName() + "%"));
        }
        if (StringUtils.isNotBlank(query.getTitle())) {
            conditionStep =
                    conditionStep.and(BGRIGAR_DOCUMENT.TITLE.like("%" + query.getTitle() + "%"));
        }
        if (StringUtils.isNotBlank(query.getPath())) {
            conditionStep = conditionStep.and(BGRIGAR_DOCUMENT.PATH.eq(query.getPath()));
        }
        if (StringUtils.isNotBlank(query.getExtention())) {
            conditionStep = conditionStep.and(BGRIGAR_DOCUMENT.EXTENTION.eq(query.getExtention()));
        }
        if (query.getStartTime() != null) {
            conditionStep =
                    conditionStep.and(BGRIGAR_DOCUMENT.LAST_MODIFIED.ge(query.getStartTime()));
        }
        if (query.getEndTime() != null) {
            conditionStep =
                    conditionStep.and(BGRIGAR_DOCUMENT.LAST_MODIFIED.lt(query.getEndTime()));
        }
        return conditionStep.execute();
    }

    @Override
    public List<BgDocumentVo> queryForDocument(BgDocumentQuery query) {
        SelectForUpdateStep<org.jooq.Record> forUpdateStep = null;
        SelectConditionStep<org.jooq.Record> conditionStep = dsl.select().from(BGRIGAR_DOCUMENT)
                .where(query.isFile() ? BGRIGAR_DOCUMENT.PATH.like("file:%")
                        : BGRIGAR_DOCUMENT.PATH.notLike("file:%"));
        if (StringUtils.isNotBlank(query.getKeyword())) {
            forUpdateStep =
                    conditionStep
                            .and(BGRIGAR_DOCUMENT.NAME.like("%" + query.getKeyword() + "%")
                                    .or(BGRIGAR_DOCUMENT.TITLE.like("%" + query.getKeyword() + "%"))
                                    .or(BGRIGAR_DOCUMENT.CONTENT
                                            .like("%" + query.getKeyword() + "%")))
                            .orderBy(BGRIGAR_DOCUMENT.LAST_MODIFIED);
        }
        if (query.getMaxResults() > 0) {
            forUpdateStep = conditionStep.orderBy(BGRIGAR_DOCUMENT.LAST_MODIFIED)
                    .limit(query.getMaxResults()).offset(query.getOffset());
        } else {
            forUpdateStep = conditionStep.orderBy(BGRIGAR_DOCUMENT.LAST_MODIFIED);
        }
        Result<org.jooq.Record> records =
                forUpdateStep != null ? forUpdateStep.fetch() : conditionStep.fetch();
        if (records != null && records.size() > 0) {
            return records.stream().map(r -> transfer2Vo(r)).toList();
        }
        return Collections.emptyList();
    }

    private BgDocumentVo transfer2Vo(org.jooq.Record r) {
        if (r == null) {
            return null;
        }
        BgDocumentVo vo = new BgDocumentVo();
        vo.setId(r.get(BGRIGAR_DOCUMENT.ID));
        vo.setName(r.get(BGRIGAR_DOCUMENT.NAME));
        vo.setTitle(r.get(BGRIGAR_DOCUMENT.TITLE));
        vo.setExtention(r.get(BGRIGAR_DOCUMENT.EXTENTION));
        vo.setPath(r.get(BGRIGAR_DOCUMENT.PATH));
        vo.setLastModified(r.get(BGRIGAR_DOCUMENT.LAST_MODIFIED));
        return vo;
    }

    @SneakyThrows
    @Override
    public PageVo<BgDocumentVo> pageForDocument(BgDocumentPageQuery query) {
        BgDocumentPageReader pageReader = new BgDocumentPageReader(query);
        PageResponse<BgDocumentVo> pageResponse =
                pageReader.list(PageRequest.of(query.getPage(), query.getMaxResults()));
        PageContent<BgDocumentVo> pageContent = pageResponse.getContent();
        return new PageVo<BgDocumentVo>(pageContent.getContent(), pageResponse.getPageNumber(),
                pageResponse.getPageSize(), pageResponse.getTotalRecords(),
                pageContent.getNextToken());
    }

    @RequiredArgsConstructor
    private class BgDocumentPageReader implements PageReader<BgDocumentVo> {

        private final BgDocumentQuery query;

        @Override
        public PageContent<BgDocumentVo> list(int pageNumber, int offset, int limit,
                Object nextToken) throws SQLException {
            query.setOffset(offset);
            query.setMaxResults(limit);
            List<BgDocumentVo> list = queryForDocument(query);
            return new DefaultPageContent<>(list, null);
        }

    }

}
