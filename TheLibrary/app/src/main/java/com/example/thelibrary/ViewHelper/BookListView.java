package com.example.thelibrary.ViewHelper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thelibrary.AddBookFormActivity;
import com.example.thelibrary.BookViewActivity;
import com.example.thelibrary.Database.DatabaseHelper;
import com.example.thelibrary.Model.Book;
import com.example.thelibrary.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BookListView extends RecyclerView.Adapter<BookListView.ViewHolder> {


    private Context _context;
    private List<Book> books;
    private List<Book> filteredBooks;


    public BookListView(Context _context, List<Book> books) {
        this._context = _context;
        this.books = books;
        this.filteredBooks = new ArrayList<>(books);
    }

    @NonNull
    @Override
    public BookListView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_view_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookListView.ViewHolder holder, int position) {



        Book book = filteredBooks.get(position);
        byte[] image = book.getImage();
        String desc = book.getName();
        String pos = book.getRack()+"-"+book.getStep()+"-"+book.getColumn()+"-"+book.getPosition();
        if(desc.length() > 50) desc = desc.substring(0,50) +"...";

        holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(image,0,image.length));
        holder.textView_name.setText(book.getName());
        holder.textView_desc.setText(book.getDescription());
        holder.textView_pos.setText(pos);

        holder.thisView.setOnClickListener(view -> {
            Intent intent = new Intent(_context, BookViewActivity.class);
            intent.putExtra("BOOK_ID",book.getId());
            _context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return filteredBooks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public  View thisView;
        public ImageView imageView;
        public TextView textView_name;
        public TextView textView_desc;
        public TextView textView_pos;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view_id);
            textView_name = itemView.findViewById(R.id.book_name_txt_view);
            textView_desc = itemView.findViewById(R.id.book_desc_txt_view);
            textView_pos = itemView.findViewById(R.id.book_address_txt_view);
            this.thisView = itemView;


        }
    }

    public Filter getFilter(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String query = charSequence.toString().toLowerCase();
                if(query.isEmpty() || query.equals("")) filteredBooks = new ArrayList<>(books);
                else{
                    List<Book> temp = new ArrayList<>();
                    for(Book book:books){
                        if(book.getName().toLowerCase().contains(query)) temp.add(book);
                    }
                    filteredBooks = temp;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredBooks;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredBooks = (List<Book>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
