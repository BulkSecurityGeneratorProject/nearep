package cn.gov.nea.sc.repository;

import cn.gov.nea.sc.domain.Provinces;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Provinces entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProvincesRepository extends JpaRepository<Provinces,Long> {
    
}
