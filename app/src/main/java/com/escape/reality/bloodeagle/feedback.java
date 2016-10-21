package com.escape.reality.bloodeagle;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by JR HARI NANDHA on 10-08-2016.
 */
public class feedback extends Activity {
    private String SUBJ;
    private String BODY;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);

        Button mail=(Button)findViewById(R.id.mail);
        final EditText sub=(EditText)findViewById(R.id.sub);
        final EditText body=(EditText)findViewById(R.id.body);
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] TO = {"exitusrerum@gmail.com"};

                SUBJ = sub.getText().toString();
                BODY = body.getText().toString();
                Intent emailIntent = new Intent(Intent.ACTION_SEND);

                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);

                emailIntent.putExtra(Intent.EXTRA_SUBJECT, SUBJ);
                emailIntent.putExtra(Intent.EXTRA_TEXT, BODY);

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    finish();

                }
                catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
