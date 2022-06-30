package com.falconx.channelling.controller;

import com.falconx.channelling.dao.IssuePrescriptionDAO;
import com.falconx.channelling.dao.PrescriptionDAO;
import com.falconx.channelling.dto.PrescriptionDTO;
import com.falconx.channelling.service.PrescriptionServiceImpl;
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
@RequestMapping("/prescription")
@CrossOrigin(origins = "*")
public class PrescriptionController {

    private PrescriptionServiceImpl prescriptionService;

    public PrescriptionController(PrescriptionServiceImpl prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_DOCTOR') or hasRole('ROLE_PHARMACIST')")
    public ResponseEntity<List<PrescriptionDTO>> getAllPrescription(@RequestParam(name = "issued") String issued) {
        try {
            ResponseEntity<List<PrescriptionDTO>> medicineResponseEntity = prescriptionService.getAllPrescription(Boolean.parseBoolean(issued));
            return medicineResponseEntity;
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    public ResponseEntity<PrescriptionDTO> createPrescription(@RequestBody PrescriptionDAO prescriptionDAO) {
        try {
            ResponseEntity<PrescriptionDTO> medicineResponseEntity = prescriptionService.createPrescription(prescriptionDAO);
            return medicineResponseEntity;
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PutMapping("/issue/{prescription-id}")
    @PreAuthorize("hasRole('ROLE_PHARMACIST')")
    public ResponseEntity<PrescriptionDTO> issuePharmacyItem(@PathVariable("prescription-id") String prescriptionId,
                                                             @RequestBody List<IssuePrescriptionDAO> issuePrescriptionList) {
        try {
            ResponseEntity<PrescriptionDTO> medicineResponseEntity = prescriptionService.issuePharmacyItem(Long.parseLong(prescriptionId), issuePrescriptionList);
            return medicineResponseEntity;
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @DeleteMapping("/{prescription-id}")
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    public ResponseEntity<Void> deleteItem(@PathVariable("prescription-id") String prescriptionId) {
        try {
            ResponseEntity<Void> medicineResponseEntity = prescriptionService.deletePrescription(Long.parseLong(prescriptionId));
            return medicineResponseEntity;
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

}
