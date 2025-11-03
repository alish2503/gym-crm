package com.gymcrm.application.dto.request;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alish
 */
public class UpdateTrainersRequest {
    private String traineeUserName;
    private List<String> trainersUserNames = new ArrayList<>();

    public void setTraineeUserName(String traineeUserName) {
        this.traineeUserName = traineeUserName;
    }

    public void setTrainersUserNames(List<String> trainersUserNames) {
        this.trainersUserNames = trainersUserNames;
    }
}
