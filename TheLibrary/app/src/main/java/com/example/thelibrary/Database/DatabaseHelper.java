package com.example.thelibrary.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.thelibrary.Model.Book;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "library.db";
    private static final String TABLE_NAME = "BOOKS_DETAIL";
    private static final String COL_ID = "id";
    private static final String COL_IMAGE = "BOOK_image";
    private static final String COL_NAME = "BOOK_name";
    private static final String COL_DESCRIPTION = "BOOK_description";
    private static final String COL_RACK = "BOOK_rack";
    private static final String COL_STEP = "BOOK_step";
    private static final String COL_COLUMN = "BOOK_colm";
    private static final String COL_POSITION = "BOOK_position";
    private Context _context;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
        this._context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE "+TABLE_NAME+ " (" + COL_ID + " integer primary key autoincrement, " + COL_IMAGE + " BLOB, " + COL_NAME + " text, " + COL_DESCRIPTION + " text, " + COL_RACK + " text, " + COL_STEP + " text, " + COL_COLUMN + " text, " + COL_POSITION + " text )";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String query = "DROP TABLE IF EXISTS "+ TABLE_NAME;
        sqLiteDatabase.execSQL(query);
        onCreate(sqLiteDatabase);
    }

    public boolean addOneBook(Book book){
        byte[] image = book.getImage();
        String name = book.getName();
        String desc = book.getDescription();
        String rack = book.getRack();
        String step = book.getStep();
        String colm = book.getColumn();
        String pos = book.getPosition();

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_IMAGE,image);
        contentValues.put(COL_NAME,name);
        contentValues.put(COL_DESCRIPTION,desc);
        contentValues.put(COL_RACK,rack);
        contentValues.put(COL_STEP,step);
        contentValues.put(COL_COLUMN,colm);
        contentValues.put(COL_POSITION,pos);

        long newRowId = db.insert(TABLE_NAME,null,contentValues);
        if(newRowId == -1){
            Toast.makeText(_context, "Error while Insert", Toast.LENGTH_SHORT).show();
            return false;
        }
        Toast.makeText(_context, "saved", Toast.LENGTH_SHORT).show();
        return true;
    }

    public Book getBookById(int id){
        Book book = null;
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE "+COL_ID +" = "+id;
        try (Cursor cursor = db.rawQuery(query, null)) {

            while (cursor.moveToNext()) {
                byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(COL_IMAGE));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION));
                String rack = cursor.getString(cursor.getColumnIndexOrThrow(COL_RACK));
                String step = cursor.getString(cursor.getColumnIndexOrThrow(COL_STEP));
                String colm = cursor.getString(cursor.getColumnIndexOrThrow(COL_COLUMN));
                String pos = cursor.getString(cursor.getColumnIndexOrThrow(COL_POSITION));

                book = new Book(id,image,name,desc,rack,step,colm,pos);
            }
        }
        if(book==null) Toast.makeText(_context, "Error getting BookByID", Toast.LENGTH_SHORT).show();
        return book;
    }

    public boolean updateBook(Book book){
        byte[] image = book.getImage();
        String name = book.getName();
        String desc = book.getDescription();
        String rack = book.getRack();
        String step = book.getStep();
        String colm = book.getColumn();
        String pos = book.getPosition();

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_IMAGE,image);
        contentValues.put(COL_NAME,name);
        contentValues.put(COL_DESCRIPTION,desc);
        contentValues.put(COL_RACK,rack);
        contentValues.put(COL_STEP,step);
        contentValues.put(COL_COLUMN,colm);
        contentValues.put(COL_POSITION,pos);

        int val = db.update(TABLE_NAME,contentValues, COL_ID+" = "+book.getId(),null);
        return val>0;
    }

    public boolean deleteBookById(int id){
        SQLiteDatabase db = getReadableDatabase();
        int val = db.delete(TABLE_NAME, COL_ID +" = "+id,null);
        return (val>0);
    }

    public List<Book> getAllBooks(){
        List<Book> books = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM "+ TABLE_NAME+" ORDER BY "+COL_NAME;
        try (Cursor cursor = db.rawQuery(query, null)) {

            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
                byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(COL_IMAGE));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION));
                String rack = cursor.getString(cursor.getColumnIndexOrThrow(COL_RACK));
                String step = cursor.getString(cursor.getColumnIndexOrThrow(COL_STEP));
                String colm = cursor.getString(cursor.getColumnIndexOrThrow(COL_COLUMN));
                String pos = cursor.getString(cursor.getColumnIndexOrThrow(COL_POSITION));

                Book book = new Book(id,image,name,desc,rack,step,colm,pos);
                books.add(book);
            }
        }

        return books;
    }

    public List<Book> getAllBooks(String s_name){
        List<Book> books = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM "+ TABLE_NAME+"WHERE "+COL_NAME+" like"+ " '%" + s_name + "%' "+" ORDER BY "+COL_NAME;
        try (Cursor cursor = db.rawQuery(query, null)) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
                byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(COL_IMAGE));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION));
                String rack = cursor.getString(cursor.getColumnIndexOrThrow(COL_RACK));
                String step = cursor.getString(cursor.getColumnIndexOrThrow(COL_STEP));
                String colm = cursor.getString(cursor.getColumnIndexOrThrow(COL_COLUMN));
                String pos = cursor.getString(cursor.getColumnIndexOrThrow(COL_POSITION));

                Book book = new Book(id,image,name,desc,rack,step,colm,pos);
                books.add(book);
            }
        }
        Toast.makeText(_context, books.toString(), Toast.LENGTH_LONG).show();

        return books;
    }

}
