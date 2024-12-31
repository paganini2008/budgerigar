package com.github.budgerigar.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 
 * @Description: BgDocumentRepository
 * @Author: Fred Feng
 * @Date: 22/12/2024
 * @Version 1.0.0
 */
public interface BgDocumentRepository extends ElasticsearchRepository<BgDocument, String> {

}
