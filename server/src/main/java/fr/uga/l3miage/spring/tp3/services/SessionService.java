package fr.uga.l3miage.spring.tp3.services;

import fr.uga.l3miage.spring.tp3.components.EcosSessionProgrammationStepComponent;
import fr.uga.l3miage.spring.tp3.components.ExamComponent;
import fr.uga.l3miage.spring.tp3.components.SessionComponent;
import fr.uga.l3miage.spring.tp3.enums.SessionStatus;
import fr.uga.l3miage.spring.tp3.exceptions.rest.CreationSessionRestException;
import fr.uga.l3miage.spring.tp3.exceptions.rest.LastStepNotPassedRestException;
import fr.uga.l3miage.spring.tp3.exceptions.rest.SessionNotFoundRestException;
import fr.uga.l3miage.spring.tp3.exceptions.rest.SessionNotStartedRestException;
import fr.uga.l3miage.spring.tp3.exceptions.rest.StepNotFoundRestException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.ExamNotFoundException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.LastStepNotPassedException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.SessionNotFoundException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.SessionNotStartedException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.StepNotFoundException;
import fr.uga.l3miage.spring.tp3.mappers.SessionMapper;
import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationStepEntity;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import fr.uga.l3miage.spring.tp3.request.SessionCreationRequest;
import fr.uga.l3miage.spring.tp3.responses.CandidateEvaluationGridResponse;
import fr.uga.l3miage.spring.tp3.responses.SessionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import fr.uga.l3miage.spring.tp3.enums.SessionStatus;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionMapper sessionMapper;
    private final ExamComponent examComponent;
    private final SessionComponent sessionComponent;
    private final EcosSessionProgrammationStepComponent ecosSessionProgrammationStepComponent;

    public SessionResponse createSession(SessionCreationRequest sessionCreationRequest){
        try {
            EcosSessionEntity ecosSessionEntity = sessionMapper.toEntity(sessionCreationRequest);
            EcosSessionProgrammationEntity programmation = sessionMapper.toEntity(sessionCreationRequest.getEcosSessionProgrammation());
            Set<EcosSessionProgrammationStepEntity> stepEntities = sessionCreationRequest.getEcosSessionProgrammation()
                    .getSteps()
                    .stream()
                    .map(sessionMapper::toEntity)
                    .collect(Collectors.toSet());

            Set<ExamEntity> exams = examComponent.getAllById(sessionCreationRequest.getExamsId());

            ecosSessionEntity.setExamEntities(exams);
            programmation.setEcosSessionProgrammationStepEntities(stepEntities);
            ecosSessionEntity.setEcosSessionProgrammationEntity(programmation);

            ecosSessionEntity.setStatus(SessionStatus.CREATED);

            return sessionMapper.toResponse(sessionComponent.createSession(ecosSessionEntity));
        }catch (RuntimeException | ExamNotFoundException e){
            throw new CreationSessionRestException(e.getMessage());
        }
    }

    public Set<CandidateEvaluationGridResponse> endEvaluationSession(long sessionId){
        try {
            EcosSessionEntity ecosSessionEntity =sessionComponent.getSessionById(sessionId);
            EcosSessionProgrammationStepEntity lastStep =ecosSessionProgrammationStepComponent.getLast(sessionId);
            ecosSessionProgrammationStepComponent.isPassed(lastStep);
            sessionComponent.isStarted(ecosSessionEntity);
            ecosSessionEntity.setStatus(SessionStatus.EVAL_ENDED);
            Set<CandidateEvaluationGridResponse> candidateEvaluationGridResponses = new HashSet<>();
            ecosSessionEntity.getExamEntities().forEach(exam->exam.getCandidateEvaluationGridEntities().forEach(grid->candidateEvaluationGridResponses.add(sessionMapper.toResponse(grid))));
            return candidateEvaluationGridResponses; 
        }catch (SessionNotFoundException e) {
            throw new SessionNotFoundRestException(e.getMessage(), e.getSessionId());
        }catch(StepNotFoundException e){
            throw new StepNotFoundRestException(e.getMessage());
        }catch(LastStepNotPassedException e){
            throw new LastStepNotPassedRestException(e.getMessage());
        }catch(SessionNotStartedException e){
            throw new SessionNotStartedRestException(e.getMessage());
        }
        
    }

}
