import os
from flask import Flask, request, jsonify
import stripe

app = Flask(__name__)

# Set your Stripe secret key (test key)
stripe.api_key = "YOUR_STRIPE_SECRET_KEY_HERE"  # Replace with your actual Stripe secret key

@app.route("/create-subscription", methods=["POST"])
def create_subscription():
    try:
        data = request.get_json() or {}

        # 1. Create a customer (or use an existing one)
        customer = stripe.Customer.create(
            email=data.get("email", "test@example.com"),
            name=data.get("name", "Test User")
        )

        # 2. Create an ephemeral key for the customer
        ephemeral_key = stripe.EphemeralKey.create(
            customer=customer["id"],
            stripe_version="2023-10-16"
        )

        # 3. Create a subscription (replace with your test price ID)
        price_id = "YOUR_STRIPE_PRICE_ID_HERE"  # Replace with your actual Stripe price ID
        subscription = stripe.Subscription.create(
            customer=customer["id"],
            items=[{"price": price_id}],
            payment_behavior="default_incomplete",
            expand=["latest_invoice.payment_intent"]
        )

        payment_intent = subscription["latest_invoice"]["payment_intent"]

        return jsonify({
            "paymentIntent": payment_intent["client_secret"],
            "customerEphemeralKey": ephemeral_key.secret,
            "customerId": customer["id"]
        })
    except Exception as e:
        return jsonify({"error": str(e)}), 400

if __name__ == "__main__":
    app.run(port=4242, debug=True) 