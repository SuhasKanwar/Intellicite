package com.example.intellicite.api;

import com.example.intellicite.models.BookChapterRequest;
import com.example.intellicite.models.BookRequest;
import com.example.intellicite.models.ConferenceRequest;
import com.example.intellicite.models.ConferenceResponse;
import com.example.intellicite.models.GenericResponse;
import com.example.intellicite.models.JournalRequest;
import com.example.intellicite.models.JournalResponse;
import com.example.intellicite.models.LoginRequest;
import com.example.intellicite.models.LoginResponse;
import com.example.intellicite.models.PatentRequest;
import com.example.intellicite.models.ProjectRequest;
import com.example.intellicite.models.ProjectResponse;
import com.example.intellicite.models.RegisterRequest;
import com.example.intellicite.models.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("user/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("user/sign-up")
    Call<RegisterResponse> registerUser(@Body RegisterRequest registerRequest);

    // Research submission endpoints with proper Path parameters
    @POST("research/books/{id}")
    Call<GenericResponse> createBook(@Path("id") String researchWorkId, @Body BookRequest bookRequest);

    @POST("research/book-chapters/{id}")
    Call<GenericResponse> createBookChapter(@Path("id") String researchWorkId, @Body BookChapterRequest bookChapterRequest);

    @POST("research/journals/{id}")
    Call<JournalResponse> createJournal(@Path("id") String researchWorkId, @Body JournalRequest journalRequest);

    @POST("research/conferences/{id}")
    Call<ConferenceResponse> submitConference(@Path("id") String researchWorkId, @Body ConferenceRequest conferenceRequest);

    @POST("research/patents/{id}")
    Call<GenericResponse> createPatent(@Path("id") String researchWorkId, @Body PatentRequest patentRequest);

    @POST("research/projects/{id}")
    Call<ProjectResponse> submitProject(@Path("id") String researchWorkId, @Body ProjectRequest projectRequest);
}