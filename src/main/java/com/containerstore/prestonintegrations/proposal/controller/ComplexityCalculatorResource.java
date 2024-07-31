package com.containerstore.prestonintegrations.proposal.controller;


import com.containerstore.prestonintegrations.proposal.models.ComplexityCalculatorRequest;
import com.containerstore.prestonintegrations.proposal.models.ComplexityCalculatorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/apps/closetpro/api")
public class ComplexityCalculatorResource implements com.containerstore.prestonintegrations.proposal.controllers.V1Api {

//    endpoint : /apps/closetpro/api/v1/complexity/calculator/get-complexity
    @Override
    public ResponseEntity<ComplexityCalculatorResponse> calculateComplexity(ComplexityCalculatorRequest complexityCalculatorRequest) {
        return null;
    }
}
