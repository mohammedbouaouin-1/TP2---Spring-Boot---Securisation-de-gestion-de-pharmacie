package ma.ensaf.pharma_security.repository;

import ma.ensaf.pharma_security.model.Medicament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicamentRepository extends JpaRepository<Medicament, Long> {
    List<Medicament> findByNomContainingIgnoreCase(String keyword);
    List<Medicament> findByQuantiteStockGreaterThan(int minStock);
}