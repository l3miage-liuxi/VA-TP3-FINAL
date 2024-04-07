package fr.uga.l3miage.spring.tp3.endpoints;

import fr.uga.l3miage.spring.tp3.exceptions.SessionNotStartedReponse;
import fr.uga.l3miage.spring.tp3.request.SessionCreationRequest;
import fr.uga.l3miage.spring.tp3.responses.CandidateEvaluationGridResponse;
import fr.uga.l3miage.spring.tp3.responses.SessionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Gestion des session")
@RestController
@RequestMapping("/api/sessions")
public interface SessionEndpoints {

    @Operation(description = "Créer une session")
    @ApiResponse(responseCode = "201",description = "La session à bien été créer")
    @ApiResponse(responseCode = "400" ,description = "La session n'a pas être créer", content = @Content(schema = @Schema(implementation = String.class),mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    SessionResponse createSession(@RequestBody SessionCreationRequest request);

    @Operation(description = "Modifier l'état d'une session [Terminer une session]")
    @ApiResponse(responseCode = "200",description = "L'état de la session à bien été modifié")
    @ApiResponse(responseCode = "409" ,description = "Conflit ! L'état de la session n'a pas pu être modifié", content = @Content(schema = @Schema(implementation = SessionNotStartedReponse.class),mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{sessionId}/end")
    Set<CandidateEvaluationGridResponse> endEvaluationSession(@PathVariable Long sessionId);
}
