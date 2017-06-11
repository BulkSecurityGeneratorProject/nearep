package cn.gov.nea.sc.repository;

import cn.gov.nea.sc.domain.Districts;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Districts entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DistrictsRepository extends JpaRepository<Districts,Long> {
    
}
