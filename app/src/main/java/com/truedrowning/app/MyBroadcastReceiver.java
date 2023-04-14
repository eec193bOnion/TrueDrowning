package com.truedrowning.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import java.io.File;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        /*
        statusPage fileSearcher = new statusPage();
        File file = new File(fileSearcher.getApplicationContext().getFilesDir(), fileSearcher.getTextFile());
        String filePath = file.getAbsolutePath();
        TextView myTextView = fileSearcher.getTextView();
        fileSearcher.searchFileForWords(filePath, myTextView);

         */

    }

    private void autoRun(){
        System.out.print("my name jeff");
    }

}
