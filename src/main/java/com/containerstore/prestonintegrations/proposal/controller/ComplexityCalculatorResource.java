package com.containerstore.prestonintegrations.proposal.controller;

import com.containerstore.prestonintegrations.proposal.exception.DuplicateSpaceIdException;
import com.containerstore.prestonintegrations.proposal.exception.NullOpportunityIdException;
import com.containerstore.prestonintegrations.proposal.exception.NullSpaceIdException;
import com.containerstore.prestonintegrations.proposal.models.ComplexityCalculatorRequest;
import com.containerstore.prestonintegrations.proposal.models.ComplexityCalculatorResponse;
import com.containerstore.prestonintegrations.proposal.service.ComplexityCalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/apps/closetpro/api")
@RequiredArgsConstructor
public class ComplexityCalculatorResource implements com.containerstore.prestonintegrations.proposal.controllers.V1Api {

    private final ComplexityCalculatorService complexityCalculatorService;

//    endpoint : /apps/closetpro/api/v1/complexity/calculator/get-complexity
    @Override
    public ResponseEntity<ComplexityCalculatorResponse> calculateComplexity(ComplexityCalculatorRequest complexityCalculatorRequest) {
        this.validateRequest(complexityCalculatorRequest);
        ComplexityCalculatorResponse response = complexityCalculatorService.getComplexity(complexityCalculatorRequest);
        return ResponseEntity.ok().body(new ComplexityCalculatorResponse(response.getJob()));
    }

    private void validateRequest(ComplexityCalculatorRequest complexityCalculatorRequest) {
        checkForDuplicateAndNullSpaceId(complexityCalculatorRequest);
        checkForNullOpportunityId(complexityCalculatorRequest);
    }

    private void checkForDuplicateAndNullSpaceId(ComplexityCalculatorRequest complexityCalculatorRequest) {
        List<String> spaceIds = complexityCalculatorRequest.getJob().getRooms().getRoom().stream()
                .flatMap(room -> room.getClosets().getCloset().stream())
                .map(com.containerstore.prestonintegrations.proposal.models.ClosetsClosetInner::getSpaceID)
                .collect(Collectors.toList());

        if (spaceIds.size() != spaceIds.stream().distinct().count()) {
            throw new DuplicateSpaceIdException("Duplicate spaceId has been found");
        }

        if (spaceIds.stream().anyMatch(Objects::isNull)) {
            throw new NullSpaceIdException("Null spaceId has been found");
        }
    }

    private void checkForNullOpportunityId(ComplexityCalculatorRequest complexityCalculatorRequest){
        if(complexityCalculatorRequest.getOpportunityId() == null){
            throw new NullOpportunityIdException("OpportunityId cannot be empty");
        }
    }

}
