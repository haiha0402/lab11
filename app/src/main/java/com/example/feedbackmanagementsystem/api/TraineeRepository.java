package com.example.feedbackmanagementsystem.api;

public class TraineeRepository {
    public static TraineeService getTraineeService(){
        return APIClient.getRetrofitInstance().create(TraineeService.class);
    }
}
