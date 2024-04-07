package fr.uga.l3miage.spring.tp3.exo2.Component;

import fr.uga.l3miage.spring.tp3.components.CandidateComponent;
import fr.uga.l3miage.spring.tp3.exceptions.technical.CandidateNotFoundException;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.repositories.CandidateEvaluationGridRepository;
import fr.uga.l3miage.spring.tp3.repositories.CandidateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class CandidateComponentTest {
    @Autowired
    private CandidateComponent candidateComponent;
    @MockBean
    private CandidateRepository candidateRepository;
    @MockBean
    private CandidateEvaluationGridRepository candidateEvaluationGridRepository;
    @Test
    void getAllEliminatedCandidateTest(){
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


        CandidateEvaluationGridEntity Grid1 = CandidateEvaluationGridEntity
                .builder()
                .grade(5.00)
                .candidateEntity(candidate1)
                .build();

        CandidateEvaluationGridEntity Grid2 = CandidateEvaluationGridEntity
                .builder()
                .grade(5.50)
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

        when(candidateEvaluationGridRepository.findAllByGradeIsLessThanEqual(5.00))
                .thenReturn(new HashSet<>(Arrays.asList(Grid1, Grid3, Grid4)));

        Set<CandidateEntity> result = candidateComponent.getAllEliminatedCandidate();

        assertNotNull(result);
        assertEquals(3, result.size()); // il y a des 3 candidates qui a moins 5.
        assertTrue(result.contains(candidate1));
        assertFalse(result.contains(candidate2));
        assertTrue(result.contains(candidate3));
        assertTrue(result.contains(candidate4));
    }

    @Test
    void getCandidatByIdFoundTest() {
        Long id = 1L;
        CandidateEntity candidate = CandidateEntity
                .builder()
                .id(id)
                .build();

        when(candidateRepository.findById(id)).thenReturn(Optional.of(candidate));

        assertDoesNotThrow(()->candidateComponent.getCandidatById(id));

    }

    @Test
    void getCandidatByIdNotFoundTest() {
        Long id = 1L;
        when(candidateRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CandidateNotFoundException.class,()->candidateComponent.getCandidatById(id));

    }



}
