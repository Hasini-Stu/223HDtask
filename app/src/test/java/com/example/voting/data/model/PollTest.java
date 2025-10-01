package com.example.voting.data.model;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Unit tests for Poll model class
 */
public class PollTest {
    
    private Poll poll;
    
    @Before
    public void setUp() {
        poll = new Poll();
    }
    
    @Test
    public void testPollCreation() {
        assertNotNull("Poll should not be null", poll);
    }
    
    @Test
    public void testPollId() {
        String testId = "test-poll-id";
        poll.setId(testId);
        assertEquals("Poll ID should match", testId, poll.getId());
    }
    
    @Test
    public void testPollTitle() {
        String testTitle = "Test Poll Title";
        poll.setTitle(testTitle);
        assertEquals("Poll title should match", testTitle, poll.getTitle());
    }
    
    @Test
    public void testPollDescription() {
        String testDescription = "Test Poll Description";
        poll.setDescription(testDescription);
        assertEquals("Poll description should match", testDescription, poll.getDescription());
    }
    
    @Test
    public void testPollVoteCount() {
        int testVoteCount = 42;
        poll.setVoteCount(testVoteCount);
        assertEquals("Poll vote count should match", testVoteCount, poll.getVoteCount());
    }
    
    @Test
    public void testPollIsActive() {
        poll.setActive(true);
        assertTrue("Poll should be active", poll.isActive());
        
        poll.setActive(false);
        assertFalse("Poll should not be active", poll.isActive());
    }
}
