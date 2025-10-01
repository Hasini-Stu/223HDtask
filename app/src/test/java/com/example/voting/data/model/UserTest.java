package com.example.voting.data.model;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Unit tests for User model class
 */
public class UserTest {
    
    private User user;
    
    @Before
    public void setUp() {
        user = new User();
    }
    
    @Test
    public void testUserCreation() {
        assertNotNull("User should not be null", user);
    }
    
    @Test
    public void testUserId() {
        String testId = "test-user-id";
        user.setId(testId);
        assertEquals("User ID should match", testId, user.getId());
    }
    
    @Test
    public void testUserEmail() {
        String testEmail = "test@example.com";
        user.setEmail(testEmail);
        assertEquals("User email should match", testEmail, user.getEmail());
    }
    
    @Test
    public void testUserName() {
        String testName = "Test User";
        user.setName(testName);
        assertEquals("User name should match", testName, user.getName());
    }
    
    @Test
    public void testUserIsPremium() {
        user.setPremium(true);
        assertTrue("User should be premium", user.isPremium());
        
        user.setPremium(false);
        assertFalse("User should not be premium", user.isPremium());
    }
    
    @Test
    public void testUserVoteCount() {
        int testVoteCount = 10;
        user.setVoteCount(testVoteCount);
        assertEquals("User vote count should match", testVoteCount, user.getVoteCount());
    }
}
