package com.supergente.loja;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.view.KeyEvent;
import java.io.File;

public class MainActivity extends Activity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webview);
        setupWebView();
        
     // Isso diz ao app para abrir o arquivo da pasta /SuperGente na memória do celular
File localFile = new File("/sdcard/SuperGente/INDEX.html");

/ NOVO CÓDIGO PARA LER DO USB:
        File localFile = new File("/sdcard/SuperGente/INDEX.html");

        if (localFile.exists()) {
            webView.loadUrl("file://" + localFile.getAbsolutePath());
        } else {
            // Caso o funcionário abra o app sem ter copiado a pasta via USB
            String aviso = "<html><body style='background-color:black;color:white;text-align:center;padding-top:50px;font-family:sans-serif;'>"
                         + "<h1>Atenção!</h1>"
                         + "<p>Pasta <b>/SuperGente</b> não encontrada na memória interna.</p>"
                         + "<p>Conecte o celular ao computador e copie os arquivos de treinamento via USB.</p>"
                         + "</body></html>";
            webView.loadData(aviso, "text/html", "UTF-8");
        }

if (localFile.exists()) {
    webView.loadUrl("file://" + localFile.getAbsolutePath());
} else {
    // Caso você instale o app mas ainda não tenha copiado a pasta via USB
    String aviso = "<html><body style='background:black;color:white;text-align:center;padding-top:50px;'>"
                 + "<h2>Pasta /SuperGente não encontrada!</h2>"
                 + "<p>Conecte ao PC e copie a pasta com os vídeos para a memória interna.</p>"
                 + "</body></html>";
    webView.loadData(aviso, "text/html", "UTF-8");
}

    private void setupWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebChromeClient(new WebChromeClient());
        
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // Se for um arquivo local (HTML, PDF, MP4)
                if (url.startsWith("file://")) {
                    // Verificar extensão do arquivo
                    if (url.endsWith(".pdf")) {
                        // Abrir PDF com app nativa
                        openPDF(url);
                        return true;
                    } else if (url.endsWith(".mp4")) {
                        // Abrir vídeo com app nativa
                        openVideo(url);
                        return true;
                    }
                    // Se for HTML, deixar o WebView carregar
                    return false;
                }
                
                // URLs externas
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                
                return false;
            }
        });
    }

    private void openPDF(String url) {
        try {
            // Converter file:// URL para caminho real
            String filePath = url.replace("file://", "");
            File file = new File(filePath);
            
            if (file.exists()) {
                Uri uri = Uri.fromFile(file);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openVideo(String url) {
        try {
            // Converter file:// URL para caminho real
            String filePath = url.replace("file://", "");
            File file = new File(filePath);
            
            if (file.exists()) {
                Uri uri = Uri.fromFile(file);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "video/mp4");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
