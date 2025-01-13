package com.example.applogin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    // Declare EditText fields and Register button
    private EditText firstNameEditText, lastNameEditText, usernameEditText, ageEditText, passwordEditText, addressEditText, phoneEditText, emailEditText;
    private Button registerButton;

    private DatabaseReference databaseReference; // Firebase reference to store user data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_page); // Set layout for the activity

        // Initialize EditText views
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        ageEditText = findViewById(R.id.ageEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        addressEditText = findViewById(R.id.addressEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);
        registerButton = findViewById(R.id.registerButton);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Set click listener for register button
        registerButton.setOnClickListener(v -> {
            // Extract input values
            String firstName = firstNameEditText.getText().toString().trim();
            String lastName = lastNameEditText.getText().toString().trim();
            String username = usernameEditText.getText().toString().trim();
            String ageStr = ageEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String address = addressEditText.getText().toString().trim();
            String phoneStr = phoneEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();

            // Validate input fields
            if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(username) ||
                    TextUtils.isEmpty(ageStr) || TextUtils.isEmpty(password) || TextUtils.isEmpty(address) ||
                    TextUtils.isEmpty(phoneStr) || TextUtils.isEmpty(email)) {
                Toast.makeText(RegistrationActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Parse age and phone, handle exceptions if invalid
            int age;
            long phone;
            try {
                age = Integer.parseInt(ageStr);
                phone = Long.parseLong(phoneStr);
            } catch (NumberFormatException e) {
                Toast.makeText(RegistrationActivity.this, "Invalid age or phone number", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a new user object with the input data
            User user = new User(firstName, lastName, username, address, phone, email, age, password);

            // Save user data in Firebase under the username key
            databaseReference.child(username).setValue(user).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(RegistrationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    finish(); // Close the registration activity
                } else {
                    Toast.makeText(RegistrationActivity.this, "Registration Failed. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    // User model class to represent user data in Firebase
    public static class User {
        public String firstName;
        public String lastName;
        public String username;
        public String address;
        public long phone;
        public String email;
        public int age;
        public String password;

        public User() {
            // Default constructor for Firebase
        }

        public User(String firstName, String lastName, String username, String address, long phone, String email, int age, String password) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.username = username;
            this.address = address;
            this.phone = phone;
            this.email = email;
            this.age = age;
            this.password = password;
        }
    }
}
