package com.example.medicalstore.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.medicalstore.entity.Medicine;
import com.example.medicalstore.entity.User;
import com.example.medicalstore.repository.MedicineRepository;
import java.util.NoSuchElementException;
@Service
public class MedicineService {

    @Autowired
    private MedicineRepository medicineRepository;

    public Medicine addMedicine(Medicine medicine, User user) {

        long count = medicineRepository.countByUser(user);

        if (count >= 5) {
            throw new RuntimeException("You can add only 5 medicines.");
        }

        medicine.setUser(user);

        medicine.setAddedTime(LocalDateTime.now());

        return medicineRepository.save(medicine);
    }

    public Page<Medicine> getMedicinesByUser(
            User user,
            Pageable pageable) {

        return medicineRepository.findByUser(user, pageable);
    }

    public Page<Medicine> searchMedicines(
            User user,
            String name,
            Pageable pageable) {

        return medicineRepository
                .findByUserAndNameContainingIgnoreCase(
                        user, name, pageable);
    }

    public void deleteMedicine(Long id, User user) {

        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException("Medicine not found."));

        if (!medicine.getUser().getId().equals(user.getId())) {
            throw new RuntimeException(
                    "You are not allowed to delete this medicine.");
        }

        medicineRepository.delete(medicine);
    }
    
    public Medicine getMedicineForUser(Long id, User user) {

        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException("Medicine not found."));

        if (!medicine.getUser().getId().equals(user.getId())) {
            throw new RuntimeException(
                    "You are not allowed to access this medicine.");
        }

        return medicine;
    }
    public Medicine updateMedicine(
            Long id,
            Medicine updatedMedicine,
            User user) {

        Medicine medicine = getMedicineForUser(id, user);

        medicine.setName(updatedMedicine.getName());
        medicine.setStock(updatedMedicine.getStock());

        return medicineRepository.save(medicine);
    }
}