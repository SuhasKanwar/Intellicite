package com.example.intellicite;

import android.app.ProgressDialog;
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

import com.example.intellicite.R;
import com.example.intellicite.api.ApiService;
import com.example.intellicite.api.RetrofitClient;
import com.example.intellicite.models.ConferenceRequest;
import com.example.intellicite.models.ConferenceResponse;
import com.example.intellicite.utils.SessionManager;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubmitConferenceActivity extends AppCompatActivity {

    // UI components
    private EditText editFacultyName, editConferenceTitle, editAuthorsList;
    private EditText editConferenceName, editLocationDates, editDOI, editDateOfPublication;
    private RadioGroup authorshipRadioGroup, indexingRadioGroup;
    private Button btnSubmitConference;
    private ImageButton hamburgerIcon, profileIcon;

    // API and session management
    private ApiService apiService;
    private SessionManager sessionManager;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conferences_submit_page);



        // Initialize Session Manager and API Service
        sessionManager = new SessionManager(this);
        apiService = RetrofitClient.getInstance(this).getApiService();

        // Initialize progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Submitting conference data...");
        progressDialog.setCancelable(false);

        // Initialize UI components
        initializeViews();
        setupClickListeners();

    }

    private void initializeViews() {
        // Top section
        hamburgerIcon = findViewById(R.id.hamburgerIcon);
        profileIcon = findViewById(R.id.profileIcon);

        // Form fields
        editFacultyName = findViewById(R.id.editFacultyName);
        editConferenceTitle = findViewById(R.id.conference_title);
        editAuthorsList = findViewById(R.id.editAuthorsList);
        editConferenceName = findViewById(R.id.editConferenceName);
        editLocationDates = findViewById(R.id.editLocationDates);
        editDOI = findViewById(R.id.editDOI);
        editDateOfPublication = findViewById(R.id.editDateOfPublication);

        // Radio groups
        authorshipRadioGroup = findViewById(R.id.authorshipRadioGroup);
        indexingRadioGroup = findViewById(R.id.indexingRadioGroup);

        // Buttons
        btnSubmitConference = findViewById(R.id.btnSubmitConference);
    }

    private void setupClickListeners() {
        // Submit button click listener
        btnSubmitConference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSubmitConference();
            }
        });

        // Hamburger menu click listener
        hamburgerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Implement menu functionality
                Toast.makeText(SubmitConferenceActivity.this, "Menu clicked", Toast.LENGTH_SHORT).show();
            }
        });

        // Profile icon click listener
        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Implement profile functionality
                Toast.makeText(SubmitConferenceActivity.this, "Profile clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void validateAndSubmitConference() {
        // Get values from form fields
        String facultyName = editFacultyName.getText().toString().trim();
        String conferenceTitle = editConferenceTitle.getText().toString().trim();
        String authorsListString = editAuthorsList.getText().toString().trim();
        String conferenceName = editConferenceName.getText().toString().trim();
        String locationDates = editLocationDates.getText().toString().trim();
        String doi = editDOI.getText().toString().trim();
        String dateOfPublication = editDateOfPublication.getText().toString().trim();

        // Check for empty fields
        if (TextUtils.isEmpty(facultyName) || TextUtils.isEmpty(conferenceTitle) ||
                TextUtils.isEmpty(authorsListString) || TextUtils.isEmpty(conferenceName) ||
                TextUtils.isEmpty(locationDates) || TextUtils.isEmpty(dateOfPublication)) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get selected radio button values
        int selectedAuthorshipId = authorshipRadioGroup.getCheckedRadioButtonId();
        int selectedIndexingId = indexingRadioGroup.getCheckedRadioButtonId();

        if (selectedAuthorshipId == -1 || selectedIndexingId == -1) {
            Toast.makeText(this, "Please select all options", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get authorship value
        String authorship = "";
        if (selectedAuthorshipId == R.id.radioFirstAuthor) {
            authorship = "First Author";
        } else if (selectedAuthorshipId == R.id.radioCoAuthor) {
            authorship = "Co-Author";
        }

        // Get indexing value
        String indexing = "";
        if (selectedIndexingId == R.id.radioIndexedYes) {
            indexing = "Yes";
        } else if (selectedIndexingId == R.id.radioIndexedNo) {
            indexing = "No";
        }

        // Split and process location and dates
        String conferenceLocation = "";
        String conferenceDate = "";
        if (locationDates.contains(",")) {
            String[] parts = locationDates.split(",", 2);
            conferenceLocation = parts[0].trim();
            conferenceDate = parts.length > 1 ? parts[1].trim() : "";
        } else {
            conferenceLocation = locationDates;
        }

        // Split authors list by commas
        List<String> authorsList = Arrays.asList(authorsListString.split("\\s*,\\s*"));

        // Create conference request
        ConferenceRequest conferenceRequest = new ConferenceRequest(
                facultyName,
                conferenceTitle,
                authorsList,
                authorship,
                conferenceName,
                conferenceLocation,
                conferenceDate,
                doi,
                dateOfPublication,
                indexing
        );

        // Submit data to API
        submitConference(conferenceRequest);
    }

    private void submitConference(ConferenceRequest request) {
        progressDialog.show();

        // Get user ID from session manager
        String userId = sessionManager.getUserId();
        if (userId == null || userId.isEmpty()) {
            progressDialog.dismiss();
            Toast.makeText(SubmitConferenceActivity.this, "User ID not found. Please log in again.", Toast.LENGTH_LONG).show();
            return;
        }

        // Pass the user ID as the research work ID
        Call<ConferenceResponse> call = apiService.submitConference(userId, request);

        call.enqueue(new Callback<ConferenceResponse>() {
            @Override
            public void onResponse(Call<ConferenceResponse> call, Response<ConferenceResponse> response) {
                progressDialog.dismiss();

                if (response.isSuccessful() && response.body() != null) {
                    ConferenceResponse conferenceResponse = response.body();
                    if (conferenceResponse.isSuccess()) {
                        Toast.makeText(SubmitConferenceActivity.this,
                                "Conference submitted successfully",
                                Toast.LENGTH_LONG).show();
                        clearForm();
                    } else {
                        Toast.makeText(SubmitConferenceActivity.this,
                                "Failed: " + conferenceResponse.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Add more detailed error handling
                    try {
                        String errorBody = response.errorBody() != null ?
                                response.errorBody().string() : "Unknown error";
                        Toast.makeText(SubmitConferenceActivity.this,
                                "Error: " + response.code() + " - " + errorBody,
                                Toast.LENGTH_LONG).show();
                        Log.e("API_ERROR", "Error: " + response.code() + " - " + errorBody);
                    } catch (Exception e) {
                        Toast.makeText(SubmitConferenceActivity.this,
                                "Error: Server error",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ConferenceResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(SubmitConferenceActivity.this,
                        "Network error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
                Log.e("API_ERROR", "Network error", t);
            }
        });
    }

    private void clearForm() {
        editFacultyName.setText("");
        editConferenceTitle.setText("");
        editAuthorsList.setText("");
        editConferenceName.setText("");
        editLocationDates.setText("");
        editDOI.setText("");
        editDateOfPublication.setText("");
        authorshipRadioGroup.clearCheck();
        indexingRadioGroup.clearCheck();
    }
}