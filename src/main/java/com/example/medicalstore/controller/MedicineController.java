package com.example.medicalstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.medicalstore.entity.Medicine;
import com.example.medicalstore.entity.User;
import com.example.medicalstore.service.MedicineService;

import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
@Controller
public class MedicineController {

    @Autowired
    private MedicineService medicineService;
    
    @GetMapping("/medicines")
    public String showMedicines(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "") String search,
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        Pageable pageable = PageRequest.of(page, 5);

        Page<Medicine> medicines;

        if (search.isBlank()) {
            medicines = medicineService
                    .getMedicinesByUser(user, pageable);
        } else {
            medicines = medicineService
                    .searchMedicines(user, search, pageable);
        }

        model.addAttribute("medicines", medicines);
        model.addAttribute("search", search);

        return "medicines";
    }

    @GetMapping("/medicines/add")
    public String showAddMedicinePage(HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        return "add-medicine";
    }

    @PostMapping("/medicines/add")
    public String addMedicine(
            @ModelAttribute Medicine medicine,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        try {
            medicineService.addMedicine(medicine, user);

            return "redirect:/medicines";

        } catch (RuntimeException e) {

            redirectAttributes.addFlashAttribute(
                    "error", e.getMessage());

            return "redirect:/medicines/add";
        }
    }
    
    @GetMapping("/medicines/delete/{id}")
    public String deleteMedicine(
            @PathVariable Long id,
            HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        try {

            medicineService.deleteMedicine(id, user);

        } catch (RuntimeException e) {

            // Ignore for now and return to medicine list
        }

        return "redirect:/medicines";
    }
    @GetMapping("/medicines/edit/{id}")
    public String showEditMedicinePage(
            @PathVariable Long id,
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        try {

            Medicine medicine =
                    medicineService.getMedicineForUser(id, user);

            model.addAttribute("medicine", medicine);

            return "edit-medicine";

        } catch (RuntimeException e) {

            return "redirect:/medicines";
        }
    }
    @PostMapping("/medicines/edit/{id}")
    public String updateMedicine(
            @PathVariable Long id,
            @ModelAttribute Medicine medicine,
            HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        try {

            medicineService.updateMedicine(
                    id,
                    medicine,
                    user);

        } catch (RuntimeException e) {

            return "redirect:/medicines";
        }

        return "redirect:/medicines";
    }
}