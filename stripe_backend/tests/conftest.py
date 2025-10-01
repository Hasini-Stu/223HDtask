import pytest
import os
import sys

# Add the parent directory to the path so we can import the app
sys.path.insert(0, os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

@pytest.fixture(scope="session")
def test_environment():
    """Set up test environment variables."""
    # Set test Stripe keys (these are fake test keys)
    os.environ['STRIPE_SECRET_KEY'] = 'sk_test_fake_key_for_testing'
    os.environ['STRIPE_PUBLISHABLE_KEY'] = 'pk_test_fake_key_for_testing'
    yield
    # Clean up after tests
    if 'STRIPE_SECRET_KEY' in os.environ:
        del os.environ['STRIPE_SECRET_KEY']
    if 'STRIPE_PUBLISHABLE_KEY' in os.environ:
        del os.environ['STRIPE_PUBLISHABLE_KEY']
