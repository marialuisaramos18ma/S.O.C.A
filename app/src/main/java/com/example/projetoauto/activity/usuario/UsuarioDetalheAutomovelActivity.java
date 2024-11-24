package com.example.projetoauto.activity.usuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projetoauto.R;
import com.example.projetoauto.adapter.SliderAdapter;
import com.example.projetoauto.auth.LoginActivity;
import com.example.projetoauto.helper.FirebaseHelper;
import com.example.projetoauto.helper.Mascara;
import com.example.projetoauto.model.Automovel;

import com.example.projetoauto.model.Empresa;
import com.example.projetoauto.model.Favorito;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;


import java.util.ArrayList;
import java.util.List;


public class UsuarioDetalheAutomovelActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txt_toolbar;
    private SliderView sliderView;
    private TextView txt_titulo_auto;
    private TextView txt_valor_venda;
    private TextView txt_data_publicado;
    private TextView txt_descricao;
    private TextView txt_modelo;
    private TextView txt_tipo;
    private TextView txt_ano;
    private TextView txt_quilometragem;
    private TextView txt_cep;
    private TextView txt_logradouro;
    private TextView txt_bairro;

    private LikeButton like_Button;

    private Automovel automovel;
    private Empresa empresa;

    private final List<String> favoritosList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_detalhe_automovel_activity);

        iniciaComponentes();


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            automovel = (Automovel) bundle.getSerializable("automovelSelecionado");

            configDados();

            recuperaEmpresa();

        }

        configButonLike();

        recuperaFavoritos();

        configCliques();
    }

    private void configButonLike() {
        like_Button.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                if (FirebaseHelper.getAutenticado()) {
                    configSnackBar("", "Automóvel salvo.", R.drawable.like_button_on, true);
                } else {
                    likeButton.setLiked(false);
                    alertAutenticacao("Você não esta autenticado.");
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                configSnackBar("DESFAZER", "Automóvel removido.", R.drawable.like_button_off, false);
            }
        });
    }

    private void configSnackBar(String actionMsg, String msg, int icon, Boolean like) {

        configFavoritos(like);

        Snackbar snackbar = Snackbar.make(like_Button, msg, Snackbar.LENGTH_SHORT);
        snackbar.setAction(actionMsg, v -> {
            if (!like) {
                configFavoritos(true);
            }

        });

        TextView snackbar_text = snackbar.getView().findViewById(R.id.snackbar_text);
        snackbar_text.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0);
        snackbar_text.setCompoundDrawablePadding(24);
        snackbar.setActionTextColor(Color.parseColor("#FFFFFFFF"))
                .setTextColor(Color.parseColor("#FFFFFF"))
                .show();
    }

    private void configFavoritos(Boolean like) {
        if (like) {
            like_Button.setLiked(true);

            favoritosList.add(automovel.getId());
        } else {
            like_Button.setLiked(false);
            favoritosList.remove(automovel.getId());
        }

        Favorito favorito = new Favorito();
        favorito.setFavoritos(favoritosList);
        favorito.salvar();
    }

    private void recuperaFavoritos() {
        if (FirebaseHelper.getAutenticado()) {
            DatabaseReference favoritosRef = FirebaseHelper.getDatabaseReference()
                    .child("favoritos")
                    .child(FirebaseHelper.getIdFirebase());
            favoritosRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        favoritosList.add(ds.getValue(String.class));
                    }

                    if (favoritosList.contains(automovel.getId())) {
                        like_Button.setLiked(true);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void alertAutenticacao(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Você não está autenticado.");
        builder.setMessage(msg);
        builder.setNegativeButton("Não", ((dialog, which) -> {
            dialog.dismiss();
        })).setPositiveButton("Sim", ((dialog, which) -> {
            startActivity(new Intent(this, LoginActivity.class));
        }));

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
        findViewById(R.id.ib_telefone).setOnClickListener(v -> whatsappTelefone());
        findViewById(R.id.ib_agenda).setOnClickListener(this);
    }

    private void whatsappTelefone() {
        if (FirebaseHelper.getAutenticado()) {
            if (whatsappNaoInstalado()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + empresa.getTelefone()));
                startActivity(intent);
            } else {
                Toast.makeText(this, "Whatsapp Não instalado", Toast.LENGTH_SHORT).show();
            }
        } else {
            alertAutenticacao("Você não esta atenticado");
        }
    }

    private boolean whatsappNaoInstalado() {
        PackageManager packageManager = getPackageManager();
        boolean whatsappInstalled;
        try {
            packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            whatsappInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            whatsappInstalled = false;
        }
        return whatsappInstalled;

    }

    private void recuperaEmpresa() {
        DatabaseReference empresaRef = FirebaseHelper.getDatabaseReference()
                .child("empresas")
                .child(automovel.getIdEmpresa());
        empresaRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                empresa = snapshot.getValue(Empresa.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configDados() {

        sliderView.setSliderAdapter(new SliderAdapter(automovel.getUrlImagens()));
        sliderView.startAutoCycle();
        sliderView.setScrollTimeInSec(4);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);

        txt_toolbar.setText("Detalhes");
        txt_titulo_auto.setText(automovel.getTitulo());
        txt_valor_venda.setText(getString(R.string.valor_automovel, Mascara.getValor(automovel.getValorDeVenda())));
        txt_data_publicado.setText(getString(R.string.data_publicacao, Mascara.getData(automovel.getDataPublicacao(), 3)));
        txt_descricao.setText(automovel.getDescricao());
        txt_modelo.setText("Modelo: " + automovel.getModelo());
        txt_tipo.setText("Tipo: " + automovel.getIdTipo());
        txt_ano.setText("Ano: " + automovel.getAno());
        txt_quilometragem.setText("KM: " + automovel.getQuilometragem());
        txt_cep.setText(automovel.getEndereco().getCep());
        txt_logradouro.setText(automovel.getEndereco().getLogradouro());
        txt_bairro.setText(automovel.getEndereco().getBairro());

    }

    private void iniciaComponentes() {
        txt_toolbar = findViewById(R.id.txt_toolbar);
        sliderView = findViewById(R.id.sliderView);
        txt_titulo_auto = findViewById(R.id.txt_titulo_auto);
        txt_valor_venda = findViewById(R.id.txt_valor_venda);
        txt_data_publicado = findViewById(R.id.txt_data_publicado);
        txt_descricao = findViewById(R.id.txt_descricao);
        txt_modelo = findViewById(R.id.txt_modelo);
        txt_tipo = findViewById(R.id.txt_tipo);
        txt_ano = findViewById(R.id.txt_ano);
        txt_quilometragem = findViewById(R.id.txt_quilometragem);
        txt_cep = findViewById(R.id.txt_cep);
        txt_logradouro = findViewById(R.id.txt_logradouro);
        txt_bairro = findViewById(R.id.txt_bairro);
        like_Button = findViewById(R.id.like_button);
    }

    @Override
    public void onClick(View view) {

        if (FirebaseHelper.getAutenticado()) {

            switch (view.getId()) {

                case R.id.ib_agenda:
                    Intent intent = new Intent(this, UsuarioCalendarioActivity.class);
                    startActivity(intent);

                    break;
            }

        } else {
            alertAutenticacao("Você não esta autenticado.");
        }

    }
}

