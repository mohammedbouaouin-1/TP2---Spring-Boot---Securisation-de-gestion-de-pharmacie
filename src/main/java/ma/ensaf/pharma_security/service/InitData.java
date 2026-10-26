package ma.ensaf.pharma_security.service;

import ma.ensaf.pharma_security.model.Medicament;
import ma.ensaf.pharma_security.model.Role;
import ma.ensaf.pharma_security.model.User;
import ma.ensaf.pharma_security.repository.MedicamentRepository;
import ma.ensaf.pharma_security.repository.RoleRepository;
import ma.ensaf.pharma_security.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Set;

@Component
public class InitData implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final MedicamentRepository medicamentRepository;
    private final PasswordEncoder passwordEncoder;

    public InitData(RoleRepository roleRepository,
                    UserRepository userRepository,
                    MedicamentRepository medicamentRepository,
                    PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.medicamentRepository = medicamentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (roleRepository.count() == 0) {
            Role roleAdmin = roleRepository.save(new Role("ADMIN"));
            Role rolePharma = roleRepository.save(new Role("PHARMACIEN"));
            Role roleCaisse = roleRepository.save(new Role("CAISSIER"));
            Role roleClient = roleRepository.save(new Role("CLIENT"));

            User admin = new User("admin", passwordEncoder.encode("admin123"), true, Set.of(roleAdmin));
            User pharma = new User("pharma", passwordEncoder.encode("pharma123"), true, Set.of(rolePharma));
            User caisse = new User("caisse", passwordEncoder.encode("caisse123"), true, Set.of(roleCaisse));
            User client = new User("client", passwordEncoder.encode("client123"), true, Set.of(roleClient));

            userRepository.save(admin);
            userRepository.save(pharma);
            userRepository.save(caisse);
            userRepository.save(client);
        }

        if (medicamentRepository.count() == 0) {
            medicamentRepository.save(new Medicament("Amoxicilline 500mg", "Antibiotique a large spectre", new BigDecimal("45.00"), 50, "AMX-500"));
            medicamentRepository.save(new Medicament("Doliprane 1000mg", "Antalgique et antipyretique", new BigDecimal("18.50"), 120, "DLP-1000"));
            medicamentRepository.save(new Medicament("Vitamine C 1000mg", "Complement nutritionnel et fortifiant", new BigDecimal("32.00"), 40, "VITC-1000"));
            medicamentRepository.save(new Medicament("Ibuprofene 400mg", "Anti-inflammatoire et douleurs", new BigDecimal("25.00"), 60, "IBU-400"));
            medicamentRepository.save(new Medicament("Betadine Solution 10%", "Antiseptique local pour plaies", new BigDecimal("22.00"), 35, "BET-010"));
        }

        System.out.println("Donnees TP 2 initialisees avec succes.");
    }
}