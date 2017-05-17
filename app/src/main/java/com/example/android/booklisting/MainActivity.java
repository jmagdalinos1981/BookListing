package com.example.android.booklisting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    /** Tag for the log messages */
    public static final String LOG_TAG = MainActivity.class.getName();

    /** Context variable so it can be referenced in other activities */
    public static Context mContext;

    /** EditText for user's search */
    EditText searchTitleEditText;
    EditText searchAuthorEditText;

    /** Button which will call BookListActivity */
    Button searchButton;

    /** Buttons clearing EditTexts */
    Button clearTitleButton;
    Button clearAuthorButton;

    /** Spinner with reslt number options */
    Spinner spinner;

    /** String containing the user's input */
    public static String userSearchInputAuthor;
    public static String userSearchInputTitle;

    /** String containing number of results */
    public static String resultNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();

        // Initialize Views
        searchTitleEditText = (EditText) findViewById(R.id.search_title_EditText);
        searchAuthorEditText = (EditText) findViewById(R.id.search_author_EditText);
        searchButton = (Button) findViewById(R.id.search_Button);
        clearTitleButton = (Button) findViewById(R.id.clear_title_button);
        clearAuthorButton = (Button) findViewById(R.id.clear_author_button);

        // Set OnClickListeners
        searchButton.setOnClickListener(this);
        clearTitleButton.setOnClickListener(this);
        clearAuthorButton.setOnClickListener(this);

        // Disable and hide clear buttons if the EditTexts are empty
        if (searchTitleEditText.getText().length() == 0) {
            clearTitleButton.setEnabled(false);
            clearTitleButton.setVisibility(View.INVISIBLE);
        } else {
            clearTitleButton.setEnabled(true);
            clearTitleButton.setVisibility(View.VISIBLE);
        }

        if (searchAuthorEditText.getText().length() == 0) {
            clearAuthorButton.setEnabled(false);
            clearAuthorButton.setVisibility(View.INVISIBLE);
        } else {
            clearAuthorButton.setEnabled(true);
            clearAuthorButton.setVisibility(View.VISIBLE);
        }


        // Set TextChanged Listeners
        searchTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // If there is text in the EditText, show the clear button, if not, hide it
                if (s.toString().trim().length() == 0) {
                    clearTitleButton.setEnabled(false);
                    clearTitleButton.setVisibility(View.INVISIBLE);
                } else {
                    clearTitleButton.setEnabled(true);
                    clearTitleButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchAuthorEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // If there is text in the EditText, show the clear button, if not, hide it
                if (s.toString().trim().length() == 0) {
                    clearAuthorButton.setEnabled(false);
                    clearAuthorButton.setVisibility(View.INVISIBLE);
                } else {
                    clearAuthorButton.setEnabled(true);
                    clearAuthorButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Populate Spinner
        spinner = (Spinner) findViewById(R.id.spinner);
        populateSpinner(spinner);

        // Set OnClickItemListener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                resultNumber = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                resultNumber = getString(R.string.spinner_default_value);
            }
        });

    }

    public void populateSpinner(Spinner spinner) {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array
                .spinner_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(spinnerAdapter);
    }

    // Implement onClick method
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clear_title_button:
                searchTitleEditText.setText("");
                break;
            case R.id.clear_author_button:
                searchAuthorEditText.setText("");
                break;
            case R.id.search_Button:
                // Save user's input
                userSearchInputTitle = searchTitleEditText.getText().toString();
                userSearchInputAuthor = searchAuthorEditText.getText().toString();
                // Start BookListActivity in order to show the query result
                Intent intent = new Intent(MainActivity.this, BookListActivity.class);
                startActivity(intent);
                break;
        }
    }
}
