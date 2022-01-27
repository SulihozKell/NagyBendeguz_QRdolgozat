package hu.petrik.qrdolgozat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    Button buttonScan, buttonKiir;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        buttonScan.setOnClickListener(view -> {
            IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
            intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
            intentIntegrator.setPrompt("QR kód olvasás");
            intentIntegrator.setCameraId(0);
            intentIntegrator.setBeepEnabled(false);
            intentIntegrator.initiateScan();
        });

        buttonKiir.setOnClickListener(view -> {
            if (text != null) {
                try {
                    Naplozas.kiir(text);
                }
                catch (IOException e) {
                    Log.d("Kiírási hiba: ", e.getMessage());
                }
            }
            else {
                Toast.makeText(this, "Nincs beszkennelve semmi!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(this, "Kiléptél a scannerből!", Toast.LENGTH_SHORT).show();
            }
            else {
                textView.setText(intentResult.getContents());
                text = intentResult.getContents();
                try {
                    Uri url = Uri.parse(intentResult.getContents());
                    Intent intent = new Intent(Intent.ACTION_VIEW, url);
                    startActivity(intent);
                }
                catch (Exception e) {
                    Log.d("URL ERROR", e.toString());
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void init() {
        textView = findViewById(R.id.text_view);
        buttonScan = findViewById(R.id.button_scan);
        buttonKiir = findViewById(R.id.button_kiir);
    }
}