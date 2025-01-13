package com.example.applogin;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso; // Used for loading profile image from a URL

public class DashboardActivity extends AppCompatActivity {

    // Declare the TextViews and ImageView for displaying user data
    private TextView firstNameTextView, lastNameTextView, ageTextView, addressTextView, phoneTextView, emailTextView;
    private ImageView profileImageView;
    private DatabaseReference databaseReference; // Reference to Firebase database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard); // Set layout for the activity

        // Initialize views by finding them from the layout
        firstNameTextView = findViewById(R.id.firstNameTextView);
        lastNameTextView = findViewById(R.id.lastNameTextView);
        ageTextView = findViewById(R.id.ageTextView);
        addressTextView = findViewById(R.id.addressTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        emailTextView = findViewById(R.id.emailTextView);
        profileImageView = findViewById(R.id.profileImageView);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Get username from MainActivity (it was passed through Intent)
        String username = getIntent().getStringExtra("username");

        // If username is not null, retrieve the user data from Firebase
        if (username != null) {
            loadUserData(username); // Call function to load user data
        }
    }

    private void loadUserData(String username) {
        // Listen for data of the given username in the Firebase Database
        databaseReference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Extract user data from the snapshot
                    String firstName = dataSnapshot.child("firstName").getValue(String.class);
                    String lastName = dataSnapshot.child("lastName").getValue(String.class);
                    String age = dataSnapshot.child("age").getValue(Long.class).toString();
                    String address = dataSnapshot.child("address").getValue(String.class);
                    String phone = dataSnapshot.child("phone").getValue(Long.class).toString();
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String profileImageUrl = dataSnapshot.child("profileImageUrl").getValue(String.class);

                    // Display the extracted data in the corresponding views
                    firstNameTextView.setText("First name: " + firstName);
                    lastNameTextView.setText("Last name: " +lastName);
                    ageTextView.setText("Age: " +age);
                    addressTextView.setText("Address: " +address);
                    phoneTextView.setText("Phone: " +phone);
                    emailTextView.setText("Email: " +email);

                    // Load profile image if URL exists using Picasso
                    if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                        Picasso.get().load(profileImageUrl).into(profileImageView);
                    }
                } else {
                    // Show a toast message if user data is not found
                    Toast.makeText(DashboardActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Show a toast message if database error occurs
                Toast.makeText(DashboardActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
