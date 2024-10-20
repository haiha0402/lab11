package com.example.feedbackmanagementsystem.api;

import com.example.feedbackmanagementsystem.Trainee;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface TraineeService {
    String TRAINEES = "hocvien";

    @GET(TRAINEES)
    Call<Trainee[]> getAllTrainees();

    @POST(TRAINEES)
    Call<Trainee> createTrainee(@Body Trainee trainee);

    @PUT(TRAINEES + "/{id}")
    Call<Trainee> updateTrainee(@Path("id") int id, @Body Trainee trainee);

    // DELETE request to delete a trainee by ID
    @DELETE(TRAINEES + "/{id}")
    Call<Void> deleteTrainee(@Path("id") int id);
}