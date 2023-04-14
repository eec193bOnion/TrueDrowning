package com.truedrowning.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.core.Amplify;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class statusPage extends AppCompatActivity {

    private TextView myTextView;
    private String textFile = "download.txt";

    public String getTextFile() {
        return textFile;
    }
    public TextView getTextView() {
        return myTextView;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_page);
        //myTextView = findViewById(R.id.my_Text_View);
        ImageView imageView = findViewById(R.id.statusImage);
        String filePathImg = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/test.jpg";
        saving maskUp = new saving();
        maskUp.importImage(this, filePathImg, imageView);

    }

    @Override
    protected void onResume() {
        super.onResume();

        downloadFileText();
        maskSetUp download = new maskSetUp();
        download.downloadFilePImage();

        myTextView = findViewById(R.id.my_Text_View);
        File file = new File(getApplicationContext().getFilesDir(), textFile);
        String filePath = file.getAbsolutePath();
        searchFileForWords(filePath, myTextView);

    }



    public void listPg(View view){
        Intent intent = new Intent(statusPage.this, wordList.class);
        startActivity(intent);
    }

    public void searching(View view){
        myTextView = findViewById(R.id.my_Text_View);
        //String[] wordsToSearch = {"bow", "man", "person"}; //all labels should be lowercase
        //String filePath = getApplicationContext().getFilesDir() + "/download.txt";

        File file = new File(getApplicationContext().getFilesDir(), textFile);
        String filePath = file.getAbsolutePath();
        searchFileForWords(filePath, myTextView);

         /*
        maskSetUp getTR = new maskSetUp();
        File file = new File(getApplicationContext().getFilesDir(), getTR.textFile);

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

          */
    }

    public void downloadFileText(){
        File file = new File(getApplicationContext().getFilesDir(), textFile);

        Amplify.Storage.downloadFile(
                "labels_file.txt",
                file,
                result -> Log.i("MyAmplifyApp", "Successfully downloaded: " + result.getFile().getName()),
                error -> Log.e("MyAmplifyApp",  "Download Failure", error)
        );
    }


    public void searchFileForWords(String filePath, TextView myTextView) { // in middle spot for the edit text it was EditText searchEditText

        wordList getList = new wordList();
        StringBuilder wordsBuilder = getList.getWordsBuilder();

        try {
            FileInputStream inputStream = openFileInput(getList.listFile);
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

        File file = new File(filePath);
        boolean found = false;
        try (FileInputStream fileInputStream = new FileInputStream(file);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream))) {
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                for (String word : wordsBuilder.toString().split("\\s+")) { //searchTerms.listFile.toString().split("\\s+"),  wordsBuilder.toString().split("\\s+")
                    if (line.toLowerCase().contains(word)) {
                        System.out.println(line);
                        if (!sb.toString().toLowerCase().contains(word)) { // check if word has already been found in line
                            sb.append(line).append("\n");
                            found = true;
                        }

                        break; // only print the line once if it contains multiple words
                    }
                    //else{
                    //  System.out.println("Not Detected");
                    //}
                }
            }

            if (!found) {
                myTextView.setText("Nothing Found"); // set the "nothing found" message if no word was found
            } else {
                myTextView.setText(""); // clear the previous text
                myTextView.setText(sb.toString().trim()); // set the new text
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}