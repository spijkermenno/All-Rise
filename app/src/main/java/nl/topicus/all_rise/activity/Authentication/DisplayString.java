package nl.topicus.all_rise.activity.Authentication;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import nl.topicus.all_rise.R;

public class DisplayString extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_show_string);

        final String string = getIntent().getExtras().getString("string");

        TextView displayField = findViewById(R.id.DisplayString);
        final TextView supporttext = findViewById(R.id.supporttext);

        displayField.setTextSize(25f);
        displayField.setText(string);
        displayField.setTooltipText("Coppied!");

        displayField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(string, string);
                clipboard.setPrimaryClip(clip);
                supporttext.setText(R.string.coppied);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
