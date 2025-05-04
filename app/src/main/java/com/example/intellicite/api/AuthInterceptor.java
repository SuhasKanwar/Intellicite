package com.example.intellicite.api;

import android.content.Context;
import android.util.Log;

import com.example.intellicite.utils.SessionManager;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * AuthInterceptor - Adds authentication token to API requests
 */
public class AuthInterceptor implements Interceptor {
    private static final String TAG = "AuthInterceptor";
    private final SessionManager sessionManager;

    public AuthInterceptor(Context context) {
        this.sessionManager = new SessionManager(context);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        // Only add auth header if user is logged in
        if (!sessionManager.isLoggedIn()) {
            return chain.proceed(originalRequest);
        }

        String token = sessionManager.getAuthToken();
        if (token != null && !token.isEmpty()) {
            Log.d(TAG, "Adding auth token to request: " + originalRequest.url());

            // Create a new request with Authorization header
            Request.Builder requestBuilder = originalRequest.newBuilder()
                    .header("Authorization", "Bearer " + token)  // Correct format for Bearer token
                    .method(originalRequest.method(), originalRequest.body());

            Request request = requestBuilder.build();
            return chain.proceed(request);
        } else {
            Log.w(TAG, "Auth token is null or empty");
            return chain.proceed(originalRequest);
        }
    }
}