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

import com.example.intellicite.R;
import com.example.intellicite.api.ApiService;
import com.example.intellicite.api.RetrofitClient;
import com.example.intellicite.models.BookChapterRequest;
import com.example.intellicite.models.GenericResponse;
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

public class BookChapterSubmissionActivity extends AppCompatActivity {
    private EditText editFacultyName, editBookChapterTitle, editBookName, editAuthorsList, editPublisher;
    private EditText editDateOfPublication, editDOI;
    private RadioGroup authorshipRadioGroup;
    private RadioButton radioFirstAuthor, radioCoAuthor;
    private Button btnUploadPdf, btnSubmitBookChapter;
    private ImageButton hamburgerIcon, profileIcon;
    private Calendar calendar;
    private SimpleDateFormat dateFormatter;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_chapter_submit);

        // Initialize API service
        apiService = RetrofitClient.getInstance(this).getApiService();

        // Initialize date formatter
        calendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        // Initialize UI components
        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        editFacultyName = findViewById(R.id.editFacultyName);
        editBookChapterTitle = findViewById(R.id.editBookChapterTitle);
        editBookName = findViewById(R.id.editBookName);
        editAuthorsList = findViewById(R.id.editAuthorsList);
        editPublisher = findViewById(R.id.editPublisher);
        editDateOfPublication = findViewById(R.id.editDateOfPublication);
        editDOI = findViewById(R.id.editDOI);

        authorshipRadioGroup = findViewById(R.id.authorshipRadioGroup);
        radioFirstAuthor = findViewById(R.id.radioFirstAuthor);
        radioCoAuthor = findViewById(R.id.radioCoAuthor);

        btnUploadPdf = findViewById(R.id.btnUploadPdf);
        btnSubmitBookChapter = findViewById(R.id.btnSubmitBookChapter);

        hamburgerIcon = findViewById(R.id.hamburgerIcon);
        profileIcon = findViewById(R.id.profileIcon);
    }

    private void setupListeners() {
        // Date picker for publication date
        editDateOfPublication.setOnClickListener(v -> showDatePicker());

        // Submit button handler
        btnSubmitBookChapter.setOnClickListener(v -> validateAndSubmitBookChapter());

        // Upload button handler (we're not implementing file upload as requested)
        btnUploadPdf.setOnClickListener(v -> {
            Toast.makeText(this, "File upload not implemented as requested", Toast.LENGTH_SHORT).show();
        });

        // Navigation handlers
        hamburgerIcon.setOnClickListener(v -> {
            // Handle menu navigation
            Toast.makeText(BookChapterSubmissionActivity.this, "Menu clicked", Toast.LENGTH_SHORT).show();
        });

        profileIcon.setOnClickListener(v -> {
            // Handle profile navigation
            Toast.makeText(BookChapterSubmissionActivity.this, "Profile clicked", Toast.LENGTH_SHORT).show();
        });
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    editDateOfPublication.setText(dateFormatter.format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void validateAndSubmitBookChapter() {
        // Get input values
        String facultyName = editFacultyName.getText().toString().trim();
        String chapterTitle = editBookChapterTitle.getText().toString().trim();
        String bookTitle = editBookName.getText().toString().trim();
        String authorsList = editAuthorsList.getText().toString().trim();
        String publisher = editPublisher.getText().toString().trim();
        String publicationDate = editDateOfPublication.getText().toString().trim();
        String doi = editDOI.getText().toString().trim();

        // Validate fields
        if (TextUtils.isEmpty(facultyName) || TextUtils.isEmpty(chapterTitle) ||
                TextUtils.isEmpty(bookTitle) || TextUtils.isEmpty(authorsList) ||
                TextUtils.isEmpty(publisher) || TextUtils.isEmpty(publicationDate)) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get authorship type
        String authorship = radioFirstAuthor.isChecked() ? "First Author" : "Co-Author";

        // Convert authors string to List
        List<String> authorList = parseAuthorsList(authorsList);

        // Create book chapter request object
        BookChapterRequest bookChapterRequest = new BookChapterRequest(
                facultyName,
                chapterTitle,
                bookTitle,
                authorList,
                authorship,
                publisher,
                publicationDate,
                doi
        );

        // Submit book chapter data to API
        submitBookChapter(bookChapterRequest);
    }

    private List<String> parseAuthorsList(String authorsList) {
        // Split the authors string by commas
        String[] authors = authorsList.split("\\s*,\\s*");
        return new ArrayList<>(Arrays.asList(authors));
    }

    private void submitBookChapter(BookChapterRequest bookChapterRequest) {
        // Show loading state
        btnSubmitBookChapter.setEnabled(false);
        btnSubmitBookChapter.setText("Submitting...");

        // Get user ID from session manager
        String userId = new SessionManager(this).getUserId();
        if (userId == null || userId.isEmpty()) {
            Toast.makeText(BookChapterSubmissionActivity.this, "User ID not found. Please log in again.", Toast.LENGTH_LONG).show();
            btnSubmitBookChapter.setEnabled(true);
            btnSubmitBookChapter.setText("Submit");
            return;
        }

        // Make API call to create a book chapter with the user ID as the research work ID
        Call<GenericResponse> call = apiService.createBookChapter(userId, bookChapterRequest);

        call.enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                btnSubmitBookChapter.setEnabled(true);
                btnSubmitBookChapter.setText("Submit");

                if (response.isSuccessful() && response.body() != null) {
                    GenericResponse result = response.body();
                    if (result.isSuccess()) {
                        Toast.makeText(BookChapterSubmissionActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
                        // Clear form or navigate back
                        clearForm();
                    } else {
                        Toast.makeText(BookChapterSubmissionActivity.this, "Error: " + result.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Add more detailed error handling
                    try {
                        String errorBody = response.errorBody() != null ?
                                response.errorBody().string() : "Unknown error";
                        Toast.makeText(BookChapterSubmissionActivity.this,
                                "Failed to submit: " + response.code() + " - " + errorBody,
                                Toast.LENGTH_LONG).show();
                        Log.e("API_ERROR", "Error: " + response.code() + " - " + errorBody);
                    } catch (Exception e) {
                        Toast.makeText(BookChapterSubmissionActivity.this,
                                "Failed to submit book chapter. Please try again.",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                btnSubmitBookChapter.setEnabled(true);
                btnSubmitBookChapter.setText("Submit");
                Toast.makeText(BookChapterSubmissionActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("API_ERROR", "Network error", t);
            }
        });
    }



    private void clearForm() {
        editFacultyName.setText("");
        editBookChapterTitle.setText("");
        editBookName.setText("");
        editAuthorsList.setText("");
        editPublisher.setText("");
        editDateOfPublication.setText("");
        editDOI.setText("");
        radioFirstAuthor.setChecked(true);
    }
}