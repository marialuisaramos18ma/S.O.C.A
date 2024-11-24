package com.example.projetoauto.activity.empresa;

import androidx.annotation.NonNull;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projetoauto.R;
import com.example.projetoauto.helper.FirebaseHelper;
import com.example.projetoauto.model.Empresa;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.santalu.maskara.widget.MaskEditText;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

public class EmpresaPerfilActivity extends AppCompatActivity {

    private final int REQUEST_GALERIA = 100;

    private ImageView img_logo;

    private EditText edt_nome;

    private EditText edt_email;

    private Button ib_salvar;

    private MaskEditText edt_telefone;

    private String caminhoLogo;

    private ProgressBar progressBar;

    private Empresa empresa;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa_perfil);

        iniciaComponentes();

        recuperaEmpresa();

        configCliques();

    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
        ib_salvar.setOnClickListener(v -> validaDados());
    }


    private void recuperaEmpresa() {
        DatabaseReference empresaRef = FirebaseHelper.getDatabaseReference()
                .child("empresas")
                .child(FirebaseHelper.getIdFirebase());

        empresaRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                empresa = snapshot.getValue(Empresa.class);

                configDados();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configDados() {
        Picasso.get().load(empresa.getImagemLogo()).into(img_logo);
        edt_nome.setText(empresa.getNome());
        edt_telefone.setText(empresa.getTelefone());
        edt_email.setText(empresa.getEmail());

        configSalvar(false);

    }

    private void configSalvar(boolean progress) {
        if (progress) {
            progressBar.setVisibility(View.VISIBLE);
            ib_salvar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            ib_salvar.setVisibility(View.VISIBLE);
        }
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALERIA);
    }

    private void salvarImagemFirebase() {
        StorageReference storageReference = FirebaseHelper.getStorageReference().child("imagens").child("perfil").child(empresa.getId() + ".JPEG");

        UploadTask uploadTask = storageReference.putFile(Uri.parse(caminhoLogo));
        uploadTask.addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnCompleteListener(task -> {

            empresa.setImagemLogo(task.getResult().toString());
            empresa.salvar();

            configSalvar(false);

            Toast.makeText(this, "Dados salvos com sucesso", Toast.LENGTH_SHORT).show();

        })).addOnFailureListener(e -> {
            configSalvar(false);
            erroSalvarDados(e.getMessage());
        });
    }

    private void erroSalvarDados(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Atenção");
        builder.setMessage(msg);
        builder.setPositiveButton("OK", ((dialog, which) -> {
            dialog.dismiss();
        }));

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void verificaGaleria() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                abrirGaleria();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getBaseContext(), "Permissão Negada", Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.create().setPermissionListener(permissionListener).setDeniedTitle("Permissão Negadas").setDeniedMessage("Você negou as permissões para acessar a galeria do dispositivo, deseja permitir ?").setDeniedCloseButtonText("Não").setGotoSettingButtonText("Sim").setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE).check();

    }

    public void selecionaLogo(View view) {
        verificaGaleria();
    }

    private void validaDados() {
        String nome = edt_nome.getText().toString().trim();
        String telefone = edt_telefone.getUnMasked();


        if (!nome.isEmpty()) {
            if (edt_telefone.isDone()) {

                ocultarTeclado();

                configSalvar(true);

                empresa.setNome(nome);
                empresa.setTelefone(telefone);

                if (caminhoLogo != null) {
                    salvarImagemFirebase();
                } else {
                    empresa.salvar();
                    configSalvar(false);
                    Toast.makeText(this, "Dados salvos com sucesso", Toast.LENGTH_SHORT).show();
                }

            } else {
                edt_telefone.requestFocus();
                edt_telefone.setError("Informe um telefone para contato");
            }

        } else {
            edt_nome.requestFocus();
            edt_nome.setError("Informe um nome nome para sua empresa");
        }


    }

    private void iniciaComponentes() {
        TextView text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Dados da empresa");

        edt_nome = findViewById(R.id.edt_nome);
        edt_email = findViewById(R.id.edt_email);
        edt_telefone = findViewById(R.id.edt_telefone);
        img_logo = findViewById(R.id.img_logo);
        progressBar = findViewById(R.id.progressBar);
        ib_salvar = findViewById(R.id.ib_salvar);
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

    private void ocultarTeclado() {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(img_logo.getWindowToken(), 0);
    }


}