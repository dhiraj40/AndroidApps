package com.example.thelibrary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thelibrary.Database.DatabaseHelper;
import com.example.thelibrary.Model.Book;

public class BookViewActivity extends AppCompatActivity {


    ImageView imageView;
    TextView view_name, view_desc, view_rack, view_step, view_column, view_position;
    Button edit_button, delete_button;

    Book book;
    DatabaseHelper databaseHelper;
    String BOOK_ID = "BOOK_ID";
    int book_id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_book_view_layout);

        //
        Intent intent = getIntent();
        book_id = intent.getIntExtra(BOOK_ID,-1);

        //Init component
        imageView = findViewById(R.id.image_view_id);
        view_name = findViewById(R.id.book_name_id);
        view_desc = findViewById(R.id.book_desc_id);
        view_rack = findViewById(R.id.book_rack_id);
        view_step = findViewById(R.id.book_step_id);
        view_column = findViewById(R.id.book_colm_id);
        view_position = findViewById(R.id.book_pos_id);

        edit_button = findViewById(R.id.edit_button);
        delete_button = findViewById(R.id.delete_button);

        //
        databaseHelper = new DatabaseHelper(BookViewActivity.this);
        //
        book = databaseHelper.getBookById(book_id);
        imageView.setImageBitmap(book.getBitmapImage());
        view_name.setText(book.getName());
        view_rack.setText("RACK : "+book.getRack());
        view_desc.setText(book.getDescription());
        view_step.setText("STEP : "+book.getStep());
        view_column.setText("COLUMN : "+book.getColumn());
        view_position.setText("POSITION : "+book.getPosition());

        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit_intent = new Intent(BookViewActivity.this, AddBookFormActivity.class);
                edit_intent.putExtra("MODE","EDIT");
                edit_intent.putExtra("CONTEXT","BOOK_VIEW_ACTIVITY");
                startActivity(edit_intent);
            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BookViewActivity.this);
                builder.setMessage("Are you sure?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                databaseHelper.deleteBookById(book_id);
                                Intent intent_home = new Intent(BookViewActivity.this, MainActivity.class);
                                startActivity(intent_home);
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .show();

            }
        });
    }
}











