package com.example.thelibrary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.thelibrary.Database.DatabaseHelper;
import com.example.thelibrary.Model.Book;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddBookFormActivity extends AppCompatActivity {

    ImageView imageView;
    EditText text_name,text_desc, text_rack, text_step, text_colm;
    RadioButton radio_front, radio_back;
    Button button_choose_pic, button_submit_book;
    AlertDialog alertDialogChoosePic;

    String FORM_MODE = "";
    String FORM_CONTEXT = "";
    int book_id =  -1;

    DatabaseHelper databaseHelper;

    static final int REQUEST_IMAGE_CAPTURE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book_form);

        Intent intent_from = getIntent();
        FORM_MODE =  intent_from.getStringExtra("MODE");
        FORM_CONTEXT = intent_from.getStringExtra("CONTEXT");

        imageView = findViewById(R.id.image_view_id);
        text_name = findViewById(R.id.txt_add_book_name);
        text_desc = findViewById(R.id.txt_add_book_desc);
        text_rack = findViewById(R.id.txt_add_book_rack);
        text_step = findViewById(R.id.txt_add_book_step);
        text_colm = findViewById(R.id.txt_add_book_colm);
        radio_front = findViewById(R.id.radio_add_book_pos_front);
        radio_back = findViewById(R.id.radio_add_book_pos_back);
        button_choose_pic = findViewById(R.id.choose_img_but);
        button_submit_book = findViewById(R.id.add_book_button);

        databaseHelper = new DatabaseHelper(this);

        if(FORM_MODE.equals("EDIT"))
        {
            book_id = intent_from.getIntExtra("BOOK_ID",-1);
            Book book = databaseHelper.getBookById(book_id);
            imageView.setImageBitmap(book.getBitmapImage());
            text_name.setText(book.getName());
            text_desc.setText(book.getDescription());
            text_rack.setText(book.getRack());
            text_step.setText(book.getStep());
            text_colm.setText(book.getColumn());
            if(book.isBack())  radio_back.setChecked(true);
            Toast.makeText(this, book.toString(), Toast.LENGTH_LONG).show();
        }

        button_choose_pic.setOnClickListener(view -> {
            chooseBookPic();
        });

        button_submit_book.setOnClickListener(view -> {
            imageView.invalidate();
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            String name = text_name.getText().toString();
            String desc = text_desc.getText().toString();
            String rack = text_rack.getText().toString();
            String step = text_step.getText().toString();
            String colm = text_colm.getText().toString();
            String pos = "";
            if(radio_front.isChecked()) pos = "FRONT";
            else if(radio_back.isChecked()) pos = "BACK";
            else Toast.makeText(AddBookFormActivity.this, "Please Select front or back", Toast.LENGTH_SHORT).show();

            Book book = new Book(bitmap,name,desc,rack,step,colm,pos);
            boolean result_flag = false;
            if(FORM_MODE.equals("ADD")) result_flag = databaseHelper.addOneBook(book);
            else if(FORM_MODE.equals("EDIT")) {
                book.setId(book_id);
                result_flag = databaseHelper.updateBook(book);
            }

            if(result_flag){
                Intent intent_to = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent_to);
            }
        });

    }

    private void chooseBookPic() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddBookFormActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.pic_choose_layout,null);
        builder.setCancelable(false);
        builder.setView(dialogView);

        ImageView imageView_default = dialogView.findViewById(R.id.default_pic_but);
        ImageView imageView_camera = dialogView.findViewById(R.id.camera_open_id);
        ImageView imageView_gallery = dialogView.findViewById(R.id.gallery_open_id);

        alertDialogChoosePic = builder.create();
        alertDialogChoosePic.show();

        imageView_default.setOnClickListener(view -> {
            setDefaultPic();
            alertDialogChoosePic.cancel();
        });
        imageView_camera.setOnClickListener(view -> {
            if(checkAndRequestPermissions()){
                dispatchTakePictureIntent();
            }
        });
        imageView_gallery.setOnClickListener(view -> getPicFromGallery());
    }

    private boolean checkAndRequestPermissions() {
        if(Build.VERSION.SDK_INT >= 23){
            int cameraPermission = ActivityCompat.checkSelfPermission(AddBookFormActivity.this, Manifest.permission.CAMERA);
            if(cameraPermission == PackageManager.PERMISSION_DENIED){
                ActivityCompat.requestPermissions(AddBookFormActivity.this,new String[]{Manifest.permission.CAMERA},5005);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 5005 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            dispatchTakePictureIntent();
        }
        else Toast.makeText(AddBookFormActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
    }

    private void dispatchTakePictureIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
    }

    private void getPicFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,5000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            if(imageBitmap == null) Toast.makeText(AddBookFormActivity.this, "Camera giving null result", Toast.LENGTH_SHORT).show();
            else imageView.setImageBitmap(imageBitmap);
        }
        else if(resultCode==RESULT_OK){
            try {
                Uri imageUri = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(selectedImage);
            }catch (Exception w){
                Toast.makeText(AddBookFormActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
            }
        }

        alertDialogChoosePic.cancel();
    }

    private void setDefaultPic() {
        imageView.setImageResource(R.drawable.default_img);
    }

}