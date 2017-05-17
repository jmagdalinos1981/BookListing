package com.example.android.booklisting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * {@link BookItemAdapter} is an {@link ArrayAdapter} that can provide the layout for each list item
 * based on a data source, which is a list of {@link BookItem} objects.
 */

public class BookItemAdapter extends ArrayAdapter<BookItem> {
    Context mContext;
    /**
     * Create a new {@link BookItemAdapter} object.
     *
     * @param context is the current context (i.e. Activity) that the adapter is being created in.
     * @param bookItems is the list of {@link BookItem}s to be displayed.
     */
    public BookItemAdapter(Context context, ArrayList<BookItem> bookItems) {
        super(context, 0, bookItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        View bookItemView = convertView;
        if (bookItemView == null) {
            bookItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_list_item, parent, false);
        }
        // Get the {@link BookItem} object located at this position in the list
        BookItem currentBookItem = getItem(position);

        // Find the ImageView in the book_list_item.xml layout with the ID title.
        ImageView thumbnailImageView = (ImageView) bookItemView.findViewById(R.id.book_thumbnail);
        // Check if there is a link to an image, otherwise use placehoder image.
        if (currentBookItem.getmSmallThumbnailURL() != null) {
            Picasso.with(getContext()).load(currentBookItem.getmSmallThumbnailURL()).into(thumbnailImageView);
        } else {
            thumbnailImageView.setImageResource(R.drawable.no_thumbnail);
        }
        // Find the TextView in the book_list_item.xml layout with the ID book_title.
        TextView titleTextView = (TextView) bookItemView.findViewById(R.id.book_title);
        // Get the Title from the currentBookItem object and set this text on the TextView.
        titleTextView.setText(currentBookItem.getmTitle());

        // Find the TextView in the book_list_item.xml layout with the ID book_author.
        TextView authorTextView = (TextView) bookItemView.findViewById(R.id.book_author);
        // Get the Author from the currentBookItem object and set this text on the TextView.
        authorTextView.setText(currentBookItem.getmAuthor());

        // Find the TextView in the book_list_item.xml layout with the ID book_page_count.
        TextView pageCountTextView = (TextView) bookItemView.findViewById(R.id.book_page_count);
        // Get the Page Count from the currentBookItem object and set this text on the TextView.
        pageCountTextView.setText(currentBookItem.getmPageCount());
        
        // Find the TextView in the book_list_item.xml layout with the ID book_language.
        TextView languageTextView = (TextView) bookItemView.findViewById(R.id.book_language);
        // Get the Language from the currentBookItem object and set this text on the TextView.
        languageTextView.setText(currentBookItem.getmLanguage());
        
        // Find the TextView in the book_list_item.xml layout with the ID book_description.
        TextView descriptionTextView = (TextView) bookItemView.findViewById(R.id.book_description);
        // Get the Description from the currentBookItem object and set this text on the TextView.
        descriptionTextView.setText(currentBookItem.getmDescription());

        return bookItemView;
    }

}
