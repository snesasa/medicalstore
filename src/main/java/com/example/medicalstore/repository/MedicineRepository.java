package com.example.medicalstore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.medicalstore.entity.Medicine;
import com.example.medicalstore.entity.User;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    Page<Medicine> findByUser(User user, Pageable pageable);

    long countByUser(User user);

    Page<Medicine> findByUserAndNameContainingIgnoreCase(
            User user,
            String name,
            Pageable pageable);
}