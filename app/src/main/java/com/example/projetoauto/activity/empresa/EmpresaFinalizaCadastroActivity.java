package com.example.projetoauto.activity.empresa;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.projetoauto.R;
import com.example.projetoauto.helper.FirebaseHelper;
import com.example.projetoauto.model.Empresa;
import com.example.projetoauto.model.Login;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.IOException;
import java.util.List;

public class EmpresaFinalizaCadastroActivity extends AppCompatActivity {

    /* Modificadores de acesso de Logo */
    private final int REQUEST_GALERIA = 100;
    private ImageView img_logo;
    private String caminhoLogo = "";

    private ProgressBar progressBar;

    private Empresa empresa;

    private Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa_finaliza_cadastro);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            empresa = (Empresa) bundle.getSerializable("empresa");
            login = (Login) bundle.getSerializable("login");
        }

        iniciaComponentes();

    }


    /* Metodos de Logo */

    public void selecionaLogo(View view) {
        verificaGaleria();
    }

    public void validaDados(View view) {

        if (!caminhoLogo.isEmpty()) {

            ocultarTeclado();

            progressBar.setVisibility(View.VISIBLE);

            salvarImagemFirebase();


        } else {
            progressBar.setVisibility(View.GONE);
            ocultarTeclado();
            erroAutenticacao("Selecione uma logo para seu cadastro");

        }
    }

    private void verificaGaleria() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                abrirGaleria();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(EmpresaFinalizaCadastroActivity.this, "Permissão Negada", Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.create().setPermissionListener(permissionListener).setDeniedTitle("Permissão Negadas").setDeniedMessage("Você negou as permissões para acessar a galeria do dispositivo, deseja permitir ?").setDeniedCloseButtonText("Não").setGotoSettingButtonText("Sim").setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE).check();

    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALERIA);
    }

    private void salvarImagemFirebase() {
        StorageReference storageReference = FirebaseHelper.getStorageReference().child("imagens").child("perfil").child(empresa.getId() + ".JPEG");

        UploadTask uploadTask = storageReference.putFile(Uri.parse(caminhoLogo));
        uploadTask.addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnCompleteListener(task -> {

            login.setAcesso(true);
            login.salvar();

            empresa.setImagemLogo(task.getResult().toString());
            empresa.salvar();

            finish();

            startActivity(new Intent(getBaseContext(), EmpresaHomeActivity.class));

        })).addOnFailureListener(e -> erroAutenticacao(e.getMessage()));
    }

    private void erroAutenticacao(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Atenção");
        builder.setMessage(msg);
        builder.setPositiveButton("OK", ((dialog, which) -> {
            dialog.dismiss();
        }));

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void iniciaComponentes() {
        img_logo = findViewById(R.id.img_logo);
        progressBar = findViewById(R.id.progressBar);
    }

    private void ocultarTeclado() {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(img_logo.getWindowToken(), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_GALERIA) {

                Bitmap bitmap;

                Uri imagemSelecionada = data.getData();
                caminhoLogo = data.getData().toString();

                if (Build.VERSION.SDK_INT < 33) {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagemSelecionada);
                        img_logo.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), imagemSelecionada);
                    try {
                        bitmap = ImageDecoder.decodeBitmap(source);
                        img_logo.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        }
    }

}