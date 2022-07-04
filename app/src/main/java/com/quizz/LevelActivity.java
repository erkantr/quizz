package com.quizz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.quizz.adapter.LevelAdapter;
import com.quizz.model.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class LevelActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    RecyclerView recyclerView;
    LevelAdapter levelAdapter;
    List<Category> categories;
    List<Integer> list;
    public static String categoryname;
    int level;
    ArrayAdapter arrayAdapter;

    static final float END_SCALE = 0.7f;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView navToggler_btn;
    LinearLayout linearLayout;
    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); ///Eneter into fullscreen mode
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(LevelActivity.this));
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navToggler_btn = findViewById(R.id.action_menu_presenter);
        linearLayout = findViewById(R.id.main_content);
        floatingActionButton = findViewById(R.id.fab_button);

        categories = new ArrayList<>();
        list = new ArrayList<>();
        Intent intent = getIntent();
        categoryname = intent.getStringExtra("category");
        //String category_level = intent.getStringExtra("level");
        readLevels();
        navigationDrawer();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LevelActivity.this,QuestionAddActivity.class);
                startActivity(intent);
            }
        });
    }

    public void readLevels(){
        DatabaseReference reference = FirebaseDatabase.getInstance("https://quizzapp-6e34b-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Categories");
        Query query=reference.orderByChild("categoryName").equalTo(categoryname);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Category category = dataSnapshot.getValue(Category.class);
                    for (int i = 0; i<=Integer.parseInt(category.getLevel()); i++ ){
                        if (i!=0){
                            level = i;
                            categories.add(category);
                            //recyclerView.setAdapter(levelAdapter);
                            arrayAdapter = new ArrayAdapter(LevelActivity.this,R.layout.support_simple_spinner_dropdown_item,categories);

                            int as =arrayAdapter.getCount();

                        }
                    }
                }
                for (int a = 0; a<=arrayAdapter.getCount(); a++){
                    if (a!= 0){
                        list.add(a);
                        levelAdapter = new LevelAdapter(LevelActivity.this, list ,categories);
                        recyclerView.setAdapter(levelAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void navigationDrawer() {

        //Navigation Drawer

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.home);

        navToggler_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        animateNavigationDrawer();

    }

    ////////////////////////////////////////////////////////////ANIMATE NAV DRAWER////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void animateNavigationDrawer() {
        drawerLayout.setScrimColor(getResources().getColor(R.color.orange));
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                final float diffScaledOffset = slideOffset*(1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                linearLayout.setScaleX(offsetScale);
                linearLayout.setScaleY(offsetScale);


                final float xOffset = drawerView.getWidth()*slideOffset;
                final float xOffsetDiff = linearLayout.getWidth()*diffScaledOffset/2;
                final float xTranslation = xOffset - xOffsetDiff;
                linearLayout.setTranslationX(xTranslation);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        if (menuItem.getItemId() == R.id.home) {
            Intent home = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(home);
            LevelActivity.super.onBackPressed();

        }
        else if (menuItem.getItemId() == R.id.user_profile) {

            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(intent);
            LevelActivity.super.onBackPressed();}
        else if (menuItem.getItemId() == R.id.about) {
            //open about activity
            Intent about = new Intent(getApplicationContext(), CategoryActivity.class);
            startActivity(about);
        } else if (menuItem.getItemId() == R.id.logout) {
            //Logout
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
        return true;
    }
}