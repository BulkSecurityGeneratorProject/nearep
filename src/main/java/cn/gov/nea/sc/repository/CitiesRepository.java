package cn.gov.nea.sc.repository;

import cn.gov.nea.sc.domain.Cities;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Cities entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CitiesRepository extends JpaRepository<Cities,Long> {
    
}
