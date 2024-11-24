package com.example.projetoauto.fragment.usuario;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.projetoauto.R;
import com.example.projetoauto.helper.FirebaseHelper;
import com.example.projetoauto.model.Login;
import com.example.projetoauto.model.Usuario;
import com.example.projetoauto.activity.usuario.UsuarioHomeActivity;
import com.santalu.maskara.widget.MaskEditText;

public class UsuarioFragment extends Fragment {
    private EditText edt_nome;
    private EditText edt_email;
    private MaskEditText edt_telefone;
    private EditText edt_senha;
    private ProgressBar progressBar;
    private Button btn_criar_conta;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_usuario, container, false);

        iniciaComponentes(view);

        configCliques();

        return view;
    }

    private void configCliques() {
        btn_criar_conta.setOnClickListener(v -> validaDados());
    }

    private void validaDados() {
        String nome = edt_nome.getText().toString();
        String email = edt_email.getText().toString();
        String telefone = edt_telefone.getUnMasked();
        String senha = edt_senha.getText().toString();

        if (!nome.isEmpty()) {
            if (!email.isEmpty()) {
                if (edt_telefone.isDone()) {
                    if (!senha.isEmpty()) {

                        ocultarTeclado();

                        progressBar.setVisibility(View.VISIBLE);

                        Usuario usuario = new Usuario();
                        usuario.setNome(nome);
                        usuario.setEmail(email);
                        usuario.setTelefone(telefone);
                        usuario.setSenha(senha);

                        criarConta(usuario);

                    } else {
                        edt_senha.requestFocus();
                        edt_senha.setError("Preencha sua senha");
                    }

                } else {
                    edt_telefone.requestFocus();
                    edt_telefone.setError("Preencha seu telefone");
                }

            } else {
                edt_email.requestFocus();
                edt_email.setError("Preencha seu Email");
            }

        } else {
            edt_nome.requestFocus();
            edt_nome.setError("Preencha seu nome");
        }
    }

    private void criarConta(Usuario usuario) {
        FirebaseHelper.getAuth().createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                String id = task.getResult().getUser().getUid();

                usuario.setId(id);
                usuario.salvar();

                Login login = new Login(id, "U", true);
                login.salvar();

                //Levar para Tela Home
                requireActivity().finish();
                Intent intent = new Intent(requireActivity(), UsuarioHomeActivity.class);
                intent.putExtra("login", login);
                intent.putExtra("usuario", usuario);
                startActivity(intent);

            } else {
                //Validação de erro
                progressBar.setVisibility(View.GONE);
                erroAutenticacao(FirebaseHelper.validarErros(task.getException().getMessage()));
            }
        });
    }

    private void erroAutenticacao(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Atenção");
        builder.setMessage(msg);
        builder.setPositiveButton("OK", ((dialog, which) -> {
            dialog.dismiss();
        }));

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void iniciaComponentes(View view) {
        edt_nome = view.findViewById(R.id.edt_nome);
        edt_email = view.findViewById(R.id.edt_email);
        edt_telefone = view.findViewById(R.id.edt_telefone);
        edt_senha = view.findViewById(R.id.edt_senha);
        progressBar = view.findViewById(R.id.progressBar);
        btn_criar_conta = view.findViewById(R.id.btn_criar_conta);
    }

    private void ocultarTeclado() {
        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                btn_criar_conta.getWindowToken(), 0
        );
    }
}