package cn.gov.nea.sc.repository;

import cn.gov.nea.sc.domain.Countries;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Countries entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CountriesRepository extends JpaRepository<Countries,Long> {
    
}
