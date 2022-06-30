package com.falconx.channelling.service;

import com.falconx.channelling.entities.PharmacyItem;
import com.falconx.channelling.repository.PharmacyItemRepository;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PharmacyItemServiceImpl {

    private final PharmacyItemRepository pharmacyRepository;

    PharmacyItemServiceImpl(PharmacyItemRepository pharmacyItemRepository){
        this.pharmacyRepository = pharmacyItemRepository;
    }

    public ResponseEntity<PharmacyItem> addPharmacyItem(PharmacyItem pharmacyItem) {
        try {
            PharmacyItem registeredPharmacyItem = pharmacyRepository.save(pharmacyItem);
            return new ResponseEntity<>(registeredPharmacyItem, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public ResponseEntity<List<PharmacyItem>> getAllPharmacyItem() {
        try {
            Iterable<PharmacyItem> medicineIterable = pharmacyRepository.findAll();
            List<PharmacyItem> pharmacyItemList = Lists.newArrayList(medicineIterable);
            return new ResponseEntity<>(pharmacyItemList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public ResponseEntity<PharmacyItem> updatePharmacyItem(PharmacyItem pharmacyItem) {
        try {
            PharmacyItem registeredPharmacyItem = pharmacyRepository.save(pharmacyItem);
            return new ResponseEntity<>(registeredPharmacyItem, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public ResponseEntity<List<PharmacyItem>> searchPharmacyItem(String itemName) {
        try {
            List<PharmacyItem> pharmacyItemList = pharmacyRepository.findByNameLike(itemName);
            return new ResponseEntity<>(pharmacyItemList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public ResponseEntity<Void> deletePharmacyItem(long pharmacyItemId) {
        try {
            pharmacyRepository.deleteById(pharmacyItemId);
            return new ResponseEntity("deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


}
