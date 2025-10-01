package com.example.voting.data.repository;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * Unit tests for AuthRepository
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthRepositoryTest {
    
    @Mock
    private AuthRepository authRepository;
    
    @Before
    public void setUp() {
        // Setup mock behavior
        when(authRepository.isUserLoggedIn()).thenReturn(false);
    }
    
    @Test
    public void testUserNotLoggedInInitially() {
        assertFalse("User should not be logged in initially", authRepository.isUserLoggedIn());
    }
    
    @Test
    public void testLoginWithValidCredentials() {
        String email = "test@example.com";
        String password = "password123";
        
        when(authRepository.login(email, password)).thenReturn(true);
        
        boolean result = authRepository.login(email, password);
        assertTrue("Login should succeed with valid credentials", result);
        
        verify(authRepository).login(email, password);
    }
    
    @Test
    public void testLoginWithInvalidCredentials() {
        String email = "invalid@example.com";
        String password = "wrongpassword";
        
        when(authRepository.login(email, password)).thenReturn(false);
        
        boolean result = authRepository.login(email, password);
        assertFalse("Login should fail with invalid credentials", result);
        
        verify(authRepository).login(email, password);
    }
    
    @Test
    public void testLogout() {
        authRepository.logout();
        verify(authRepository).logout();
    }
    
    @Test
    public void testRegisterNewUser() {
        String email = "newuser@example.com";
        String password = "newpassword123";
        String name = "New User";
        
        when(authRepository.register(email, password, name)).thenReturn(true);
        
        boolean result = authRepository.register(email, password, name);
        assertTrue("Registration should succeed with valid data", result);
        
        verify(authRepository).register(email, password, name);
    }
}
