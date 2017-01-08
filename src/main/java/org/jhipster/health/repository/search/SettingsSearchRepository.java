package org.jhipster.health.repository.search;

import org.jhipster.health.domain.Settings;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Settings entity.
 */
public interface SettingsSearchRepository extends ElasticsearchRepository<Settings, Long> {
}
