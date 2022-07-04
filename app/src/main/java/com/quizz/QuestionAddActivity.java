package com.quizz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Random;

import static com.quizz.LevelActivity.categoryname;

public class QuestionAddActivity extends AppCompatActivity {

    EditText editTextQuestion, editTextA, editTextB, editTextC, editTextD, editTextE, editTextCorrect,getEditTextLevel;
    Button add;
    FirebaseUser fuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_add);
        editTextQuestion = findViewById(R.id.editTextQuestion);
        editTextA = findViewById(R.id.editTextA);
        editTextB = findViewById(R.id.editTextB);
        editTextC = findViewById(R.id.editTextC);
        editTextD = findViewById(R.id.editTextD);
        editTextE = findViewById(R.id.editTextE);
        editTextCorrect = findViewById(R.id.editTextCorrect);
        getEditTextLevel = findViewById(R.id.editTextLevel);
        add = findViewById(R.id.add);

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        String level = intent.getStringExtra("level");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_question = editTextQuestion.getText().toString();
                String txt_A = editTextA.getText().toString();
                String txt_B = editTextB.getText().toString();
                String txt_C = editTextC.getText().toString();
                String txt_D = editTextD.getText().toString();
                String txt_E = editTextE.getText().toString();
                String txt_correct = editTextCorrect.getText().toString();
                String txt_level = getEditTextLevel.getText().toString();


                if (TextUtils.isEmpty(txt_question) || TextUtils.isEmpty(txt_B) || TextUtils.isEmpty(txt_A) || TextUtils.isEmpty(txt_C) ||
                        TextUtils.isEmpty(txt_D) || TextUtils.isEmpty(txt_E) || TextUtils.isEmpty(txt_correct) || TextUtils.isEmpty(txt_level)){
                    Toast.makeText(QuestionAddActivity.this, "Tüm alanlar zorunludur!", Toast.LENGTH_SHORT).show();
                } else {
                    addQuestion(categoryname,txt_question, txt_A,txt_B,txt_C,txt_D,txt_E,txt_correct,txt_level);
                    Intent intent = new Intent(QuestionAddActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
    public void addQuestion(String category,String question,String a,String b,String c,String d,String e,String correct,String level){
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String key = database.getReference("quiz").push().getKey();
        DatabaseReference reference = FirebaseDatabase.getInstance("https://quizzapp-6e34b-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Questions").child(key);

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("category", category);
                    hashMap.put("question", question);
                    hashMap.put("a", a);
                    hashMap.put("b", b);
                    hashMap.put("c", c);
                    hashMap.put("d", d);
                    hashMap.put("e", e);
                    hashMap.put("correct", correct);
                    hashMap.put("level", level);
                    reference.updateChildren(hashMap);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }
}