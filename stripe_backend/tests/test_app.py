import pytest
import json
from unittest.mock import patch, MagicMock
import stripe_backend.app as app

@pytest.fixture
def client():
    """Create a test client for the Flask app."""
    app.app.config['TESTING'] = True
    with app.app.test_client() as client:
        yield client

class TestSubscriptionEndpoint:
    """Test cases for the /create-subscription endpoint."""
    
    @patch('stripe_backend.app.stripe.Customer.create')
    @patch('stripe_backend.app.stripe.EphemeralKey.create')
    @patch('stripe_backend.app.stripe.Subscription.create')
    def test_create_subscription_success(self, mock_subscription, mock_ephemeral, mock_customer, client):
        """Test successful subscription creation."""
        # Mock Stripe API responses
        mock_customer.return_value = {"id": "cus_test123"}
        mock_ephemeral.return_value.secret = "ek_test123"
        mock_subscription.return_value = {
            "latest_invoice": {
                "payment_intent": {
                    "client_secret": "pi_test123_secret"
                }
            }
        }
        
        # Test data
        test_data = {
            "email": "test@example.com",
            "name": "Test User"
        }
        
        # Make request
        response = client.post('/create-subscription', 
                             data=json.dumps(test_data),
                             content_type='application/json')
        
        # Assertions
        assert response.status_code == 200
        data = json.loads(response.data)
        assert 'paymentIntent' in data
        assert 'customerEphemeralKey' in data
        assert 'customerId' in data
        
        # Verify Stripe API calls
        mock_customer.assert_called_once()
        mock_ephemeral.assert_called_once()
        mock_subscription.assert_called_once()
    
    @patch('stripe_backend.app.stripe.Customer.create')
    def test_create_subscription_stripe_error(self, mock_customer, client):
        """Test subscription creation with Stripe error."""
        # Mock Stripe error
        mock_customer.side_effect = Exception("Stripe API error")
        
        # Test data
        test_data = {
            "email": "test@example.com",
            "name": "Test User"
        }
        
        # Make request
        response = client.post('/create-subscription',
                             data=json.dumps(test_data),
                             content_type='application/json')
        
        # Assertions
        assert response.status_code == 400
        data = json.loads(response.data)
        assert 'error' in data
        assert data['error'] == "Stripe API error"
    
    def test_create_subscription_no_data(self, client):
        """Test subscription creation with no data."""
        response = client.post('/create-subscription')
        
        # Should still work with default values
        assert response.status_code == 200
    
    def test_create_subscription_invalid_json(self, client):
        """Test subscription creation with invalid JSON."""
        response = client.post('/create-subscription',
                             data="invalid json",
                             content_type='application/json')
        
        # Should handle gracefully
        assert response.status_code == 200

class TestAppConfiguration:
    """Test app configuration and setup."""
    
    def test_app_creation(self):
        """Test that the Flask app is created correctly."""
        assert app.app is not None
        assert app.app.config['TESTING'] == False  # Default value
    
    def test_stripe_configuration(self):
        """Test that Stripe is configured."""
        # This test assumes Stripe API key is set
        assert hasattr(app, 'stripe')
        assert app.stripe.api_key is not None

if __name__ == '__main__':
    pytest.main([__file__])
