package com.containerstore.prestonintegrations.proposal.service;

import com.containerstore.prestonintegrations.proposal.entity.ComplexityComponents;
import com.containerstore.prestonintegrations.proposal.repository.ComplexityComponentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ComplexityCalculatorService {

    private int maxComplexity = 5;

    @Autowired
    private ComplexityComponentsRepository complexityComponentsRepository;

    public com.containerstore.prestonintegrations.proposal.models.ComplexityCalculatorResponse getComplexity(com.containerstore.prestonintegrations.proposal.models.ComplexityCalculatorRequest request){
        List<com.containerstore.prestonintegrations.proposal.models.JobRoomsRoomInner> listOfJobRoomsInner = request.getJob().getRooms().getRoom();
        for(com.containerstore.prestonintegrations.proposal.models.JobRoomsRoomInner jobRoomsRoomInner : listOfJobRoomsInner){
            List<com.containerstore.prestonintegrations.proposal.models.ClosetsClosetInner> listOfClosets = jobRoomsRoomInner.getClosets().getCloset();
            for(com.containerstore.prestonintegrations.proposal.models.ClosetsClosetInner closetsClosetInner : listOfClosets){
                populateComplexityForCloset(closetsClosetInner);
            }
        }
        com.containerstore.prestonintegrations.proposal.models.ComplexityCalculatorResponse response = new com.containerstore.prestonintegrations.proposal.models.ComplexityCalculatorResponse();
        response.setJob(request.getJob());
        return response;
    }

    public void populateComplexityForCloset(com.containerstore.prestonintegrations.proposal.models.ClosetsClosetInner closetsClosetInner){

        List<String> listOfComponentsValue = new ArrayList<>();

        for(com.containerstore.prestonintegrations.proposal.models.ComponentsComponentInner componentsComponentInner : closetsClosetInner.getComponents().getComponent()){
            listOfComponentsValue.add(componentsComponentInner.getDescription());
        }
        listOfComponentsValue.add(closetsClosetInner.getLayout());
        listOfComponentsValue.add(closetsClosetInner.getTypeOfSpace());

        int currentComplexityCount = maxComplexity;

        try {
            while(currentComplexityCount > 0){
                boolean isCurrentComplexity = matchesComplexityLevel(listOfComponentsValue,currentComplexityCount);
                if(isCurrentComplexity){
                    //setComplexity is accepting a string but should have been int instead;
                    closetsClosetInner.setComplexity(String.valueOf(currentComplexityCount));
                    return;
                }
                currentComplexityCount--;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean matchesComplexityLevel(List<String> listOfComponentsValue, int count){

        List<String> listOfComponentsValueFromDB = complexityComponentsRepository.getComponentValueByComplexity(count);
        for(String str : listOfComponentsValueFromDB){
            if((str != null ) && (listOfComponentsValue.contains(str))){
                return true;
            }
        }
        return false;
    }

    public List<ComplexityComponents> getAllComponents(){
        return complexityComponentsRepository.findAll();
    }
}
