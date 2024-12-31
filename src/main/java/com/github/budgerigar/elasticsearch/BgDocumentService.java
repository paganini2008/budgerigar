package com.github.budgerigar.elasticsearch;

import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;
import com.github.doodler.common.elasticsearch.BasicCrudService;

/**
 * 
 * @Description: BgDocumentService
 * @Author: Fred Feng
 * @Date: 23/12/2024
 * @Version 1.0.0
 */
@Service
public class BgDocumentService extends BasicCrudService<BgDocument> {

    public BgDocumentService(ElasticsearchRestTemplate elasticsearchTemplate) {
        super(elasticsearchTemplate);
    }

}
