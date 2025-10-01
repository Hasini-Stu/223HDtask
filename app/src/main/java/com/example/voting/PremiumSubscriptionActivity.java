package com.example.voting;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

public class PremiumSubscriptionActivity extends AppCompatActivity {
    private PaymentSheet paymentSheet;
    private String paymentIntentClientSecret;
    private String customerConfigKey;
    private static Toast lastToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium_subscription);

        // Initialize Stripe (replace with your publishable key)
        PaymentConfiguration.init(this, "YOUR_STRIPE_PUBLISHABLE_KEY_HERE"); // Replace with your actual Stripe publishable key
        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);

        ImageView closeBtn = findViewById(R.id.closeButton);
        if (closeBtn != null) {
            closeBtn.setOnClickListener(v -> finish());
        }

        findViewById(R.id.moreInfo).setOnClickListener(v ->
            showToast("More information clicked")
        );
        findViewById(R.id.subscribeButton).setOnClickListener(v -> {
            // Call backend to create subscription and get client_secret
            fetchPaymentSheetDataAndPresent();
        });
        findViewById(R.id.termsOfService).setOnClickListener(v ->
            showToast("Terms of Service clicked")
        );
        findViewById(R.id.privacyPolicy).setOnClickListener(v ->
            showToast("Privacy Policy clicked")
        );
    }

    private void fetchPaymentSheetDataAndPresent() {
        // TODO: Replace with your backend endpoint
        // This is a placeholder for demonstration. Use Retrofit/Volley/HttpUrlConnection in production.
        new Thread(() -> {
            try {
                java.net.URL url = new java.net.URL("https://your-backend.com/create-subscription");
                java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");
                // Add auth headers if needed
                java.io.OutputStream os = conn.getOutputStream();
                os.write("{}".getBytes());
                os.flush();
                os.close();
                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    java.io.InputStream is = conn.getInputStream();
                    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
                    String result = s.hasNext() ? s.next() : "";
                    try {
                        org.json.JSONObject json = new org.json.JSONObject(result);
                        if (json.has("error")) {
                            final String errorMsg = json.getString("error");
                            runOnUiThread(() -> showToast("Server error: " + errorMsg));
                            return;
                        }
                        paymentIntentClientSecret = json.getString("paymentIntent");
                        customerConfigKey = json.getString("customerEphemeralKey");
                        runOnUiThread(this::presentPaymentSheet);
                    } catch (org.json.JSONException e) {
                        runOnUiThread(() -> showToast("Invalid server response"));
                    }
                } else {
                    runOnUiThread(() -> showToast("Failed to get payment info"));
                }
            } catch (Exception e) {
                runOnUiThread(() -> showToast("Error: " + e.getMessage()));
            }
        }).start();
    }

    private void presentPaymentSheet() {
        PaymentSheet.CustomerConfiguration customerConfig = new PaymentSheet.CustomerConfiguration(
                "cus_test", // Replace with real customer ID from backend
                customerConfigKey
        );
        paymentSheet.presentWithPaymentIntent(
                paymentIntentClientSecret,
                new PaymentSheet.Configuration(
                        "SecureVote Premium",
                        customerConfig
                )
        );
    }

    private void onPaymentSheetResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            Toast.makeText(this, "Payment complete!", Toast.LENGTH_LONG).show();
            finish();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Toast.makeText(this, "Payment canceled", Toast.LENGTH_SHORT).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void showToast(String message) {
        if (lastToast != null) lastToast.cancel();
        lastToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        lastToast.show();
    }
} 