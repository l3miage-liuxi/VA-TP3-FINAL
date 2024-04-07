package fr.uga.l3miage.spring.tp3.components;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Component;

import fr.uga.l3miage.spring.tp3.exceptions.technical.LastStepNotPassedException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.StepNotFoundException;
import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationStepEntity;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionProgrammationStepRepository;
import fr.uga.l3miage.spring.tp3.responses.enums.SessionStatus;

@Component
@RequiredArgsConstructor
public class EcosSessionProgrammationStepComponent {
    private final EcosSessionProgrammationStepRepository ecosSessionProgrammationStepRepository;

    public EcosSessionProgrammationStepEntity getLast(long id) throws StepNotFoundException{
        Optional<EcosSessionProgrammationStepEntity> last=ecosSessionProgrammationStepRepository.findFirstByEcosSessionProgrammationEntityIdOrderByDateTimeDesc(id);
        if(last.isPresent()){
            return last.get();
        }else {
            throw new StepNotFoundException("Last step not found with ID :"+ id);
        }
    }

    public void isPassed(EcosSessionProgrammationStepEntity ecosSessionProgrammationStepEntity) throws LastStepNotPassedException{
        if (!(ecosSessionProgrammationStepEntity.getDateTime().isBefore(LocalDateTime.now()))){
            throw new LastStepNotPassedException("Last Step not yet passed !");
        }
    }
}
