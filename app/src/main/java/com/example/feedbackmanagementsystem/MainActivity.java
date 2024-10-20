package com.example.feedbackmanagementsystem;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.feedbackmanagementsystem.api.TraineeRepository;
import com.example.feedbackmanagementsystem.api.TraineeService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private CustomAdapter adapter;
    private ListView listView;
    private Button btnAdd;
    private TraineeService traineeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        btnAdd = findViewById(R.id.btn_add);
        traineeService = TraineeRepository.getTraineeService();

        loadTrainees();

        btnAdd.setOnClickListener(v -> {
            submitTrainee();
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            editOrDeleteItem(position);
        });
    }

    private void loadTrainees() {
        Call<Trainee[]> call = traineeService.getAllTrainees();
        call.enqueue(new Callback<Trainee[]>() {
            @Override
            public void onResponse(Call<Trainee[]> call, Response<Trainee[]> response) {
                if (response.body() != null){
                    Toast.makeText(MainActivity.this, "Load Successfully",
                            Toast.LENGTH_LONG).show();
                }
                ArrayList<Trainee> list = new ArrayList<>();
                list = new ArrayList<>(List.of(response.body()));
                adapter = new CustomAdapter(getApplicationContext(), list);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<Trainee[]> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Load Fail",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void submitTrainee() {
        // Add your logic to submit a trainee
    }

    private void editOrDeleteItem(int position) {
        // Add your logic to edit or delete a trainee
    }
}
