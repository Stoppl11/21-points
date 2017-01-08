package org.jhipster.health.repository;

import org.jhipster.health.domain.Settings;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Settings entity.
 */
@SuppressWarnings("unused")
public interface SettingsRepository extends JpaRepository<Settings,Long> {

}
