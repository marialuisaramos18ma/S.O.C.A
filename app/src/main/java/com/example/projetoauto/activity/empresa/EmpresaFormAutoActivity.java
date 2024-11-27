package com.example.projetoauto.activity.empresa;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.example.projetoauto.R;
import com.example.projetoauto.activity.TipoAutomovelActivity;
import com.example.projetoauto.helper.FirebaseHelper;
import com.example.projetoauto.helper.Mascara;
import com.example.projetoauto.model.Automovel;
import com.example.projetoauto.model.Endereco;
import com.example.projetoauto.model.Imagem;
import com.example.projetoauto.model.Tipo;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EmpresaFormAutoActivity extends AppCompatActivity {

    private Endereco endereco;

    private Automovel automovel;

    private ImageView img0;
    private ImageView img1;
    private ImageView img2;

    private String currentPhotoPath;

    private List<Imagem> imagemList = new ArrayList<>();

    private EditText edt_titulo;
    private EditText edt_descricao;
    private Button btn_tipo;
    private EditText edt_placa;
    private EditText edt_modelo;
    private MaskEditText edt_ano;

    private Button btn_salvar;

    private EditText edt_quilometragem;

    private EditText edt_valor_de_venda;
    private EditText edt_valor_comprado;


    private Button btn_endereco;
    private TextView txt_endereco;

    private TextView text_toolbar;

    private final int REQUEST_TIPO = 100;
    private final int REQUEST_ENDERECO = 200;

    private String tipoSelecionado = "";
    private String enderecoSelecionado = "";


    private boolean novoAutomovel = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa_form_auto);

        iniciaComponentes();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            automovel = (Automovel) bundle.getSerializable("automovelSelecionado");
            configDados();
        }

        configCliques();

        recuperaEndereco();
    }

    private void configDados() {
        text_toolbar.setText("Editando anúncio");

        tipoSelecionado = automovel.getIdTipo();
        btn_tipo.setText(tipoSelecionado);

        edt_titulo.setText(automovel.getTitulo());
        edt_descricao.setText(automovel.getDescricao());
        //btn_tipo.setText(automovel.getIdTipo());
        edt_placa.setText(automovel.getPlaca());
        edt_modelo.setText(automovel.getModelo());
        edt_ano.setText(automovel.getAno());
        edt_quilometragem.setText(automovel.getQuilometragem());
        edt_valor_comprado.setText(Mascara.getValor(automovel.getValorDeVenda()));
        edt_valor_de_venda.setText(Mascara.getValor(automovel.getValorDeVenda()));

        //Arrumar recuperação de endereço
        btn_endereco.setText(automovel.getEndereco().getLogradouro());

        Picasso.get().load(automovel.getUrlImagens().get(0).getCaminhoimagem()).into(img0);
        Picasso.get().load(automovel.getUrlImagens().get(1).getCaminhoimagem()).into(img1);
        Picasso.get().load(automovel.getUrlImagens().get(2).getCaminhoimagem()).into(img2);

        novoAutomovel = false;
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());

        img0.setOnClickListener(v -> showBottomDialog(0));
        img1.setOnClickListener(v -> showBottomDialog(1));
        img2.setOnClickListener(v -> showBottomDialog(2));
    }

    public void validaDados(View view) {

        String titulo = edt_titulo.getText().toString().trim();
        String descricao = edt_descricao.getText().toString().trim();
        String placa = edt_placa.getText().toString().trim();
        String modelo = edt_modelo.getText().toString().trim();
        String anoStr = edt_ano.getText().toString().trim();
        String quilometragem = edt_quilometragem.getText().toString().trim();

        String valorDeVendaStr = edt_valor_de_venda.getText().toString().trim().replace(".", "").replace(",", ".");
        String valorCompradoStr = edt_valor_comprado.getText().toString().trim().replace(".", "").replace(",", ".");

        double valorDeVenda = valorDeVendaStr.isEmpty() ? 0 : Double.parseDouble(valorDeVendaStr);
        double valorComprado = valorCompradoStr.isEmpty() ? 0 : Double.parseDouble(valorCompradoStr);

        // Validação para o ano de fabricação
        int ano = 0;
        try {
            ano = Integer.parseInt(anoStr);
        } catch (NumberFormatException e) {
            edt_ano.setError("Informe um Ano válido");
            edt_ano.requestFocus();
            return;
        }

        // Verifique se o ano está dentro de um intervalo aceitável
        if (ano < 1900 || ano > Calendar.getInstance().get(Calendar.YEAR)) {
            edt_ano.setError("Informe um Ano válido");
            edt_ano.requestFocus();
            return;
        }

        if (!titulo.isEmpty()) {
            if (!descricao.isEmpty()) {
                if (!tipoSelecionado.isEmpty()) {
                    if (!placa.isEmpty()) {
                        if (!modelo.isEmpty()) {
                            if (anoStr.length() == 4) {
                                if (!quilometragem.isEmpty()) {
                                    if (valorDeVenda > 0) {
                                        if (valorComprado > 0) {
                                            if (!enderecoSelecionado.isEmpty()) {
                                                if (automovel == null) automovel = new Automovel();
                                                automovel.setIdEmpresa(FirebaseHelper.getIdFirebase());
                                                automovel.setTitulo(titulo);
                                                automovel.setDescricao(descricao);
                                                automovel.setPlaca(placa);
                                                automovel.setModelo(modelo);
                                                automovel.setAno(anoStr);
                                                automovel.setQuilometragem(quilometragem);
                                                automovel.setValorDeVenda(valorDeVenda);
                                                automovel.setValorComprado(valorComprado);
                                                automovel.setIdTipo(tipoSelecionado);
                                                automovel.setIdEndereco(enderecoSelecionado);
                                                automovel.setEndereco(endereco);

                                                if (novoAutomovel) {
                                                    if (imagemList.size() == 3) {
                                                        for (int i = 0; i < imagemList.size(); i++) {
                                                            salvarImagemFirebase(imagemList.get(i), i);
                                                        }
                                                    } else {
                                                        Toast.makeText(this, "Selecione 3 Imagens", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    if (imagemList.size() > 0) {
                                                        for (int i = 0; i < imagemList.size(); i++) {
                                                            salvarImagemFirebase(imagemList.get(i), i);
                                                        }
                                                    } else {
                                                        btn_salvar.setText("Aguarde...");
                                                        automovel.salvar(this, false);
                                                    }
                                                }
                                            } else {
                                                btn_endereco.requestFocus();
                                                ocultarTeclado();
                                                erroSalvarEnderecoAutomovel();
                                            }
                                        } else {
                                            edt_valor_comprado.requestFocus();
                                            edt_valor_comprado.setError("Informe um Valor que foi Comprado");
                                        }
                                    } else {
                                        edt_valor_de_venda.requestFocus();
                                        edt_valor_de_venda.setError("Informe um Valor Para Venda");
                                    }
                                } else {
                                    edt_quilometragem.requestFocus();
                                    edt_quilometragem.setError("Informe a Quilometragem");
                                }
                            } else {
                                edt_ano.requestFocus();
                                edt_ano.setError("Informe o Ano");
                            }
                        } else {
                            edt_modelo.requestFocus();
                            edt_modelo.setError("Informe o Modelo");
                        }
                    } else {
                        edt_placa.requestFocus();
                        edt_placa.setError("Informe o número de motores");
                    }
                } else {
                    btn_tipo.requestFocus();
                    ocultarTeclado();
                    erroSalvarTipoAutomovel();
                }
            } else {
                edt_descricao.requestFocus();
                edt_descricao.setError("Informe uma Descrição");
            }
        } else {
            edt_titulo.requestFocus();
            edt_titulo.setError("Informe um Titulo");
        }
    }




    private void salvarImagemFirebase(Imagem imagem, int index) {

        btn_salvar.setText("Aguarde...");

        StorageReference storageReference = FirebaseHelper.getStorageReference()
                .child("imagens")
                .child("automoveis")
                .child(automovel.getId())
                .child("imagem" + index + ".jpeg");

        UploadTask uploadTask = storageReference.putFile(Uri.parse(imagem.getCaminhoimagem()));
        uploadTask.addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnCompleteListener(task -> {

            imagem.setCaminhoimagem(task.getResult().toString());

            if (novoAutomovel) {
                automovel.getUrlImagens().add(imagem);
            } else {
                automovel.getUrlImagens().set(index, imagem);
            }
            if (imagemList.size() == index + 1) {
                automovel.salvar(this, novoAutomovel);
            }

        })).addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
        //Salva as imagens como imagem0.jpeg......
    }


    private void erroSalvarTipoAutomovel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Atenção");
        builder.setMessage("Selecione o Tipo do Automovel");
        builder.setPositiveButton("OK", ((dialog, which) -> {
            dialog.dismiss();
        }));

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void erroSalvarEnderecoAutomovel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Atenção");
        builder.setMessage("Selecione o Endereço do Automovel");
        builder.setPositiveButton("OK", ((dialog, which) -> {
            dialog.dismiss();
        }));

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    //Validação das imagens
    public void showBottomDialog(int requestCode) {
        View modalbottomsheet = getLayoutInflater().inflate(R.layout.layout_bottom_sheet, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialog);
        bottomSheetDialog.setContentView(modalbottomsheet);
        bottomSheetDialog.show();

        modalbottomsheet.findViewById(R.id.btn_camera).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            verificaPermissaoCamera(requestCode);
        });
        modalbottomsheet.findViewById(R.id.btn_galeria).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            verificaPermissaoGaleria(requestCode);
        });
        modalbottomsheet.findViewById(R.id.btn_close).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            Toast.makeText(this, "Fechando", Toast.LENGTH_SHORT).show();
        });
    }

    private void verificaPermissaoCamera(int requestCode) {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                dispatchTakePictureIntent(requestCode);
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(EmpresaFormAutoActivity.this, "Permissão Negada", Toast.LENGTH_SHORT).show();
            }
        };
        showDialogPermissao(permissionListener, new String[]{Manifest.permission.CAMERA},
                "Você negou as permissões para acessar a câmera do dispositivo, deseja permitir ?");

    }

    private void verificaPermissaoGaleria(int requestCode) {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                abrirGaleria(requestCode);
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(EmpresaFormAutoActivity.this, "Permissão Negada", Toast.LENGTH_SHORT).show();
            }
        };
        showDialogPermissao(permissionListener, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                "Você negou as permissões para acessar a galeria do dispositivo, deseja permitir ?");
    }

    private void showDialogPermissao(PermissionListener permissionListener, String[] permission, String msg) {
        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setDeniedTitle("Permissão Negada")
                .setDeniedMessage(msg)
                .setDeniedCloseButtonText("Não")
                .setGotoSettingButtonText("Sim")
                .setPermissions(permission)
                .check();
    }


    private void dispatchTakePictureIntent(int requestCode) {

        int request = 0;

        switch (requestCode) {
            case 0:
                request = 3;
                break;
            case 1:
                request = 4;
                break;
            case 2:
                request = 5;
                break;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create the File where the photo should go
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(this, "com.example.projetoauto.fileprovider", photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, request);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void abrirGaleria(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, requestCode);
    }


    // Validar os tipos e o endereço
    public void selecionarTipo(View view) {
        Intent intent = new Intent(this, TipoAutomovelActivity.class);
        startActivityForResult(intent, REQUEST_TIPO);
    }

    public void selecionarEndereco(View view) {
        Intent intent = new Intent(this, EmpresaSelecionaEnderecoActivity.class);
        startActivityForResult(intent, REQUEST_ENDERECO);

    }

    private void recuperaEndereco() {
        if (FirebaseHelper.getAutenticado()) {
            DatabaseReference enderecoRef = FirebaseHelper.getDatabaseReference()
                    .child("enderecos")
                    .child(FirebaseHelper.getIdFirebase());

            enderecoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            endereco = ds.getValue(Endereco.class);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            Bitmap bitmap0;
            Bitmap bitmap1;
            Bitmap bitmap2;

            Uri imagemSelecionada = data.getData();
            String caminhoImagem;

            if (requestCode == REQUEST_TIPO) {

                Tipo tipo = (Tipo) data.getSerializableExtra("tipoSelecionado");
                tipoSelecionado = tipo.getNome();
                btn_tipo.setText(tipoSelecionado);


            } else if (requestCode == REQUEST_ENDERECO) {
                endereco = (Endereco) data.getSerializableExtra("enderecoSelecionado");
                enderecoSelecionado = endereco.getLogradouro();
                txt_endereco.setText(endereco.getBairro() + " " + endereco.getUf());
                btn_endereco.setText(enderecoSelecionado);

            } else if (requestCode <= 2) { //Galeria

                try {
                    caminhoImagem = imagemSelecionada.toString();

                    switch (requestCode) {
                        case 0:
                            if (Build.VERSION.SDK_INT < 28) {
                                bitmap0 = MediaStore.Images.Media.getBitmap(getContentResolver(), imagemSelecionada);
                            } else {
                                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), imagemSelecionada);
                                bitmap0 = ImageDecoder.decodeBitmap(source);
                            }
                            img0.setImageBitmap(bitmap0);
                            break;
                        case 1:
                            if (Build.VERSION.SDK_INT < 28) {
                                bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), imagemSelecionada);
                            } else {
                                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), imagemSelecionada);
                                bitmap1 = ImageDecoder.decodeBitmap(source);
                            }
                            img1.setImageBitmap(bitmap1);
                            break;
                        case 2:
                            if (Build.VERSION.SDK_INT < 28) {
                                bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), imagemSelecionada);
                            } else {
                                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), imagemSelecionada);
                                bitmap2 = ImageDecoder.decodeBitmap(source);
                            }
                            img2.setImageBitmap(bitmap2);
                            break;

                    }

                    configUpload(requestCode, caminhoImagem);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else { //Camera

                File file = new File(currentPhotoPath);

                caminhoImagem = String.valueOf(file.toURI());

                switch (requestCode) {
                    case 3:
                        img0.setImageURI(Uri.fromFile(file));
                        break;
                    case 4:
                        img1.setImageURI(Uri.fromFile(file));
                        break;
                    case 5:
                        img2.setImageURI(Uri.fromFile(file));
                        break;
                }

                configUpload(requestCode, caminhoImagem);

            }
        }
    }

    private void configUpload(int requestCode, String caminhoImagem) {

        int request = 0;

        switch (requestCode) {
            case 0:
            case 3:
                request = 0;
                break;
            case 1:
            case 4:
                request = 1;
                break;
            case 2:
            case 5:
                request = 2;
                break;
        }
        Imagem imagem = new Imagem(caminhoImagem, request);
        if (imagemList.size() > 0) {

            boolean encontrou = false;

            for (int i = 0; i < imagemList.size(); i++) {
                if (imagemList.get(i).getIndex() == request) {
                    encontrou = true;
                }
            }
            if (encontrou) {
                imagemList.set(request, imagem);
            } else {
                imagemList.add(imagem);
            }

        } else {
            imagemList.add(imagem);
        }
    }

    private void iniciaComponentes() {
        text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Nova Aeronave");

        edt_titulo = findViewById(R.id.edt_titulo);
        edt_descricao = findViewById(R.id.edt_descricao);
        btn_tipo = findViewById(R.id.btn_tipo);
        edt_placa = findViewById(R.id.edt_placa);
        edt_modelo = findViewById(R.id.edt_modelo);
        edt_ano = findViewById(R.id.edt_ano);
        edt_quilometragem = findViewById(R.id.edt_quilometragem);
        btn_endereco = findViewById(R.id.btn_endereco);
        txt_endereco = findViewById(R.id.txt_endereco);
        btn_salvar = findViewById(R.id.btn_salvar);

        edt_valor_de_venda = findViewById(R.id.edt_valor_de_venda);
        edt_valor_comprado = findViewById(R.id.edt_valor_comprado);

        // Configurar EditTexts para entrada de números decimais
        edt_valor_de_venda.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        edt_valor_comprado.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);

        img0 = findViewById(R.id.img0);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
    }


    private void ocultarTeclado() {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(btn_tipo.getWindowToken(), 0);
    }

}