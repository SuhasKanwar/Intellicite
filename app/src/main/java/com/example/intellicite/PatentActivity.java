package com.example.intellicite;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intellicite.api.ApiService;
import com.example.intellicite.api.RetrofitClient;
import com.example.intellicite.models.GenericResponse;
import com.example.intellicite.models.PatentRequest;
import com.example.intellicite.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatentActivity extends AppCompatActivity {

    private static final String TAG = "PatentActivity";

    // UI components
    private EditText editPatentTitle, editFiledBy, editInventors, editAgency,
            editDate, editApplicationNumber, editPatentUrl, editSupportingDoc;
    private RadioGroup radioGroupPatentType, radioGroupStatus;
    private Button btnSubmitPatent;
    private ImageButton hamburgerIcon, profileIcon;

    // Date picker
    private Calendar calendar;
    private SimpleDateFormat dateFormatter;

    // API and session management
    private ApiService apiService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patents_submit);

        // Initialize API and session
        apiService = RetrofitClient.getInstance(this).getApiService();
        sessionManager = new SessionManager(this);

        // Initialize date format
        calendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        // Initialize UI components
        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        // Edit texts
        editPatentTitle = findViewById(R.id.editPatentTitle);
        editFiledBy = findViewById(R.id.editFiledBy);
        editInventors = findViewById(R.id.editInventors);
        editAgency = findViewById(R.id.editAgency);
        editDate = findViewById(R.id.editDate);
        editApplicationNumber = findViewById(R.id.editApplicationNumber);
        editPatentUrl = findViewById(R.id.editPatentUrl);
        editSupportingDoc = findViewById(R.id.editSupportingDoc);

        // Radio groups
        radioGroupPatentType = findViewById(R.id.radioGroupPatentType);
        radioGroupStatus = findViewById(R.id.radioGroupStatus);

        // Buttons
        btnSubmitPatent = findViewById(R.id.btnSubmitPatent);
        hamburgerIcon = findViewById(R.id.hamburgerIcon);
        profileIcon = findViewById(R.id.profileIcon);
    }

    private void setupListeners() {
        // Date picker dialog
        editDate.setOnClickListener(v -> showDatePickerDialog());

        // Submit button
        btnSubmitPatent.setOnClickListener(v -> validateAndSubmitPatent());

        // Navigation
        hamburgerIcon.setOnClickListener(v -> onBackPressed()); // Simple back for now, could be menu
        profileIcon.setOnClickListener(v -> {
            // Navigate to profile or settings
            // Intent intent = new Intent(PatentActivity.this, ProfileActivity.class);
            // startActivity(intent);
        });
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    editDate.setText(dateFormatter.format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void validateAndSubmitPatent() {
        // Get values from UI components
        String patentTitle = editPatentTitle.getText().toString().trim();
        String filedBy = editFiledBy.getText().toString().trim();
        String inventorsText = editInventors.getText().toString().trim();
        String patentAgency = editAgency.getText().toString().trim();
        String dateOfPatent = editDate.getText().toString().trim();
        String patentNumber = editApplicationNumber.getText().toString().trim();
        String patentUrl = editPatentUrl.getText().toString().trim();
        String supportingDocUrl = editSupportingDoc.getText().toString().trim();

        // Get selected radio buttons
        int selectedTypeId = radioGroupPatentType.getCheckedRadioButtonId();
        int selectedStatusId = radioGroupStatus.getCheckedRadioButtonId();

        // Validation
        if (TextUtils.isEmpty(patentTitle)) {
            editPatentTitle.setError("Patent title is required");
            return;
        }

        if (TextUtils.isEmpty(filedBy)) {
            editFiledBy.setError("Filed by information is required");
            return;
        }

        if (TextUtils.isEmpty(inventorsText)) {
            editInventors.setError("List of inventors is required");
            return;
        }

        if (selectedTypeId == -1) {
            Toast.makeText(this, "Please select a patent type", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedStatusId == -1) {
            Toast.makeText(this, "Please select a patent status", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(dateOfPatent)) {
            editDate.setError("Date is required");
            return;
        }

        if (TextUtils.isEmpty(patentNumber)) {
            editApplicationNumber.setError("Patent number is required");
            return;
        }

        // Get patent type
        RadioButton selectedTypeButton = findViewById(selectedTypeId);
        String patentType = selectedTypeButton.getText().toString();

        // Get patent status
        RadioButton selectedStatusButton = findViewById(selectedStatusId);
        String patentStatus = selectedStatusButton.getText().toString();

        // Split inventors by comma
        List<String> inventorsList = new ArrayList<>(Arrays.asList(inventorsText.split("\\s*,\\s*")));

        // Create supporting documents list (could be multiple in a real app)
        List<String> supportingDocs = new ArrayList<>();
        if (!TextUtils.isEmpty(supportingDocUrl)) {
            supportingDocs.add(supportingDocUrl);
        }

        // Create PatentRequest object
        PatentRequest patentRequest = new PatentRequest(
                patentTitle,
                filedBy,
                patentType,
                inventorsList,
                patentAgency,
                patentStatus,
                dateOfPatent,
                patentNumber,
                patentUrl,
                supportingDocs
        );

        // Submit to API
        submitPatent(patentRequest);
    }

    private void submitPatent(PatentRequest patentRequest) {
        // Show loading indicator
        btnSubmitPatent.setEnabled(false);
        btnSubmitPatent.setText("Submitting...");

        // Get research work ID from session or use a default
        String researchWorkId = sessionManager.getUserId();
        if (researchWorkId == null || researchWorkId.isEmpty()) {
            researchWorkId = "default_research_id";
        }

        Log.d(TAG, "Submitting patent with research ID: " + researchWorkId);

        // Make API call
        apiService.createPatent(researchWorkId, patentRequest)
                .enqueue(new Callback<GenericResponse>() {
                    @Override
                    public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                        btnSubmitPatent.setEnabled(true);
                        btnSubmitPatent.setText("Submit");

                        if (response.isSuccessful() && response.body() != null) {
                            GenericResponse result = response.body();
                            if (result.isSuccess()) {
                                Toast.makeText(PatentActivity.this,
                                        "Patent submitted successfully: " + result.getMessage(),
                                        Toast.LENGTH_LONG).show();
                                clearForm();
                            } else {
                                Toast.makeText(PatentActivity.this,
                                        "Error: " + result.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        } else {
                            String errorMsg = "Failed to submit patent";
                            try {
                                if (response.errorBody() != null) {
                                    errorMsg += ": " + response.errorBody().string();
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Error parsing error response", e);
                            }

                            Toast.makeText(PatentActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                            Log.e(TAG, "Error response code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<GenericResponse> call, Throwable t) {
                        btnSubmitPatent.setEnabled(true);
                        btnSubmitPatent.setText("Submit");
                        Log.e(TAG, "API call failed", t);
                        Toast.makeText(PatentActivity.this,
                                "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void clearForm() {
        editPatentTitle.setText("");
        editFiledBy.setText("");
        editInventors.setText("");
        editAgency.setText("");
        editDate.setText("");
        editApplicationNumber.setText("");
        editPatentUrl.setText("");
        editSupportingDoc.setText("");
        radioGroupPatentType.clearCheck();
        radioGroupStatus.clearCheck();
    }
}