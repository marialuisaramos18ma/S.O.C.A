package com.example.projetoauto.activity.empresa;

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
import com.example.projetoauto.api.CEPService;
import com.example.projetoauto.model.Endereco;
import com.santalu.maskara.widget.MaskEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class EmpresaFormEnderecoActivity extends AppCompatActivity {

    private MaskEditText edt_cep;
    private EditText edt_uf;
    private EditText edt_logradouro;
    private EditText edt_bairro;

    private Retrofit retrofit;

    private Button ib_salvar;
    private ProgressBar progressBar;
    private Endereco endereco;
    private boolean novoEndereco = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa_form_endereco);

        iniciarComponentes();

        iniciaRetrofit();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            endereco = (Endereco) bundle.getSerializable("enderecoSelecionado");
            configDados();
        }
        configCliques();
    }

    private void iniciaRetrofit() {
        retrofit = new Retrofit
                .Builder()
                .baseUrl("https://viacep.com.br/ws/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
        findViewById(R.id.ib_buscarCep).setOnClickListener(v -> buscarCEP());
    }

    private void buscarCEP() {

        String cep = edt_cep.getMasked().replaceAll("_", "").replace("-", "");

        if (cep.length() == 8) {

            ocultarTeclado();

            progressBar.setVisibility(View.VISIBLE);

            CEPService cepService = retrofit.create(CEPService.class);
            Call<Endereco> call = cepService.recuperaCEP(cep);

            call.enqueue(new Callback<Endereco>() {
                @Override
                public void onResponse(@NonNull Call<Endereco> call, @NonNull Response<Endereco> response) {

                    if (response.isSuccessful()) {
                        endereco = response.body();
                        if (endereco != null) {
                            if (endereco.getLogradouro() != null) {

                                configDados();

                            } else {
                                Toast.makeText(EmpresaFormEnderecoActivity.this, "Não foi possivel recuperar o endereço", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(EmpresaFormEnderecoActivity.this, "Não foi possivel recuperar o endereço", Toast.LENGTH_SHORT).show();
                        }

                        progressBar.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onFailure(@NonNull Call<Endereco> call, @NonNull Throwable t) {
                    Toast.makeText(EmpresaFormEnderecoActivity.this, "Não foi possivel recuperar o endereço", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });

        } else {
            Toast.makeText(this, "Formato Invalido", Toast.LENGTH_SHORT).show();
        }
    }

    private void configDados() {

        edt_cep.setText(endereco.getCep());
        edt_uf.setText(endereco.getUf());
        edt_logradouro.setText(endereco.getLogradouro());
        edt_bairro.setText(endereco.getBairro());

        novoEndereco = false;
    }

    public void validarDados(View view) {
        String cep = edt_cep.getMasked().trim();
        String uf = edt_uf.getText().toString().trim();
        String logradouro = edt_logradouro.getText().toString().trim();
        String bairro = edt_bairro.getText().toString().trim();

        if (!cep.isEmpty()) {
            if (!uf.isEmpty()) {
                if (!logradouro.isEmpty()) {
                    if (!bairro.isEmpty()) {

                        progressBar.setVisibility(View.VISIBLE);


                        if (endereco == null) endereco = new Endereco();
                        endereco.setCep(cep);
                        endereco.setUf(uf);
                        endereco.setLogradouro(logradouro);
                        endereco.setBairro(bairro);
                        endereco.salvar(getBaseContext(), progressBar);

                        if (novoEndereco) {
                            finish();
                        } else {
                            ocultarTeclado();
                        }


                    } else {
                        edt_bairro.requestFocus();
                        edt_bairro.setError("Preencha o Bairro");
                    }

                } else {
                    edt_logradouro.requestFocus();
                    edt_logradouro.setError("Preencha o Município");
                }

            } else {
                edt_uf.requestFocus();
                edt_uf.setError("Preencha sua UF");
            }

        } else {
            edt_cep.requestFocus();
            edt_cep.setError("Preencha seu CEP");
        }
    }

    private void ocultarTeclado() {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                ib_salvar.getWindowToken(), 0
        );
    }

    private void iniciarComponentes() {
        TextView text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Endereço");

        ib_salvar = findViewById(R.id.ib_salvar);
        edt_cep = findViewById(R.id.edt_cep);
        edt_uf = findViewById(R.id.edt_uf);
        edt_logradouro = findViewById(R.id.edt_logradouro);
        edt_bairro = findViewById(R.id.edt_bairro);
        progressBar = findViewById(R.id.progressBar);
    }
}