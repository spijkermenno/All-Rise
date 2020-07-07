package nl.topicus.all_rise.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import nl.topicus.all_rise.R;

public class ZenmodeActivity extends AppCompatActivity {

    private int zenval;
    private Button btn1hourzen, btn2hourzen, btn3hourzen, btn4hourzen, btncancelzen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zenmode);
        Intent intent2 = getIntent();
        zenval = intent2.getIntExtra("zenval", 0);

        btn1hourzen = findViewById(R.id.btn_1hourzen);
        btn1hourzen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zenval = 1;
                Intent intent = new Intent();
                intent.putExtra("Zenvalue", zenval);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        btn2hourzen = findViewById(R.id.btn_2hourzen);
        btn2hourzen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zenval = 2;
                Intent intent = new Intent();
                intent.putExtra("Zenvalue", zenval);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        btn3hourzen = findViewById(R.id.btn_3hourzen);
        btn3hourzen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zenval = 3;
                Intent intent = new Intent();
                intent.putExtra("Zenvalue", zenval);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        btn4hourzen = findViewById(R.id.btn_4hourzen);
        btn4hourzen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zenval = 4;
                Intent intent = new Intent();
                intent.putExtra("Zenvalue", zenval);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        btncancelzen = findViewById(R.id.btn_cancelzen);
        btncancelzen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zenval = 5;
                Intent intent = new Intent();
                intent.putExtra("Zenvalue", zenval);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}