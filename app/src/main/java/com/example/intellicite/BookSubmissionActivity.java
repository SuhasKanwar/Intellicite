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
import com.example.intellicite.models.BookRequest;
import com.example.intellicite.models.GenericResponse;
import com.example.intellicite.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookSubmissionActivity extends AppCompatActivity {
    private static final String TAG = "BookSubmissionActivity";

    private EditText editFacultyName, editBookTitle, editAuthorsList, editPublisher, editISBN, editBookURL, editBookDate;
    private RadioGroup radioGroupEditedAuthored;
    private RadioButton radioEdited, radioAuthored;
    private Button btnSubmitBook;
    private ImageButton hamburgerIcon, profileIcon;
    private Calendar calendar;
    private SimpleDateFormat dateFormatter;
    private ApiService apiService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_submit);

        // Initialize API service
        apiService = RetrofitClient.getInstance(this).getApiService();
        sessionManager = new SessionManager(this);

        // Initialize date formatter
        calendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        // Initialize UI components
        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        editFacultyName = findViewById(R.id.editFacultyName);
        editBookTitle = findViewById(R.id.editBookTitle);
        editAuthorsList = findViewById(R.id.editAuthorsList);
        editPublisher = findViewById(R.id.editPublisher);
        editISBN = findViewById(R.id.editISBN);
        editBookURL = findViewById(R.id.editBookURL);
        editBookDate = findViewById(R.id.editBookDate);
        radioGroupEditedAuthored = findViewById(R.id.radioGroupEditedAuthored);
        radioEdited = findViewById(R.id.radioEdited);
        radioAuthored = findViewById(R.id.radioAuthored);
        btnSubmitBook = findViewById(R.id.btnSubmitBook);
        hamburgerIcon = findViewById(R.id.hamburgerIcon);
        profileIcon = findViewById(R.id.profileIcon);
    }

    private void setupListeners() {
        // Date picker for book publishing date
        editBookDate.setOnClickListener(v -> showDatePicker());

        // Submit button handler
        btnSubmitBook.setOnClickListener(v -> validateAndSubmitBook());

        // Navigation handlers
        hamburgerIcon.setOnClickListener(v -> {
            // Handle menu navigation
            // For example: Open drawer menu
            Toast.makeText(BookSubmissionActivity.this, "Menu clicked", Toast.LENGTH_SHORT).show();
        });

        profileIcon.setOnClickListener(v -> {
            // Handle profile navigation
            Toast.makeText(BookSubmissionActivity.this, "Profile clicked", Toast.LENGTH_SHORT).show();
        });
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    editBookDate.setText(dateFormatter.format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void validateAndSubmitBook() {
        // Get input values
        String facultyName = editFacultyName.getText().toString().trim();
        String bookTitle = editBookTitle.getText().toString().trim();
        String authorsList = editAuthorsList.getText().toString().trim();
        String publisher = editPublisher.getText().toString().trim();
        String isbn = editISBN.getText().toString().trim();
        String bookUrl = editBookURL.getText().toString().trim();
        String bookDate = editBookDate.getText().toString().trim();

        // Validate fields
        if (TextUtils.isEmpty(facultyName) || TextUtils.isEmpty(bookTitle) ||
                TextUtils.isEmpty(authorsList) || TextUtils.isEmpty(publisher) ||
                TextUtils.isEmpty(isbn) || TextUtils.isEmpty(bookDate)) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get book type (Edited or Authored)
        String bookType = radioEdited.isChecked() ? "Edited" : "Authored";

        // Convert authors string to List
        List<String> authorList = parseAuthorsList(authorsList);

        // Create book request object
        BookRequest bookRequest = new BookRequest(
                bookType,
                authorList,
                bookTitle,
                publisher,
                isbn,
                bookUrl,  // URL
                bookUrl,  // DOI (using same field for URL/DOI as per your form)
                bookDate
        );

        // Submit book data to API
        submitBook(bookRequest);
    }

    private List<String> parseAuthorsList(String authorsList) {
        // Split the authors string by commas
        String[] authors = authorsList.split("\\s*,\\s*");
        return new ArrayList<>(Arrays.asList(authors));
    }

    private void submitBook(BookRequest bookRequest) {
        // Show loading state
        btnSubmitBook.setEnabled(false);
        btnSubmitBook.setText("Submitting...");

        // Get or generate a research work ID
        // In a real app, this might come from an intent or a previous API call
        // For now, we'll use the user ID or a default value if not available
        String researchWorkId = sessionManager.getUserId();
        if (researchWorkId == null || researchWorkId.isEmpty()) {
            researchWorkId = "default_research_id";
            // Alternatively, generate a random ID
            // researchWorkId = UUID.randomUUID().toString();
        }

        Log.d(TAG, "Submitting book with research ID: " + researchWorkId);

        // Make API call to create a book with the research work ID
        Call<GenericResponse> call = apiService.createBook(researchWorkId, bookRequest);
        call.enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                btnSubmitBook.setEnabled(true);
                btnSubmitBook.setText("Submit");

                if (response.isSuccessful() && response.body() != null) {
                    GenericResponse result = response.body();
                    if (result.isSuccess()) {
                        Toast.makeText(BookSubmissionActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
                        // Clear form or navigate back
                        clearForm();
                    } else {
                        Toast.makeText(BookSubmissionActivity.this, "Error: " + result.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    String errorMsg = "Failed to submit book";
                    try {
                        if (response.errorBody() != null) {
                            errorMsg += ": " + response.errorBody().string();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing error response", e);
                    }

                    Toast.makeText(BookSubmissionActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Error response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                btnSubmitBook.setEnabled(true);
                btnSubmitBook.setText("Submit");
                Log.e(TAG, "Network error", t);
                Toast.makeText(BookSubmissionActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void clearForm() {
        editFacultyName.setText("");
        editBookTitle.setText("");
        editAuthorsList.setText("");
        editPublisher.setText("");
        editISBN.setText("");
        editBookURL.setText("");
        editBookDate.setText("");
        radioAuthored.setChecked(true);
    }
}