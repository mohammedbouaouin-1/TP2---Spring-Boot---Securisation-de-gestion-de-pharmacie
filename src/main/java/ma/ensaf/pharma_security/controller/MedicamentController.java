package ma.ensaf.pharma_security.controller;

import ma.ensaf.pharma_security.model.Medicament;
import ma.ensaf.pharma_security.repository.MedicamentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/medicaments")
public class MedicamentController {

    private final MedicamentRepository medicamentRepository;

    public MedicamentController(MedicamentRepository medicamentRepository) {
        this.medicamentRepository = medicamentRepository;
    }

    @GetMapping
    public String listMedicaments(@RequestParam(required = false) String search, Model model) {
        if (search != null && !search.trim().isEmpty()) {
            model.addAttribute("medicaments", medicamentRepository.findByNomContainingIgnoreCase(search));
        } else {
            model.addAttribute("medicaments", medicamentRepository.findAll());
        }
        model.addAttribute("searchTerm", search);
        return "medicaments";
    }

    @GetMapping("/nouveau")
    public String nouveauMedicamentForm(Model model) {
        model.addAttribute("medicament", new Medicament());
        model.addAttribute("isEdit", false);
        return "medicament-form";
    }

    @GetMapping("/modifier/{id}")
    public String modifierMedicamentForm(@PathVariable Long id, Model model) {
        Medicament med = medicamentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Medicament non trouve id: " + id));
        model.addAttribute("medicament", med);
        model.addAttribute("isEdit", true);
        return "medicament-form";
    }

    @PostMapping("/save")
    public String saveMedicament(@ModelAttribute("medicament") Medicament medicament,
                                 RedirectAttributes redirectAttributes) {
        medicamentRepository.save(medicament);
        redirectAttributes.addFlashAttribute("successMessage", "Medicament enregistre avec succes.");
        return "redirect:/medicaments";
    }

    @GetMapping("/supprimer/{id}")
    public String supprimerMedicament(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        medicamentRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Medicament supprime.");
        return "redirect:/medicaments";
    }
}