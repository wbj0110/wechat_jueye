package com.jueye.es.repository;

import com.jueye.es.entity.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Created by soledede on 2018/1/1.
 */
//泛型的参数分别是实体类型和主键类型
public interface ArticleSearchRepository extends ElasticsearchRepository<Article, Long> {
}
