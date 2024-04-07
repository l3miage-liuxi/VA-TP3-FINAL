package fr.uga.l3miage.spring.tp3.exo1;

import fr.uga.l3miage.spring.tp3.enums.TestCenterCode;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.models.TestCenterEntity;
import fr.uga.l3miage.spring.tp3.repositories.CandidateEvaluationGridRepository;
import fr.uga.l3miage.spring.tp3.repositories.CandidateRepository;
import fr.uga.l3miage.spring.tp3.repositories.TestCenterRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Set;

import static fr.uga.l3miage.spring.tp3.enums.TestCenterCode.GRE;
import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
public class CandidateRepositoryTest {
    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private TestCenterRepository testCenterRepository;

    @Autowired
    private CandidateEvaluationGridRepository candidateEvaluationGridRepository;
    @Test
    void testRequestFindAllByTestCenterEntityCode(){
        //creer testcenter
        TestCenterEntity testCenter = TestCenterEntity
                .builder()
                .code(GRE)
                .build();
        testCenterRepository.save(testCenter);

        //creer candidate
        CandidateEntity candidate1 = CandidateEntity
                .builder()
                .birthDate(LocalDate.of(1999,6,8))
                .testCenterEntity(testCenter)
                .email("allanWang@gmail.com")
                .build();
        CandidateEntity candidate2 = CandidateEntity
                .builder()
                .birthDate(LocalDate.of(2000,5,21))
                .testCenterEntity(testCenter)
                .email("xiaomanLIU@hotmail.com")
                .build();
        CandidateEntity candidate3 = CandidateEntity
                .builder()
                .birthDate(LocalDate.of(1998,9,2))
                .testCenterEntity(testCenter)
                .email("Iris2111@hotmail.com")
                .build();
        CandidateEntity candidate4 = CandidateEntity
                .builder()
                .birthDate(LocalDate.of(1997,11,21))
                .email("ye4321@gmail.com")
                .testCenterEntity(testCenter)
                .build();

        candidateRepository.save(candidate1);
        candidateRepository.save(candidate2);
        candidateRepository.save(candidate3);
        candidateRepository.save(candidate4);

        Set<CandidateEntity> results = candidateRepository.findAllByTestCenterEntityCode(testCenter.getCode());

        assertThat(results).hasSize(4);
        assertThat(results)
                .extracting(CandidateEntity::getBirthDate)
                .containsExactlyInAnyOrder(LocalDate.of(1999,6,8), LocalDate.of(2000,5,21), LocalDate.of(1998,9,2), LocalDate.of(1997,11,21));
    }

    @Test
    void testRequestfindAllByCandidateEvaluationGridEntitiesGradeLessThan(){
        CandidateEntity candidate1 = CandidateEntity
                .builder()
                .birthDate(LocalDate.of(1999,6,8))
                .email("allanWang@gmail.com")
                .build();

        CandidateEntity candidate2 = CandidateEntity
                .builder()
                .birthDate(LocalDate.of(2000,5,21))
                .email("xiaomanLIU@hotmail.com")
                .build();

        CandidateEntity candidate3 = CandidateEntity
                .builder()
                .birthDate(LocalDate.of(1998,9,2))
                .email("Iris2111@hotmail.com")
                .build();

        CandidateEntity candidate4 = CandidateEntity
                .builder()
                .birthDate(LocalDate.of(1997,11,21))
                .email("ye4321@gmail.com")
                .build();

        candidateRepository.save(candidate1);
        candidateRepository.save(candidate2);
        candidateRepository.save(candidate3);
        candidateRepository.save(candidate4);

        CandidateEvaluationGridEntity Grid1 = CandidateEvaluationGridEntity
                .builder()
                .grade(5.00)
                .candidateEntity(candidate1)
                .build();

        CandidateEvaluationGridEntity Grid2 = CandidateEvaluationGridEntity
                .builder()
                .grade(4.50)
                .candidateEntity(candidate2)
                .build();

        CandidateEvaluationGridEntity Grid3 = CandidateEvaluationGridEntity
                .builder()
                .grade(3.65)
                .candidateEntity(candidate3)
                .build();

        CandidateEvaluationGridEntity Grid4 = CandidateEvaluationGridEntity
                .builder()
                .grade(2.65)
                .candidateEntity(candidate4)
                .build();

        candidateEvaluationGridRepository.save(Grid1);
        candidateEvaluationGridRepository.save(Grid2);
        candidateEvaluationGridRepository.save(Grid3);
        candidateEvaluationGridRepository.save(Grid4);

        Set<CandidateEntity> results = candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(4.00);
        assertThat(results).hasSize(2);
        assertThat(results)
                .extracting(CandidateEntity::getBirthDate)
                .containsExactlyInAnyOrder( LocalDate.of(1998,9,2), LocalDate.of(1997,11,21));
    }

    @Test
    void testRequestfindAllByHasExtraTimeFalseAndBirthDateBefore(){
        CandidateEntity candidate1 = CandidateEntity
                .builder()
                .birthDate(LocalDate.of(1999,6,8))
                .email("allanWang@gmail.com")
                .hasExtraTime(true)
                .build();

        CandidateEntity candidate2 = CandidateEntity
                .builder()
                .birthDate(LocalDate.of(2000,5,21))
                .email("xiaomanLIU@hotmail.com")
                .hasExtraTime(true)
                .build();

        CandidateEntity candidate3 = CandidateEntity
                .builder()
                .birthDate(LocalDate.of(1998,9,2))
                .email("Iris2111@hotmail.com")
                .hasExtraTime(false)
                .build();

        CandidateEntity candidate4 = CandidateEntity
                .builder()
                .birthDate(LocalDate.of(1997,11,21))
                .email("ye4321@gmail.com")
                .hasExtraTime(false)
                .build();

        candidateRepository.save(candidate1);
        candidateRepository.save(candidate2);
        candidateRepository.save(candidate3);
        candidateRepository.save(candidate4);

        Set<CandidateEntity> results = candidateRepository.findAllByHasExtraTimeFalseAndBirthDateBefore(LocalDate.of(1998,7,6));

        assertThat(results).hasSize(1);
        assertThat(results)
                .extracting(CandidateEntity::getBirthDate)
                .containsExactlyInAnyOrder(  LocalDate.of(1997,11,21));
    }
}
