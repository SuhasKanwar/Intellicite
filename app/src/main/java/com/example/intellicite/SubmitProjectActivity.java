package com.example.intellicite;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intellicite.api.RetrofitClient;
import com.example.intellicite.models.ProjectRequest;
import com.example.intellicite.models.ProjectResponse;
import com.example.intellicite.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubmitProjectActivity extends AppCompatActivity {

    private EditText editFacultyName, editProjectTitle, editFundingAgency;
    private EditText editAmount, editStatus, editDate, editSupportingDoc, editDuration;
    private RadioGroup radioGroupRole;
    private RadioButton radioPI, radioCoPI, radioPIAndCoPI;
    private Button btnSubmitProject;
    private ImageButton hamburgerIcon, profileIcon;

    private Calendar myCalendar = Calendar.getInstance();
    private static final String TAG = "SubmitProjectActivity";
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.projects_submit);

        // Initialize SessionManager
        sessionManager = new SessionManager(this);

        // Initialize views
        initViews();
        setupDatePicker();
        setupClickListeners();
    }

    private void initViews() {
        // Top section
        hamburgerIcon = findViewById(R.id.hamburgerIcon);
        profileIcon = findViewById(R.id.profileIcon);
        editFacultyName = findViewById(R.id.editFacultyName);
        editProjectTitle = findViewById(R.id.editProjectTitle);

        // Bottom section
        editFundingAgency = findViewById(R.id.editFundingAgency);
        radioGroupRole = findViewById(R.id.radioGroupRole);
        radioPI = findViewById(R.id.radioPI);
        radioCoPI = findViewById(R.id.radioCoPI);
        radioPIAndCoPI = findViewById(R.id.radioPIAndCoPI);
        editAmount = findViewById(R.id.editAmount);
        editStatus = findViewById(R.id.editStatus);
        editDate = findViewById(R.id.editDate);
        editSupportingDoc = findViewById(R.id.editSupportingDoc);
        editDuration = findViewById(R.id.editDuration);
        btnSubmitProject = findViewById(R.id.btnSubmitProject);
    }

    private void setupDatePicker() {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel();
            }
        };

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(SubmitProjectActivity.this, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateDateLabel() {
        String format = "yyyy-MM-dd"; // ISO format for API
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        editDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void setupClickListeners() {
        btnSubmitProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    submitProject();
                }
            }
        });

        // Add click listeners for hamburger and profile icons if needed
        hamburgerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open drawer or show menu
                onBackPressed();
            }
        });

        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to profile screen
                Toast.makeText(SubmitProjectActivity.this, "Profile clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInputs() {
        // Check for empty fields
        if (TextUtils.isEmpty(editFacultyName.getText())) {
            editFacultyName.setError("Faculty name is required");
            return false;
        }

        if (TextUtils.isEmpty(editProjectTitle.getText())) {
            editProjectTitle.setError("Project title is required");
            return false;
        }

        if (TextUtils.isEmpty(editFundingAgency.getText())) {
            editFundingAgency.setError("Funding agency is required");
            return false;
        }

        if (radioGroupRole.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select PI or Co-PI role", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(editAmount.getText())) {
            editAmount.setError("Amount is required");
            return false;
        }

        if (TextUtils.isEmpty(editStatus.getText())) {
            editStatus.setError("Status is required");
            return false;
        }

        if (TextUtils.isEmpty(editDate.getText())) {
            editDate.setError("Date is required");
            return false;
        }

        if (TextUtils.isEmpty(editDuration.getText())) {
            editDuration.setError("Duration is required");
            return false;
        }

        return true;
    }

    private void submitProject() {
        // Get project type based on PI/Co-PI selection
        String projectType = "";
        int selectedRadioId = radioGroupRole.getCheckedRadioButtonId();
        if (selectedRadioId == R.id.radioPI) {
            projectType = "PI";
        } else if (selectedRadioId == R.id.radioCoPI) {
            projectType = "Co-PI";
        } else if (selectedRadioId == R.id.radioPIAndCoPI) {
            projectType = "PI and Co-PI";
        }

        // Parse amount
        double amount = 0;
        try {
            amount = Double.parseDouble(editAmount.getText().toString().trim());
        } catch (NumberFormatException e) {
            editAmount.setError("Please enter a valid amount");
            return;
        }

        // Handle supporting documents (simplified for this example)
        String supportingDocUrl = editSupportingDoc.getText().toString().trim();
        String[] supportingDocs = supportingDocUrl.isEmpty() ? new String[0] : new String[]{supportingDocUrl};

        // Create project request
        ProjectRequest projectRequest = new ProjectRequest(
                editFacultyName.getText().toString().trim(),
                editProjectTitle.getText().toString().trim(),
                editFundingAgency.getText().toString().trim(),
                projectType,
                amount,
                editStatus.getText().toString().trim(),
                editDate.getText().toString().trim(),
                supportingDocs,
                editDuration.getText().toString().trim()
        );

        // Show loading indicator
        btnSubmitProject.setEnabled(false);
        btnSubmitProject.setText("Submitting...");

        // Get research work ID from user session or intent
        String researchWorkId = getResearchWorkId();
        Log.d(TAG, "Submitting project for research work ID: " + researchWorkId);

        // Make API call with the research work ID
        RetrofitClient.getInstance(this)
                .getApiService()
                .submitProject(researchWorkId, projectRequest)
                .enqueue(new Callback<ProjectResponse>() {
                    @Override
                    public void onResponse(Call<ProjectResponse> call, Response<ProjectResponse> response) {
                        btnSubmitProject.setEnabled(true);
                        btnSubmitProject.setText("Submit");

                        if (response.isSuccessful() && response.body() != null) {
                            ProjectResponse projectResponse = response.body();
                            if (projectResponse.isSuccess()) {
                                Toast.makeText(SubmitProjectActivity.this,
                                        projectResponse.getMessage(), Toast.LENGTH_LONG).show();
                                // Reset form or navigate back
                                finish();
                            } else {
                                Toast.makeText(SubmitProjectActivity.this,
                                        "Error: " + projectResponse.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            try {
                                String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                                Log.e(TAG, "API Error: " + errorBody);
                                Toast.makeText(SubmitProjectActivity.this,
                                        "Failed to submit project. Status code: " + response.code(),
                                        Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Log.e(TAG, "Error parsing error response", e);
                                Toast.makeText(SubmitProjectActivity.this,
                                        "An error occurred. Please try again.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ProjectResponse> call, Throwable t) {
                        btnSubmitProject.setEnabled(true);
                        btnSubmitProject.setText("Submit");
                        Log.e(TAG, "Network failure", t);
                        Toast.makeText(SubmitProjectActivity.this,
                                "Network error. Please check your connection and try again.",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * Get the research work ID
     * This method should be updated based on how your app identifies research works
     */
    private String getResearchWorkId() {
        // Option 1: Get from intent extras if passed from previous screen
        String researchWorkId = getIntent().getStringExtra("RESEARCH_WORK_ID");

        // Option 2: Get from user ID (if each user has one research work)
        if (researchWorkId == null || researchWorkId.isEmpty()) {
            researchWorkId = sessionManager.getUserId();
        }

        // Option 3: Use a default or placeholder value if none is available
        if (researchWorkId == null || researchWorkId.isEmpty()) {
            researchWorkId = "default_research_work";
            Log.w(TAG, "Using default research work ID. This should be updated in production.");
        }

        return researchWorkId;
    }
}