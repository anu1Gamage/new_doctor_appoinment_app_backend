package com.falconx.channelling.controller;

import com.falconx.channelling.dao.PharmacyItemDAO;
import com.falconx.channelling.entities.PharmacyItem;
import com.falconx.channelling.service.PharmacyItemServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pharmacy-item")
@CrossOrigin(origins = "*")
public class PharmacyItemController {

    private final ModelMapper modelMapper;
    private final PharmacyItemServiceImpl pharmacyItemService;

    public PharmacyItemController(ModelMapper modelMapper, PharmacyItemServiceImpl pharmacyItemService) {
        this.modelMapper = modelMapper;
        this.pharmacyItemService = pharmacyItemService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_PHARMACIST')")
    public ResponseEntity<PharmacyItem> registerPharmacyItem(@RequestBody PharmacyItemDAO pharmacyItemDAO) {
        try {
            ResponseEntity<PharmacyItem> medicineResponseEntity = pharmacyItemService.addPharmacyItem(modelMapper.map(pharmacyItemDAO, PharmacyItem.class));
            return medicineResponseEntity;
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_DOCTOR') or hasRole('ROLE_PHARMACIST')")
    public ResponseEntity<List<PharmacyItem>> getAllPharmacyItem() {
        try {
            ResponseEntity<List<PharmacyItem>> medicineResponseEntity = pharmacyItemService.getAllPharmacyItem();
            return medicineResponseEntity;
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

//    @PostMapping("/issue-prescription/{prescription-id}")
//    @PreAuthorize("hasRole('ROLE_PHARMACIST')")
//    public ResponseEntity<PharmacyItem> issuePharmacyItem(@PathVariable("prescription-id") String prescriptionId) {
//        try {
//            ResponseEntity<PharmacyItem> medicineResponseEntity = pharmacyItemService.addPharmacyItem(modelMapper.map(pharmacyItemDAO, PharmacyItem.class));
//            return medicineResponseEntity;
//        } catch (Exception e) {
//            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
//        }
//    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_PHARMACIST')")
    public ResponseEntity<PharmacyItem> updatePharmacyItem(@RequestBody PharmacyItemDAO pharmacyItemDAO) {
        try {
            ResponseEntity<PharmacyItem> medicineResponseEntity = pharmacyItemService.updatePharmacyItem(modelMapper.map(pharmacyItemDAO, PharmacyItem.class));
            return medicineResponseEntity;
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ROLE_PHARMACIST')")
    public ResponseEntity<List<PharmacyItem>> searchPharmacyItems(@RequestParam(name = "search-key") String pharmacyItem) {
        try {
            ResponseEntity<List<PharmacyItem>> medicineResponseEntity = pharmacyItemService.searchPharmacyItem(pharmacyItem);
            return medicineResponseEntity;
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @DeleteMapping("/{pharmacy-item-id}")
    @PreAuthorize("hasRole('ROLE_PHARMACIST')")
    public ResponseEntity<Void> deletePharmacyItem(@PathVariable("pharmacy-item-id") String pharmacyItemId) {
        try {
            ResponseEntity<Void> medicineResponseEntity = pharmacyItemService.deletePharmacyItem(Long.parseLong(pharmacyItemId));
            return medicineResponseEntity;
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

}
