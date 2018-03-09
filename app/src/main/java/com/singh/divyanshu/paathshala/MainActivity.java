package com.singh.divyanshu.paathshala;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private EditText username;
    private RadioButton teacher,student;
    private EditText password;
    private Button login;
    private  String s;
    private Button register;
    private Intent iTeacher,iStudent;
    TeacherStudent teacherStudent;
    private static final String TAG = "MainActivity";
    private FirebaseDatabase database=FirebaseDatabase.getInstance();
    private DatabaseReference myRef=database.getReference("TeacherStudent");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        teacher=findViewById(R.id.Teacher);
        student=findViewById(R.id.Student);
        iTeacher=new Intent(MainActivity.this,Teacher.class);
        iStudent=new Intent(MainActivity.this,Student.class);
        username=findViewById(R.id.user);
        password=findViewById(R.id.pass);
        login=findViewById(R.id.login);
        register=findViewById(R.id.registerlogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(username.getText().toString(),password.getText().toString());
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,Register.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }

    private void signIn(final String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        if(!(teacher.isChecked()||student.isChecked()))
        {
            Toast.makeText(MainActivity.this,"select Either Teacher or Student",Toast.LENGTH_SHORT).show();
        }
        else if (teacher.isChecked())
        {
            myRef=myRef.child("teachers");
            Query query=myRef.orderByChild("teacherorstudent").equalTo(email.substring(0,email.indexOf("@")));
            query.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                   teacherStudent=dataSnapshot.getValue(TeacherStudent.class);
                    Log.e("childAddedTeacher",dataSnapshot.getKey());
                    MainActivity.this.s=teacherStudent.getTeacherstudent();
                    Toast.makeText(MainActivity.this,s,Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                   teacherStudent=dataSnapshot.getValue(TeacherStudent.class);
                    Log.e("childChangedTeacher",dataSnapshot.getKey());
                    MainActivity.this.s=teacherStudent.getTeacherstudent();
                    Toast.makeText(MainActivity.this,s,Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        else if (student.isChecked())
        {
            myRef=myRef.child("students");
            Query query=myRef.orderByChild("teacherorstudent").equalTo(email.substring(0,email.indexOf("@")));
            query.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    teacherStudent=dataSnapshot.getValue(TeacherStudent.class);
                    Log.e("childAddedStudent",dataSnapshot.getKey());
                    MainActivity.this.s=teacherStudent.getTeacherstudent();
                    Toast.makeText(MainActivity.this,s,Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    teacherStudent=dataSnapshot.getValue(TeacherStudent.class);
                    Log.e("childChangedStudent",dataSnapshot.getKey());
                    MainActivity.this.s=teacherStudent.getTeacherstudent();
                    Toast.makeText(MainActivity.this,s,Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, "Welcome aboard",
                                    Toast.LENGTH_SHORT).show();
                            if (MainActivity.this.s.equalsIgnoreCase(email.substring(0,email.indexOf("@")))&&student.isChecked())
                            {
                                iStudent=new Intent(MainActivity.this,Student.class);
                                iStudent.putExtra("username",MainActivity.this.s);
                                startActivity(iStudent);
                            }
                            else if(MainActivity.this.s.equalsIgnoreCase(email.substring(0,email.indexOf("@")))&&teacher.isChecked())
                            {
                                iTeacher=new Intent(MainActivity.this,Teacher.class);
                                iTeacher.putExtra("username",MainActivity.this.s);
                                startActivity(iTeacher);
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    private boolean validateForm() {
        boolean valid = true;

        String e_mail = username.getText().toString();
        if (TextUtils.isEmpty(e_mail)) {
            username.setError("Required.");
            valid = false;
        } else {
            username.setError(null);
        }

        String pass = password.getText().toString();
        if (TextUtils.isEmpty(pass)) {
            password.setError("Required.");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }

}
