package com.quizz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.quizz.model.Category;
import com.quizz.model.User;

import java.io.IOException;
import java.util.HashMap;

public class CategoryAddActivity extends AppCompatActivity {

    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;
    StorageReference storageReference;
    DatabaseReference reference;
    FirebaseUser fuser;
    ImageView imageView;
    Button add;
    EditText categoryName,level;
    String txt_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_add);
        imageView = findViewById(R.id.addimage);
        add = findViewById(R.id.add);
        categoryName = findViewById(R.id.editTextCategory);
        level = findViewById(R.id.editTextLevel);

        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        if (ActivityCompat.shouldShowRequestPermissionRationale(CategoryAddActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE )) {
            ActivityCompat.requestPermissions(CategoryAddActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            ActivityCompat.requestPermissions(CategoryAddActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openImage();
                }
            });

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String txt_level=level.getText().toString();
                    txt_category= categoryName.getText().toString();

                    if (TextUtils.isEmpty(txt_level) || TextUtils.isEmpty(txt_category)){
                        Toast.makeText(CategoryAddActivity.this, "Tüm alanlar zorunludur!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (imageView.getDrawable() == null || imageUri == null) {
                            Toast.makeText(CategoryAddActivity.this, "Lütfen Kategoriyi Temsil Eden Bir Görsel Seçin", Toast.LENGTH_SHORT).show();
                        } else {
                            addCategory(txt_category,txt_level);
                            Intent intent = new Intent(CategoryAddActivity.this,MainActivity.class);
                            startActivity(intent);
                        }
                    }
                }
            });
    }
    public void addCategory(String name, String level){
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance("https://quizzapp-6e34b-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Categories").child(txt_category);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Category category = snapshot.getValue(Category.class);
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("categoryName", name);
                hashMap.put("level", level);
                reference.updateChildren(hashMap);
                if (uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(CategoryAddActivity.this, "Upload in preogress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadImage();
                   // finish();
                }
               // Glide.with(CategoryAddActivity.this).load(category.getImage()).into(imageView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        //final ProgressDialog pd = new ProgressDialog(this);
        //pd.setMessage("Uploading");
       // pd.show();

        if (imageUri != null) {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        reference = FirebaseDatabase.getInstance("https://quizzapp-6e34b-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Categories").child(txt_category);
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("image", "" + mUri);
                        reference.updateChildren(map);
                        uploadTask.cancel();

                       // pd.dismiss();
                      //  finish();
                    } else {
                        Toast.makeText(CategoryAddActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                        //pd.dismiss();
                    }


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CategoryAddActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                   // pd.dismiss();
                }
            });
        } else {
            Toast.makeText(CategoryAddActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageView.setImageBitmap(imageToStore);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(CategoryAddActivity.this, "Upload in preogress", Toast.LENGTH_SHORT).show();
            } else {
               // uploadImage();
            }
        }
    }
}