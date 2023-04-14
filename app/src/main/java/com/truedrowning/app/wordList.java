package com.truedrowning.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amplifyframework.core.Amplify;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class wordList extends AppCompatActivity {

    public String listFile = "wordListFile.txt";

    //public TextView resultTextView;
    private TextView searchList;
    private EditText searchEditText;
    private StringBuilder wordsBuilder = new StringBuilder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);

        listMaker();
    }

    private void uploadFile2() {
        File uploadFile = new File(getApplicationContext().getFilesDir(), "wordListFile.txt");

        Amplify.Storage.uploadFile(
                "searchList.txt",
                uploadFile,
                result -> Log.i("MyAmplifyApp", "Successfully uploaded: " + result.getKey()),
                storageFailure -> Log.e("MyAmplifyApp", "Upload failed", storageFailure)
        );
    }

    private void textRef(){

        File file = new File(getApplicationContext().getFilesDir(), "wordListFile.txt");

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }

            bufferedReader.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void searchTerms(View view){
        listMaker();
        textRef();
        uploadFile2();
    }
    public void eraseList(View view){

        searchList = findViewById(R.id.result_text_view);
        String[] emptyArray = new String[0];
        FileOutputStream outputStream;

        try {
            // Delete the existing file
            File file = new File(getFilesDir(), listFile);
            file.delete();

            // Recreate the file with an empty string array
            outputStream = openFileOutput(listFile, Context.MODE_PRIVATE);
            for (String s : emptyArray) {
                outputStream.write(s.getBytes());
            }
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Clear the contents of the wordsTextView
        searchList.setText("none");
    }

    private void listMaker(){
        searchList = findViewById(R.id.result_text_view);
        searchEditText = findViewById(R.id.search_edit_text);
        wordsBuilder = new StringBuilder();
        wordsBuilder.append("Words to search:\n");
        //wordsText = wordsBuilder.toString();

        String searchInput = searchEditText.getText().toString().trim();
        if (!searchInput.isEmpty()) {
            String[] wordsToSearch = searchInput.split("\\s+");
            Set<String> existingWords = new HashSet<>();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(openFileInput("wordListFile.txt")))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    existingWords.add(line.trim());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try (FileOutputStream outputStream = openFileOutput("wordListFile.txt", Context.MODE_APPEND)) {
                for (String word : wordsToSearch) {
                    String trimmedWord = word.trim();
                    if (!existingWords.contains(trimmedWord)) {
                        outputStream.write(trimmedWord.getBytes());
                        outputStream.write("\n".getBytes());
                        existingWords.add(trimmedWord);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        try {
            FileInputStream inputStream = openFileInput("wordListFile.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                wordsBuilder.append(line).append("\n"); // add each line to the StringBuilder with a newline
            }
            reader.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        searchList.setText(wordsBuilder.toString());
    }

    public StringBuilder getWordsBuilder() {
        return wordsBuilder;
    }


}