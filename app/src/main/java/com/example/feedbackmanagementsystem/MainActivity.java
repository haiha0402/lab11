package com.example.feedbackmanagementsystem;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.feedbackmanagementsystem.api.TraineeRepository;
import com.example.feedbackmanagementsystem.api.TraineeService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private CustomAdapter adapter;
    private ListView listView;
    private Button btnAdd;
    private Button btnDelete;
    private TraineeService traineeService;
    private List<Trainee> traineeList = new ArrayList<>();
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
                if (response.body() != null) {
                    Toast.makeText(MainActivity.this, "Load Successfully", Toast.LENGTH_LONG).show();
                    traineeList.clear();  // Clear the list before adding new items
                    traineeList.addAll(List.of(response.body()));  // Add all trainees to the list
                    adapter = new CustomAdapter(getApplicationContext(), (ArrayList<Trainee>) traineeList);
                    listView.setAdapter(adapter);  // Set the adapter for the ListView
                }
            }

            @Override
            public void onFailure(Call<Trainee[]> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Load Fail", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void submitTrainee() {
        // Inflate the custom layout view
        View view = getLayoutInflater().inflate(R.layout.trainee_item, null);

        // Initialize the AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thêm Item mới");

        // Set the custom view for the dialog
        builder.setView(view);

        // Reference EditText fields in the custom layout
        EditText name = view.findViewById(R.id.trainee_name);
        EditText email = view.findViewById(R.id.trainee_email);
        EditText phone = view.findViewById(R.id.trainee_phone);
        EditText gender = view.findViewById(R.id.trainee_gender);

        // Add 'Submit' and 'Cancel' buttons for the dialog
        builder.setPositiveButton("Submit", (dialog, which) -> {
            // Create new Trainee object and set its properties
            Trainee trainee = new Trainee();
            trainee.setName(name.getText().toString());
            trainee.setEmail(email.getText().toString());
            trainee.setPhone(phone.getText().toString());
            trainee.setGender(gender.getText().toString());

            // Make the API call to create the trainee
            Call<Trainee> call = traineeService.createTrainee(trainee);
            call.enqueue(new Callback<Trainee>() {
                @Override
                public void onResponse(Call<Trainee> call, Response<Trainee> response) {
                    if (response.body() != null) {
                        Toast.makeText(MainActivity.this, "Trainee added successfully!", Toast.LENGTH_LONG).show();
                        traineeList.add(response.body());  // Append the new trainee to the list
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<Trainee> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Failed to add trainee", Toast.LENGTH_LONG).show();
                }
            });
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Show the dialog
        builder.show();
    }
    public void editItem(int position) {
        Trainee traineeToUpdate = traineeList.get(position);

        // Inflate the custom layout view for the edit dialog
        View view = getLayoutInflater().inflate(R.layout.trainee_item, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sửa thông tin học viên");

        // Set the custom view for the dialog
        builder.setView(view);
        EditText name = view.findViewById(R.id.trainee_name);
        EditText email = view.findViewById(R.id.trainee_email);
        EditText phone = view.findViewById(R.id.trainee_phone);
        EditText gender = view.findViewById(R.id.trainee_gender);

        // Pre-fill the EditText fields with the existing trainee data
        name.setText(traineeToUpdate.getName());
        email.setText(traineeToUpdate.getEmail());
        phone.setText(traineeToUpdate.getPhone());
        gender.setText(traineeToUpdate.getGender());

        // Add 'Submit' and 'Cancel' buttons for the dialog
        builder.setPositiveButton("Submit", (dialog, which) -> {
            // Create a new Trainee object with updated details
            Trainee updatedTrainee = new Trainee();
            updatedTrainee.setId(traineeToUpdate.getId());
            updatedTrainee.setName(name.getText().toString());
            updatedTrainee.setEmail(email.getText().toString());
            updatedTrainee.setPhone(phone.getText().toString());
            updatedTrainee.setGender(gender.getText().toString());

            // Update the trainee via API call
            Call<Trainee> call = traineeService.updateTrainee(updatedTrainee.getId(), updatedTrainee);
            call.enqueue(new Callback<Trainee>() {
                @Override
                public void onResponse(Call<Trainee> call, Response<Trainee> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        traineeList.set(position, response.body());
                        adapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "Trainee updated successfully!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to update trainee", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Trainee> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Failed to update trainee", Toast.LENGTH_LONG).show();
                }
            });
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }



    public void deleteItem(int position) {
        Trainee traineeToDelete = traineeList.get(position);

        // Show a confirmation dialog before deleting
        new AlertDialog.Builder(this)
                .setTitle("Delete Trainee")
                .setMessage("Are you sure you want to delete this trainee?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Make the API call to delete the trainee
                    Call<Void> call = traineeService.deleteTrainee(traineeToDelete.getId());
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                traineeList.remove(position);  // Remove the trainee from the list
                                adapter.notifyDataSetChanged();  // Notify the adapter of data change
                                Toast.makeText(MainActivity.this, "Trainee deleted successfully!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Failed to delete trainee", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "Failed to delete trainee", Toast.LENGTH_LONG).show();
                        }
                    });
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }


    private void editOrDeleteItem(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Action");
        builder.setItems(new String[]{"Edit", "Delete"}, (dialog, which) -> {
            if (which == 0) {
                editItem(position);
            } else if (which == 1) {
                deleteItem(position);
            }
        });
        builder.show();
    }
}
