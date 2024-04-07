package fr.uga.l3miage.spring.tp3.components;

import fr.uga.l3miage.spring.tp3.exceptions.rest.SessionNotFoundRestException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.SessionNotFoundException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.SessionNotStartedException;
import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionProgrammationRepository;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionProgrammationStepRepository;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import fr.uga.l3miage.spring.tp3.enums.SessionStatus;

@Component
@RequiredArgsConstructor
public class SessionComponent {
    private final EcosSessionRepository ecosSessionRepository;
    private final EcosSessionProgrammationRepository ecosSessionProgrammationRepository;
    private final EcosSessionProgrammationStepRepository ecosSessionProgrammationStepRepository;


    public EcosSessionEntity createSession(EcosSessionEntity entity){
        ecosSessionProgrammationStepRepository.saveAll(entity.getEcosSessionProgrammationEntity().getEcosSessionProgrammationStepEntities());
        ecosSessionProgrammationRepository.save(entity.getEcosSessionProgrammationEntity());
        return ecosSessionRepository.save(entity);
    }

    public EcosSessionEntity getSessionById(Long id) throws SessionNotFoundException {
        return ecosSessionRepository.findById(id).orElseThrow(()-> new SessionNotFoundException(String.format("Le session [%s] n'a pas été trouvé",id),id));
    }

    public void isStarted(EcosSessionEntity ecosSessionEntity) throws SessionNotStartedException{
        if(!(ecosSessionEntity.getStatus()==SessionStatus.EVAL_STARTED)){
            throw new SessionNotStartedException("This session isn't the status started");
        }
    }
}
