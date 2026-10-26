package ma.ensaf.pharma_security.controller;

import ma.ensaf.pharma_security.model.Role;
import ma.ensaf.pharma_security.model.User;
import ma.ensaf.pharma_security.repository.RoleRepository;
import ma.ensaf.pharma_security.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String adminDashboard(Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("roles", roleRepository.findAll());
        return "admin";
    }

    @PostMapping("/users/save")
    public String saveUser(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String roleName,
                           RedirectAttributes redirectAttributes) {
        if (userRepository.findByUsername(username).isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Le nom d'utilisateur existe deja.");
            return "redirect:/admin";
        }

        Role role = roleRepository.findByName(roleName).orElse(null);
        if (role == null) {
            role = roleRepository.save(new Role(roleName));
        }

        User user = new User(username, passwordEncoder.encode(password), true, Set.of(role));
        userRepository.save(user);

        redirectAttributes.addFlashAttribute("successMessage", "Utilisateur " + username + " cree avec succes.");
        return "redirect:/admin";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userRepository.findById(id).ifPresent(user -> {
            if (!"admin".equals(user.getUsername())) {
                userRepository.delete(user);
                redirectAttributes.addFlashAttribute("successMessage", "Utilisateur supprime.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Impossible de supprimer l'administrateur principal.");
            }
        });
        return "redirect:/admin";
    }
}