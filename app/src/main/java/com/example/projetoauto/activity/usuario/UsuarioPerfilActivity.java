package com.example.projetoauto.activity.usuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projetoauto.R;
import com.example.projetoauto.helper.FirebaseHelper;
import com.example.projetoauto.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.santalu.maskara.widget.MaskEditText;

public class UsuarioPerfilActivity extends AppCompatActivity {

    private EditText edt_nome;

    private EditText edt_email;

    private Button ib_salvar;

    private MaskEditText edt_telefone;


    private ProgressBar progressBar;

    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_perfil);

        iniciaComponentes();

        configCliques();

        recuperaUsuario();
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
        ib_salvar.setOnClickListener(v -> validaDados());
    }

    private void recuperaUsuario() {
        DatabaseReference usuarioRef = FirebaseHelper.getDatabaseReference()
                .child("usuarios")
                .child(FirebaseHelper.getIdFirebase());

        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usuario = snapshot.getValue(Usuario.class);

                configDados();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configDados() {

        edt_nome.setText(usuario.getNome());
        edt_telefone.setText(usuario.getTelefone());
        edt_email.setText(usuario.getEmail());

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

    private void validaDados() {
        String nome = edt_nome.getText().toString().trim();
        String telefone = edt_telefone.getUnMasked();


        if (!nome.isEmpty()) {
            if (edt_telefone.isDone()) {

                ocultarTeclado();

                configSalvar(true);

                usuario.setNome(nome);
                usuario.setTelefone(telefone);

                if (edt_nome != null) {
                    usuario.salvar();
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
        text_toolbar.setText("Perfil");

        edt_nome = findViewById(R.id.edt_nome);
        edt_email = findViewById(R.id.edt_email);
        edt_telefone = findViewById(R.id.edt_telefone);
        progressBar = findViewById(R.id.progressBar);
        ib_salvar = findViewById(R.id.ib_salvar);
    }

    private void ocultarTeclado() {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(edt_email.getWindowToken(), 0);
    }

}