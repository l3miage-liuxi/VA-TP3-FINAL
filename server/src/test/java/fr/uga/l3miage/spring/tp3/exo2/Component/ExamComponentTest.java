package fr.uga.l3miage.spring.tp3.exo2.Component;


import fr.uga.l3miage.spring.tp3.components.ExamComponent;
import fr.uga.l3miage.spring.tp3.exceptions.technical.ExamNotFoundException;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import fr.uga.l3miage.spring.tp3.models.SkillEntity;
import fr.uga.l3miage.spring.tp3.repositories.ExamRepository;
import fr.uga.l3miage.spring.tp3.repositories.SkillRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ExamComponentTest {
    @Autowired
    private ExamComponent examComponent;

    @MockBean
    private SkillRepository skillRepository;

    @MockBean
    private ExamRepository examRepository;

    @Test
    void getAllCardioExamTest() {
        SkillEntity Skill1 = SkillEntity
                .builder()
                .name("cardio")
                .build();
        SkillEntity Skill2 = SkillEntity
                .builder()
                .name("skill2")
                .build();
        SkillEntity Skill3 = SkillEntity
                .builder()
                .name("skill3")
                .build();

        ExamEntity exam1 = ExamEntity.builder()
                .id(1L)
                .skillEntities(Set.of(Skill1))
                .skillEntities(Set.of(Skill3))
                .build();

        ExamEntity exam2 = ExamEntity.builder()
                .id(2L)
                .skillEntities(Set.of(Skill1))
                .skillEntities(Set.of(Skill2))
                .build();

        when(skillRepository.findByNameLike("cardio")).thenReturn(Optional.of(Skill1));
        when(skillRepository.findByNameLike("Skill2")).thenReturn(Optional.of(Skill2));
        when(skillRepository.findByNameLike("Skill3")).thenReturn(Optional.of(Skill3));


        when(examRepository.findAllBySkillEntitiesContaining(Skill1)).thenReturn(Set.of(exam1,exam2));
        when(examRepository.findAllBySkillEntitiesContaining(Skill2)).thenReturn(Set.of(exam2));

        Set<ExamEntity> result = examComponent.getAllCardioExam();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.contains(exam1));
        assertTrue(result.contains(exam2));
    }

    @Test
    void getAllByIdTest() {
        Set<Long> examIds = new HashSet<>(Arrays.asList(1L, 2L));

        ExamEntity exam1 = ExamEntity
                .builder()
                .id(1L)
                .build();
        ExamEntity exam2 = ExamEntity
                .builder()
                .id(2L)
                .build();

        when(examRepository.findAllById(examIds)).thenReturn(Arrays.asList(exam1, exam2));

        assertDoesNotThrow(()->examComponent.getAllById(examIds));


    }

    @Test
    void getAllByIdNotFoundTest() {
        Set<Long> examIds = new HashSet<>(Arrays.asList(1L, 2L));
        ExamEntity exam1 = ExamEntity
                .builder()
                .id(1L)
                .build();

        when(examRepository.findAllById(examIds)).thenReturn(Arrays.asList(exam1));

        // 执行测试并期待抛出异常
        assertThrows(ExamNotFoundException.class, () -> {
            examComponent.getAllById(examIds);
        });
    }
}
