package com.example.thelibrary;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.thelibrary.Database.DatabaseHelper;
import com.example.thelibrary.Model.Book;
import com.example.thelibrary.ViewHelper.BookListView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.io.ByteArrayOutputStream;
import java.io.CharArrayReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;

    List<Book> bookList;
    DatabaseHelper database;
    BookListView adapter;
    EditText search_bar;
    CharSequence search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingActionButton = findViewById(R.id.add_book_float_but);
        recyclerView = findViewById(R.id.recycler_view_books_list);
        search_bar = findViewById(R.id.search_bar);

        floatingActionButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(),AddBookFormActivity.class);
            intent.putExtra("MODE","ADD");
            intent.putExtra("CONTEXT","MAIN");
            startActivity(intent);
        });

        database = new DatabaseHelper(MainActivity.this);

        bookList = database.getAllBooks();
        //initiate();

        showBooksOnRecyclerView();

        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
                search = charSequence;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void initiate() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_img);
        Book book = new Book(bitmap,"Hanuman chalisa","Hanuman Chalisa...","1","3","66","front");
        bookList = new ArrayList<>();

        for(int i=0;i<20;i++) bookList.add(book);

    }

    private void showBooksOnRecyclerView() {
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(MainActivity.this,DividerItemDecoration.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter = new BookListView(MainActivity.this,bookList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.nav_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.app_bar_info:
                Toast.makeText(this, "Info bar Clicked...", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void reloadData(){
        onResume();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bookList = database.getAllBooks();
        showBooksOnRecyclerView();
    }
}














