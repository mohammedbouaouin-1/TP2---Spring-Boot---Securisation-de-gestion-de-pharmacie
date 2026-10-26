package ma.ensaf.pharma_security.controller;

import ma.ensaf.pharma_security.model.Medicament;
import ma.ensaf.pharma_security.repository.MedicamentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/ventes")
public class VenteController {

    private final MedicamentRepository medicamentRepository;

    public VenteController(MedicamentRepository medicamentRepository) {
        this.medicamentRepository = medicamentRepository;
    }

    @GetMapping
    public String pageVentes(Model model) {
        model.addAttribute("medicaments", medicamentRepository.findByQuantiteStockGreaterThan(0));
        return "ventes";
    }

    @PostMapping("/enregistrer")
    public String enregistrerVente(@RequestParam Long medicamentId,
                                   @RequestParam int quantite,
                                   RedirectAttributes redirectAttributes) {
        Medicament med = medicamentRepository.findById(medicamentId).orElse(null);
        if (med == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Medicament introuvable.");
            return "redirect:/ventes";
        }

        if (quantite <= 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "La quantite vendue doit etre superieure a zero.");
            return "redirect:/ventes";
        }

        if (med.getQuantiteStock() < quantite) {
            redirectAttributes.addFlashAttribute("errorMessage", "Stock insuffisant (" + med.getQuantiteStock() + " disponible).");
            return "redirect:/ventes";
        }

        med.setQuantiteStock(med.getQuantiteStock() - quantite);
        medicamentRepository.save(med);

        redirectAttributes.addFlashAttribute("successMessage", "Vente enregistree : " + quantite + " x " + med.getNom() + ".");
        return "redirect:/ventes";
    }
}