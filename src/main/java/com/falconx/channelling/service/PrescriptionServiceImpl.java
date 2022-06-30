package com.falconx.channelling.service;

import com.falconx.channelling.dao.IssuePrescriptionDAO;
import com.falconx.channelling.dao.MedicineDAO;
import com.falconx.channelling.dao.PrescriptionDAO;
import com.falconx.channelling.dto.PrescriptionDTO;
import com.falconx.channelling.entities.Doctor;
import com.falconx.channelling.entities.Patient;
import com.falconx.channelling.entities.PharmacyItem;
import com.falconx.channelling.entities.Prescription;
import com.falconx.channelling.entities.PrescriptionItem;
import com.falconx.channelling.repository.DoctorRepository;
import com.falconx.channelling.repository.PatientRepository;
import com.falconx.channelling.repository.PharmacyItemRepository;
import com.falconx.channelling.repository.PrescriptionItemRepository;
import com.falconx.channelling.repository.PrescriptionRepository;
import com.falconx.channelling.util.DateTimeUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class PrescriptionServiceImpl {

    private final PrescriptionRepository prescriptionRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final PharmacyItemRepository pharmacyItemRepository;
    private final PrescriptionItemRepository prescriptionItemRepository;
    private final DoctorServiceImpl doctorService;

    public PrescriptionServiceImpl(PrescriptionRepository prescriptionRepository, DoctorRepository doctorRepository,
                                   PatientRepository patientRepository, PharmacyItemRepository pharmacyItemRepository,
                                   PrescriptionItemRepository prescriptionItemRepository, DoctorServiceImpl doctorService) {
        this.prescriptionRepository = prescriptionRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.pharmacyItemRepository = pharmacyItemRepository;
        this.prescriptionItemRepository = prescriptionItemRepository;
        this.doctorService = doctorService;
    }

    public ResponseEntity<List<PrescriptionDTO>> getAllPrescription(boolean issued) {
        List<PrescriptionDTO> prescriptionList = new ArrayList<>();
        try {
            List<Prescription> allByIssuePharmacyItem = prescriptionRepository.findAllByIssuePharmacyItem(issued);
            allByIssuePharmacyItem.forEach(prescription -> {
                prescriptionList.add(mapPrescription(prescription, 0));
            });
            return new ResponseEntity(prescriptionList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public ResponseEntity<PrescriptionDTO> createPrescription(PrescriptionDAO prescriptionDAO) {

        List<PrescriptionItem> prescriptionItemList = new ArrayList<>();

        try {
            Prescription prescription = new Prescription();

            Optional<Doctor> doctorById = doctorRepository.findById(Long.parseLong(prescriptionDAO.getDoctorId()));
            Optional<Patient> patientById = patientRepository.findById(Long.parseLong(prescriptionDAO.getPatientId()));

            if(doctorById.isPresent()){
                if(patientById.isPresent()){
                    prescription.setDoctor(doctorById.get());
                    prescription.setPatient(patientById.get());
                    List<MedicineDAO> medicineList = prescriptionDAO.getMedicineList();

                    medicineList.forEach(medicineDAO -> {
                        PrescriptionItem prescriptionItem = new PrescriptionItem();
                        PharmacyItem pharmacyItem = pharmacyItemRepository.findById(medicineDAO.getPharmacyItemId());
                        prescriptionItem.setPharmacyItem(pharmacyItem);
                        prescriptionItem.setNumberOfDays(medicineDAO.getNumberOfDays());
                        prescriptionItem.setTimeOfDays(medicineDAO.getTimeOfDays());
                        PrescriptionItem savedPrescriptionItem = prescriptionItemRepository.save(prescriptionItem);
                        prescriptionItemList.add(savedPrescriptionItem);
                    });

                    prescription.setPrescriptionItemList(prescriptionItemList);
                    prescription.setTime(DateTimeUtil.getCurrentTime());
                    prescription.setDate(DateTimeUtil.getCurrentDate());
                    Prescription registeredPrescription = prescriptionRepository.save(prescription);
                    PrescriptionDTO prescriptionDTO = mapPrescription(registeredPrescription, 0);
                    return new ResponseEntity<>(prescriptionDTO, HttpStatus.OK);
                }
                return new ResponseEntity("no patient found with provided information", HttpStatus.OK);
            }

            return new ResponseEntity("no doctor found with provided information", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public ResponseEntity<PrescriptionDTO> issuePharmacyItem(long prescriptionId, List<IssuePrescriptionDAO> issuePrescriptionList) {

        AtomicReference<Double> totalCost = new AtomicReference<>(0.0);

        try {
            Optional<Prescription>  prescription = prescriptionRepository.findById(prescriptionId);
            if(prescription.isPresent()){
                issuePrescriptionList.forEach(issuePrescriptionDAO -> {
                    prescription.get().getPrescriptionItemList().forEach(prescriptionItem -> {
                        if(prescriptionItem.getId() == issuePrescriptionDAO.getId()){
                            int remain = prescriptionItem.getPharmacyItem().getQuantity() - issuePrescriptionDAO.getQuantity();
                            if(remain > 0) {
                                prescriptionItem.getPharmacyItem().setQuantity(remain);
                                prescriptionItem.setIssued(issuePrescriptionDAO.isIssued());
                                prescriptionItemRepository.save(prescriptionItem);
                                totalCost.set(totalCost.get() + issuePrescriptionDAO.getQuantity() * prescriptionItem.getPharmacyItem().getPrice());
                            }
                        }
                    });
                });
                prescription.get().setIssuePharmacyItem(true);
                Prescription updated = prescriptionRepository.save(prescription.get());
                PrescriptionDTO prescriptionDTO = mapPrescription(updated, totalCost.get());
                return new ResponseEntity<>(prescriptionDTO, HttpStatus.OK);
            }
            return new ResponseEntity("Issue failed", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public ResponseEntity<Void> deletePrescription(long prescriptionId) {
        try {
            prescriptionRepository.deleteById(prescriptionId);
            return new ResponseEntity("deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public PrescriptionDTO mapPrescription(Prescription prescription, double totalCost){
        PrescriptionDTO prescriptionDTO = new PrescriptionDTO();

        prescriptionDTO.setId(prescription.getId());
        prescriptionDTO.setDate(prescription.getDate());
        prescriptionDTO.setTime(prescription.getTime());
        prescriptionDTO.setDoctorData(doctorService.mapDoctorData(prescription.getDoctor()));
        prescriptionDTO.setIssuePharmacyItem(prescription.isIssuePharmacyItem());
        prescriptionDTO.setPrescriptionItemList(prescription.getPrescriptionItemList());
        prescriptionDTO.setTotalCost(totalCost);
        prescriptionDTO.setPatient(prescription.getPatient());

        return prescriptionDTO;
    }

}
