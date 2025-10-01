package com.example.voting.data.repository;

import com.example.voting.data.model.User;
import com.google.android.gms.tasks.Task;

public interface AuthRepository {
    Task<User> signInWithEmailAndPassword(String email, String password);
    Task<User> signInWithBiometric();
    Task<Void> signOut();
    Task<User> getCurrentUser();
    boolean isBiometricAvailable();
} 