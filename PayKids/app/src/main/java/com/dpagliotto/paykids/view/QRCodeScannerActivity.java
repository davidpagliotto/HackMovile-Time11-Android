package com.dpagliotto.paykids.view;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ErrorCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.dpagliotto.paykids.R;
import com.dpagliotto.paykids.db.dao.BaseDAO;
import com.dpagliotto.paykids.model.Dependente;
import com.dpagliotto.paykids.support.HTTPHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.Result;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import cz.msebera.android.httpclient.Header;

public class QRCodeScannerActivity extends AppCompatActivity {

    private Dependente dependente;
    private CodeScanner codeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scanner);

        CodeScannerView scannerView = findViewById(R.id.scanner_view);

        Toolbar toolbar = findViewById(R.id.fragmentContainerToolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().getParcelable("DEP") != null) {
                dependente = getIntent().getExtras().getParcelable("DEP");
            }
        }

        codeScanner = new CodeScanner(this, scannerView);
        codeScanner.setCamera(CodeScanner.CAMERA_BACK);
        codeScanner.setFormats(CodeScanner.ALL_FORMATS);
        codeScanner.setAutoFocusMode(AutoFocusMode.SAFE);
        codeScanner.setScanMode(ScanMode.SINGLE);
        codeScanner.setAutoFocusEnabled(true);
        codeScanner.setFlashEnabled(false);

        // Callbacks
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                new Handler(getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (dependente != null) {
                            new RequestAsyncTask().execute(result.getText());
                        }
                    }
                });
            }
        });
        codeScanner.setErrorCallback(new ErrorCallback() {
            @Override
            public void onError(@NonNull Exception error) {
                new Handler(getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(QRCodeScannerActivity.this, "Camera initialization error: ${it.message}",  Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }

    private class RequestAsyncTask extends AsyncTask<Object, Void, Void> {

        private AsyncHttpResponseHandler response = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String str = new String(responseBody);
                    Log.v("QRScanner", str);
                    if (str.trim().length() > 0) {
                        tratarRetornoPessoa(str);
                    }
                } catch (Exception e) {
                    Toast.makeText(QRCodeScannerActivity.this, "Erro ao realizar pagamento", Toast.LENGTH_SHORT).show();
                    QRCodeScannerActivity.this.finish();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, final byte[] responseBody, Throwable error) {
                new Handler(QRCodeScannerActivity.this.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        String str = new String(responseBody == null ? new byte[0] : responseBody);
                        if (str.trim().length() > 0) {
                            try {
                                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                                HashMap map = gson.fromJson(str, HashMap.class);

                                if (map.get("mensagem") != null)
                                    Toast.makeText(QRCodeScannerActivity.this, map.get("message").toString(), Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(QRCodeScannerActivity.this, "Erro ao realizar pagamento", Toast.LENGTH_SHORT).show();
                            }
                            catch (Exception e) {
                                Toast.makeText(QRCodeScannerActivity.this, "Erro ao realizar pagamento", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                            Toast.makeText(QRCodeScannerActivity.this, "Erro ao realizar pagamento", Toast.LENGTH_SHORT).show();
                    }
                });

                new Handler(QRCodeScannerActivity.this.getMainLooper()).postAtTime(new Runnable() {
                    @Override
                    public void run() {
                        QRCodeScannerActivity.this.finish();
                    }
                }, 2000);
            }
        };

        @Override
        protected Void doInBackground(Object... objects) {
            if (objects != null && objects.length > 0) {
                if (objects[0] instanceof  String) {
                    String url = (String) objects[0];
                    SyncHttpClient httpClient = new SyncHttpClient(false, 80, 443);
                    httpClient.get(QRCodeScannerActivity.this, url, null, "application/json", response);
                }
            }
            return null;
        }

        private void tratarRetornoPessoa(String str) {
            if (str.trim().length() > 0) {
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                HashMap map = gson.fromJson(str, HashMap.class);

                if (map.get("id") != null) {
                    dependente.setSaldo(map.get("saldo") == null ? 0.0 : (Double) map.get("saldo"));
                    Log.v("QRScanner", dependente.getSaldo().toString());
                    BaseDAO daoDependente = new BaseDAO(QRCodeScannerActivity.this, Dependente.class);
                    daoDependente.createOrUpdate(dependente);

                    QRCodeScannerActivity.this.finish();
                }
            }
        }

    }
}
