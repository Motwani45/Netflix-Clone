package com.example.netflix.mainscreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.netflix.R;
import com.example.netflix.activities.SignInActivity;
import com.example.netflix.databinding.ActivitySettingsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseNetworkException;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class Settings extends AppCompatActivity {
    ActivitySettingsBinding binding;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseUser user;
    DocumentReference reference;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    String userid, useremail, plan;
    Date ending;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        binding.bottomnaviagtionbar.getMenu().getItem(2).setChecked(true);
        getSupportActionBar().hide();
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressDialog = new ProgressDialog(Settings.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        if (user != null) {
            userid = user.getUid();
            reference = firebaseFirestore.collection("Users").document(userid);
            reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    ending = documentSnapshot.getDate("Ending");
                    useremail = documentSnapshot.getString("Email");
                    plan = documentSnapshot.getString("Price");
                    binding.emailsettings.setText(useremail);
                    binding.plansettings.setText("₹ " + plan + "/mo");
                    binding.datesettings.setText(ending.toString());
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    progressDialog.cancel();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (e instanceof FirebaseNetworkException) {
                        Toast.makeText(getApplicationContext(), "NO internet connection", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "Error data not fetched", Toast.LENGTH_SHORT).show();
                    }
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    progressDialog.cancel();
                }
            });
        }
        binding.signoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder signout = new AlertDialog.Builder(view.getContext());
                signout.setTitle("Do you really want to signout ?");
                signout.setMessage("Press YES to signout");
                signout.setCancelable(false);
                signout.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth.getInstance().signOut();
                        Intent x = new Intent(Settings.this, SignInActivity.class);
                        startActivity(x);
                        finish();

                    }
                });
                signout.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                signout.create().show();
            }
        });
        binding.bottomnaviagtionbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemid = item.getItemId();
                switch (itemid) {
                    case R.id.homeicon:
                        Intent intent2 = new Intent(Settings.this, MainScreen.class);
                        startActivity(intent2);
                        break;
                    case R.id.searchicon:
                        Intent intent = new Intent(Settings.this, Search.class);
                        startActivity(intent);
                        break;
                    case R.id.settingsicon:
                        break;
                }

                return false;
            }
        });
        binding.resetpasswordbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog = new ProgressDialog(Settings.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                String oldpassword = binding.resetpasswordedittext.getText().toString();
                if (binding.resetpasswordedittext.getText().toString().length() > 7) {
                    firebaseAuth.signInWithEmailAndPassword(useremail, oldpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                EditText changepassword = new EditText(view.getContext());
                                AlertDialog.Builder updatepassword = new AlertDialog.Builder(view.getContext());
                                updatepassword.setTitle("Update Password?");
                                updatepassword.setCancelable(false);
                                changepassword.setHint("New password");
                                changepassword.setSingleLine();
                                updatepassword.setView(changepassword);
                                updatepassword.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        progressDialog.show();
                                        String newpasswordstring = changepassword.getText().toString();
                                        if (newpasswordstring.length() > 7) {
                                            user.updatePassword(newpasswordstring).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(getApplicationContext(), "Password Updated", Toast.LENGTH_SHORT).show();
                                                    binding.resetpasswordedittext.setText("");
                                                    progressDialog.cancel();

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    if (e instanceof FirebaseNetworkException)
                                                        Toast.makeText(getApplicationContext(), "NO internet connection", Toast.LENGTH_SHORT).show();
                                                    else {
                                                        Toast.makeText(getApplicationContext(), "Password not updated", Toast.LENGTH_SHORT).show();
                                                    }
                                                    progressDialog.cancel();
                                                }
                                            });

                                        } else {
                                            progressDialog.cancel();
                                            Toast.makeText(getApplicationContext(), "Password to short please retry ", Toast.LENGTH_SHORT).show();

                                        }


                                    }
                                });
                                updatepassword.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        binding.resetpasswordedittext.setText("");
                                        progressDialog.cancel();
                                    }
                                });
                                updatepassword.create().show();
                            }

                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (e instanceof FirebaseNetworkException)
                                Toast.makeText(getApplicationContext(), "NO internet connection", Toast.LENGTH_SHORT).show();
                            if (e instanceof FirebaseAuthInvalidCredentialsException)
                                binding.resetpasswordedittext.setError("Incorrect password");
                            else
                                binding.resetpasswordedittext.setError("Incorrect password");
                            progressDialog.cancel();

                        }
                    });

                } else {
                    binding.resetpasswordedittext.setError("Password to short");
                    progressDialog.cancel();
                }

            }
        });
    }
}