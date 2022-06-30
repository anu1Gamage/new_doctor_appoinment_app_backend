package com.falconx.channelling.repository;

import com.falconx.channelling.entities.PharmacyItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PharmacyItemRepository extends CrudRepository<PharmacyItem, Long> {

    PharmacyItem findById(long id);

    @Query("Select i from PharmacyItem i where i.name like %:itemName%")
    List<PharmacyItem> findByNameLike(String itemName);

}
