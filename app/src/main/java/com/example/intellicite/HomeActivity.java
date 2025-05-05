package com.example.intellicite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.intellicite.utils.SessionManager;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    // UI Elements
    private ImageButton hamburgerIcon;
    private ImageButton profileIcon;
    private TextView greetingText;
    private BarChart barChart;
    private LinearLayout plusMenuLayout;
    private ImageButton homeBtn;
    private ImageButton plusBtn;
    private ImageButton searchBtn;

    // Menu buttons
    private Button journalButton;
    private Button conferencesButton;
    private Button booksButton;
    private Button booksChapterButton;
    private Button projectsButton;
    private Button patentsButton;

    // Session manager for user data
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_user); // Assuming your XML file is named activity_home.xml

        // Initialize session manager
        sessionManager = new SessionManager(this);

        // Check if user is logged in
        if (!sessionManager.isLoggedIn()) {
            // If not logged in, redirect to login page
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Initialize UI components
        initializeViews();

        // Set up click listeners
        setupClickListeners();

        // Set up the bar chart
        setupBarChart();

        // Set greeting with user's name
        updateGreetingWithUserName();
    }

    private void updateGreetingWithUserName() {
        // Get user's name from SessionManager
        String userName = sessionManager.getUserName();

        // Create greeting message
        String greeting = "Welcome, " + userName + "!";

        // Set the greeting text
        greetingText.setText(greeting);
    }

    private void initializeViews() {
        // Top bar
        hamburgerIcon = findViewById(R.id.hamburger_icon);
        profileIcon = findViewById(R.id.profile_icon);

        // Greeting text
        greetingText = findViewById(R.id.greeting_text);

        // Bar chart
        barChart = findViewById(R.id.barChart);

        // Plus menu
        plusMenuLayout = findViewById(R.id.plus_menu_layout);
        journalButton = findViewById(R.id.journal_button);
        conferencesButton = findViewById(R.id.conferences_button);
        booksButton = findViewById(R.id.books_button);
        booksChapterButton = findViewById(R.id.Books_Chapter_button);
        projectsButton = findViewById(R.id.Projects_button);
        patentsButton = findViewById(R.id.Patents_button);

        // Bottom navigation
        homeBtn = findViewById(R.id.home_btn);
        plusBtn = findViewById(R.id.plus_btn);
        searchBtn = findViewById(R.id.search_btn);
    }

    private void setupClickListeners() {
        // Top bar listeners
        hamburgerIcon.setOnClickListener(v -> {
            Toast.makeText(HomeActivity.this, "Menu clicked", Toast.LENGTH_SHORT).show();
            // TODO: Implement menu functionality
        });

        profileIcon.setOnClickListener(v -> {
            Toast.makeText(HomeActivity.this, "Profile clicked", Toast.LENGTH_SHORT).show();
            // TODO: Navigate to profile page when you create a ProfileActivity
            // Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            // startActivity(intent);
        });

        // Plus menu listeners
        journalButton.setOnClickListener(v -> {
            Toast.makeText(HomeActivity.this, "Opening Journal section", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(HomeActivity.this, JournalActivity.class);
            startActivity(intent);
            plusMenuLayout.setVisibility(View.GONE);
        });

        conferencesButton.setOnClickListener(v -> {
            Toast.makeText(HomeActivity.this, "Opening Conferences section", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(HomeActivity.this, SubmitConferenceActivity.class);
            startActivity(intent);
            plusMenuLayout.setVisibility(View.GONE);
        });

        booksButton.setOnClickListener(v -> {
            Toast.makeText(HomeActivity.this, "Opening Books section", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(HomeActivity.this, BookSubmissionActivity.class);
            startActivity(intent);
            plusMenuLayout.setVisibility(View.GONE);
        });

        booksChapterButton.setOnClickListener(v -> {
            Toast.makeText(HomeActivity.this, "Opening Book Chapters section", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(HomeActivity.this, BookChapterSubmissionActivity.class);
            startActivity(intent);
            plusMenuLayout.setVisibility(View.GONE);
        });

        projectsButton.setOnClickListener(v -> {
            Toast.makeText(HomeActivity.this, "Opening Projects section", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(HomeActivity.this, SubmitProjectActivity.class);
            startActivity(intent);
            plusMenuLayout.setVisibility(View.GONE);
        });

        patentsButton.setOnClickListener(v -> {
            Toast.makeText(HomeActivity.this, "Opening Patents section", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(HomeActivity.this, PatentActivity.class);
            startActivity(intent);
            plusMenuLayout.setVisibility(View.GONE);
        });

        // Bottom navigation listeners
        homeBtn.setOnClickListener(v -> {
            // Already on home page
            Toast.makeText(HomeActivity.this, "Already on Home page", Toast.LENGTH_SHORT).show();
            plusMenuLayout.setVisibility(View.GONE);
        });

        plusBtn.setOnClickListener(v -> {
            // Toggle the visibility of the plus menu
            if (plusMenuLayout.getVisibility() == View.VISIBLE) {
                plusMenuLayout.setVisibility(View.GONE);
            } else {
                plusMenuLayout.setVisibility(View.VISIBLE);
            }
        });

        searchBtn.setOnClickListener(v -> {
            Toast.makeText(HomeActivity.this, "Search clicked", Toast.LENGTH_SHORT).show();
            plusMenuLayout.setVisibility(View.GONE);
            // TODO: Create and navigate to SearchActivity when you implement it
            // Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
            // startActivity(intent);
        });

        // Set up click listeners for horizontal scrolling cards
        setupCardClickListeners();
    }

    private void setupCardClickListeners() {
        // Find all CardViews in the horizontal scroll view
        LinearLayout horizontalLayout = findViewById(R.id.horizontal_card_layout);

        // Check if we found the layout
        if (horizontalLayout != null) {
            // Loop through child views
            for (int i = 0; i < horizontalLayout.getChildCount(); i++) {
                View child = horizontalLayout.getChildAt(i);

                if (child instanceof CardView) {
                    CardView card = (CardView) child;
                    final int position = i; // Capture the position for use in lambda
                    card.setOnClickListener(v -> {
                        Toast.makeText(HomeActivity.this, "Journal card " + (position + 1) + " clicked", Toast.LENGTH_SHORT).show();
                        // Navigate to JournalActivity with the selected journal's details
                        Intent intent = new Intent(HomeActivity.this, JournalActivity.class);
                        intent.putExtra("JOURNAL_POSITION", position);
                        startActivity(intent);
                    });
                }
            }
        }
    }

    private void setupBarChart() {
        // Sample data for the bar chart
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 4f));
        entries.add(new BarEntry(1f, 8f));
        entries.add(new BarEntry(2f, 6f));
        entries.add(new BarEntry(3f, 12f));
        entries.add(new BarEntry(4f, 18f));
        entries.add(new BarEntry(5f, 9f));

        BarDataSet dataSet = new BarDataSet(entries, "Publication Count");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);

        // Customize the chart
        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);
        barChart.animateY(1000);

        // Customize X axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);

        // Set custom labels for X axis
        List<String> labels = new ArrayList<>();
        labels.add("Jan");
        labels.add("Feb");
        labels.add("Mar");
        labels.add("Apr");
        labels.add("May");
        labels.add("Jun");

        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        // Customize Y axis
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f);

        barChart.getAxisRight().setEnabled(false);
        barChart.getLegend().setEnabled(false);

        // Refresh the chart
        barChart.invalidate();
    }

    @Override
    public void onBackPressed() {
        // Check if plus menu is open
        if (plusMenuLayout.getVisibility() == View.VISIBLE) {
            plusMenuLayout.setVisibility(View.GONE);
        } else {
            // Handle regular back button press
            super.onBackPressed();
        }
    }

    // Handle logout functionality
    public void logout() {
        // Clear session data
        sessionManager.clearSession();

        // Navigate to login screen
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}