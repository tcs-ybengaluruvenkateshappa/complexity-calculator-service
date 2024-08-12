package com.containerstore.prestonintegrations.proposal.service;

import com.containerstore.prestonintegrations.proposal.exception.NullComplexityException;
import com.containerstore.prestonintegrations.proposal.repository.ComplexityComponentsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ComplexityCalculatorService {

    @Value("${spring.application.maxcomplexity}")
    private int maxComplexity;

    private final ComplexityComponentsRepository complexityComponentsRepository;

    public com.containerstore.prestonintegrations.proposal.models.ComplexityCalculatorResponse getComplexity(com.containerstore.prestonintegrations.proposal.models.ComplexityCalculatorRequest request){
        List<com.containerstore.prestonintegrations.proposal.models.JobRoomsRoomInner> listOfJobRoomsInner = request.getJob().getRooms().getRoom();

        listOfJobRoomsInner.stream()
                .flatMap(jobRoomsRoomInner -> jobRoomsRoomInner.getClosets().getCloset().stream())
                .forEach(this::populateComplexityForCloset);

//        for(com.containerstore.prestonintegrations.proposal.models.JobRoomsRoomInner jobRoomsRoomInner : listOfJobRoomsInner){
//            List<com.containerstore.prestonintegrations.proposal.models.ClosetsClosetInner> listOfClosets = jobRoomsRoomInner.getClosets().getCloset();
//            for(com.containerstore.prestonintegrations.proposal.models.ClosetsClosetInner closetsClosetInner : listOfClosets){
//                populateComplexityForCloset(closetsClosetInner);
//            }
//        }
        com.containerstore.prestonintegrations.proposal.models.ComplexityCalculatorResponse response = new com.containerstore.prestonintegrations.proposal.models.ComplexityCalculatorResponse();
        response.setJob(request.getJob());
        return response;
    }

    public void populateComplexityForCloset(com.containerstore.prestonintegrations.proposal.models.ClosetsClosetInner closetsClosetInner){

        List<String> listOfComponentsValue = closetsClosetInner.getComponents().getComponent().stream()
                .map(com.containerstore.prestonintegrations.proposal.models.ComponentsComponentInner::getDescription)
                .collect(Collectors.toList());

//        for(com.containerstore.prestonintegrations.proposal.models.ComponentsComponentInner componentsComponentInner : closetsClosetInner.getComponents().getComponent()){
//            listOfComponentsValue.add(componentsComponentInner.getDescription());
//        }
        listOfComponentsValue.add(closetsClosetInner.getLayout());
        listOfComponentsValue.add(closetsClosetInner.getTypeOfSpace());

        int currentComplexityCount = maxComplexity;

        while (currentComplexityCount > 0) {
            log.info("Checking if complexity is matching at level : " + currentComplexityCount);
            boolean isCurrentComplexity = matchesComplexityLevel(listOfComponentsValue, currentComplexityCount);
            if (isCurrentComplexity) {
                closetsClosetInner.setComplexity(String.valueOf(currentComplexityCount));
                return;
            }
            currentComplexityCount--;
        }
        if(currentComplexityCount == 0){
            throw new NullComplexityException("Complexity could not be calculated");
        }
    }

    public boolean matchesComplexityLevel(List<String> listOfComponentsValue, Integer count){

        List<String> listOfComponentsValueFromDB = complexityComponentsRepository.getComponentValueByComplexity(count);
        return listOfComponentsValueFromDB.stream()
                .filter(str -> str != null)
                .anyMatch(listOfComponentsValue::contains);
    }
}