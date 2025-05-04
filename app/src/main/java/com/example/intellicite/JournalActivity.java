package com.example.intellicite;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intellicite.api.RetrofitClient;
import com.example.intellicite.models.JournalRequest;
import com.example.intellicite.models.JournalResponse;
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

public class JournalActivity extends AppCompatActivity {
    private static final String TAG = "JournalActivity";

    private EditText editFacultyName;
    private EditText editResearchTitle;
    private EditText editJournalName;
    private EditText editAuthorsList;
    private EditText editDOI;
    private EditText editDateOfPublication;
    private RadioGroup authorshipRadioGroup;
    private RadioGroup scopusIndexedRadioGroup;
    private Spinner spinnerQuartile;
    private Button btnSubmit;
    private Button btnUploadPdf;
    private ImageButton hamburgerIcon;
    private ImageButton profileIcon;

    private Calendar calendar;
    private ProgressDialog progressDialog;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journal_submit_page);

        // Initialize session manager
        sessionManager = new SessionManager(this);

        // Initialize components
        initComponents();
        setupListeners();
    }

    private void initComponents() {
        editFacultyName = findViewById(R.id.editFacultyName);
        editResearchTitle = findViewById(R.id.editResearchTitle);
        editJournalName = findViewById(R.id.editJournalName);
        editAuthorsList = findViewById(R.id.editAuthorsList);
        editDOI = findViewById(R.id.editDOI);
        editDateOfPublication = findViewById(R.id.editDateOfPublication);
        authorshipRadioGroup = findViewById(R.id.authorshipRadioGroup);
        scopusIndexedRadioGroup = findViewById(R.id.scopusIndexedRadioGroup);
        spinnerQuartile = findViewById(R.id.spinnerQuartile);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnUploadPdf = findViewById(R.id.btnUploadPdf);
        hamburgerIcon = findViewById(R.id.hamburgerIcon);
        profileIcon = findViewById(R.id.profileIcon);

        calendar = Calendar.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Submitting journal...");
        progressDialog.setCancelable(false);

        // Setup quartile spinner if not already defined in XML
        if (spinnerQuartile.getAdapter() == null) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    this, R.array.quartile_options, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerQuartile.setAdapter(adapter);
        }
    }

    private void setupListeners() {
        // Date picker for publication date
        editDateOfPublication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Submit button
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    submitJournal();
                }
            }
        });

        // Upload PDF button (just a placeholder, as per requirements)
        btnUploadPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(JournalActivity.this, "File upload functionality not required.", Toast.LENGTH_SHORT).show();
            }
        });

        // Navigation buttons
        hamburgerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle navigation drawer or back action
                onBackPressed();
            }
        });

        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle profile action
                Toast.makeText(JournalActivity.this, "Profile action", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDateLabel();
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void updateDateLabel() {
        String dateFormat = "yyyy-MM-dd"; // Using ISO format for API compatibility
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        editDateOfPublication.setText(sdf.format(calendar.getTime()));
    }

    private boolean validateInputs() {
        boolean isValid = true;

        // Validate faculty name
        if (TextUtils.isEmpty(editFacultyName.getText())) {
            editFacultyName.setError("Faculty name is required");
            isValid = false;
        }

        // Validate research title
        if (TextUtils.isEmpty(editResearchTitle.getText())) {
            editResearchTitle.setError("Research title is required");
            isValid = false;
        }

        // Validate journal name
        if (TextUtils.isEmpty(editJournalName.getText())) {
            editJournalName.setError("Journal name is required");
            isValid = false;
        }

        // Validate authors list
        if (TextUtils.isEmpty(editAuthorsList.getText())) {
            editAuthorsList.setError("Authors list is required");
            isValid = false;
        }

        // Validate authorship selection
        if (authorshipRadioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select authorship type", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        // Validate scopus indexed selection
        if (scopusIndexedRadioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select whether the paper is Scopus indexed", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        // Validate publication date
        if (TextUtils.isEmpty(editDateOfPublication.getText())) {
            editDateOfPublication.setError("Publication date is required");
            isValid = false;
        }

        return isValid;
    }

    private void submitJournal() {
        progressDialog.show();

        // Get all values
        String facultyName = editFacultyName.getText().toString().trim();
        String journalTitle = editResearchTitle.getText().toString().trim();
        String journalName = editJournalName.getText().toString().trim();

        // Parse authors list (split by commas)
        String authorsListStr = editAuthorsList.getText().toString().trim();
        List<String> authorList = new ArrayList<>(Arrays.asList(authorsListStr.split("\\s*,\\s*")));

        // Get authorship type
        int selectedAuthorshipId = authorshipRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedAuthorship = findViewById(selectedAuthorshipId);
        String authorship = selectedAuthorship.getText().toString();

        // Get DOI
        String doi = editDOI.getText().toString().trim();

        // Get publication date
        String datePublished = editDateOfPublication.getText().toString().trim();

        // Get journal quartile
        String journalQuartile = spinnerQuartile.getSelectedItem().toString();

        // Get scopus indexed status
        int selectedScopusId = scopusIndexedRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedScopus = findViewById(selectedScopusId);
        boolean scopusIndexed = selectedScopus.getText().toString().equals("Yes");

        // Create journal request
        JournalRequest journalRequest = new JournalRequest(
                facultyName, journalTitle, journalName, authorList, authorship,
                doi, datePublished, journalQuartile, scopusIndexed
        );

        // Get research work ID from user session or intent
        // In a real app, you might get this from the intent extras or another source
        String researchWorkId = getResearchWorkId();
        Log.d(TAG, "Submitting journal for research work ID: " + researchWorkId);

        // Make API call with the research work ID
        Call<JournalResponse> call = RetrofitClient.getInstance(this)
                .getApiService()
                .createJournal(researchWorkId, journalRequest);

        call.enqueue(new Callback<JournalResponse>() {
            @Override
            public void onResponse(Call<JournalResponse> call, Response<JournalResponse> response) {
                progressDialog.dismiss();

                if (response.isSuccessful() && response.body() != null) {
                    JournalResponse journalResponse = response.body();
                    if (journalResponse.isSuccess()) {
                        Toast.makeText(JournalActivity.this,
                                "Journal submitted successfully", Toast.LENGTH_LONG).show();
                        // Close activity or navigate back
                        finish();
                    } else {
                        Toast.makeText(JournalActivity.this,
                                "Error: " + journalResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        Log.e(TAG, "API Error: " + errorBody);
                        Toast.makeText(JournalActivity.this,
                                "Failed to submit journal. Status code: " + response.code(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing error response", e);
                        Toast.makeText(JournalActivity.this,
                                "Failed to submit journal. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JournalResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, "Network error", t);
                Toast.makeText(JournalActivity.this,
                        "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
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