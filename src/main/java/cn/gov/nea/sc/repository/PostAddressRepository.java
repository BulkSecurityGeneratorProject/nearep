package cn.gov.nea.sc.repository;

import cn.gov.nea.sc.domain.PostAddress;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PostAddress entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PostAddressRepository extends JpaRepository<PostAddress,Long> {
    
}
