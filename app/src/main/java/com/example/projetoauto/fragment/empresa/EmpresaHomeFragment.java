package com.example.projetoauto.fragment.empresa;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.projetoauto.R;
import com.example.projetoauto.activity.FiltrosActivity;
import com.example.projetoauto.activity.empresa.EmpresaFormAutoActivity;
import com.example.projetoauto.activity.TipoAutomovelActivity;
import com.example.projetoauto.adapter.AdapterListaAutomovel;
import com.example.projetoauto.helper.FirebaseHelper;
import com.example.projetoauto.helper.SPFiltro;
import com.example.projetoauto.model.Automovel;
import com.example.projetoauto.model.Filtro;
import com.example.projetoauto.model.Tipo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class EmpresaHomeFragment extends Fragment implements AdapterListaAutomovel.OnclickListener {

    private AdapterListaAutomovel adapterListaAutomovel;

    private final List<Automovel> automovelList = new ArrayList<>();

    private SwipeableRecyclerView rv_automoveis;
    private Button btn_inserir;

    private Button btn_tipos;

    private Button btn_filtros;

    private SearchView search_view;

    private EditText edit_search_view;

    private Filtro filtro = new Filtro();

    private ProgressBar progressBar;

    private TextView text_info;

    private final int REQUEST_TIPO = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_empresa_home, container, false);

        iniciarComponentes(view);

        configRv();

        configCliques();

        configSearchView();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        configFiltros();
    }

    private void configSearchView() {

        edit_search_view = search_view.findViewById(androidx.appcompat.R.id.search_src_text);

        search_view.findViewById(androidx.appcompat.R.id.search_close_btn).setOnClickListener(v -> {
            limparPesquisa();
        });

        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SPFiltro.setFiltro(requireActivity(), "pesquisa", query);

                configFiltros();

                ocultarTeclado();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void limparPesquisa() {
        search_view.clearFocus();

        edit_search_view.setText("");

        SPFiltro.setFiltro(requireActivity(), "pesquisa", "");

        configFiltros();

        ocultarTeclado();
    }

    private void configFiltros() {
        filtro = SPFiltro.getFiltro(requireActivity());

        if (!filtro.getTipo().isEmpty()) {
            btn_tipos.setText(filtro.getTipo());
        } else {
            btn_tipos.setText("Tipos");
        }

        recuperaAutomoveis();
    }

    private void recuperaAutomoveis() {
        if (FirebaseHelper.getAutenticado()) {
            DatabaseReference automoveisRef = FirebaseHelper.getDatabaseReference()
                    .child("meus_automoveis")
                    .child(FirebaseHelper.getIdFirebase());

            automoveisRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        automovelList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Automovel automovel = ds.getValue(Automovel.class);
                            automovelList.add(automovel);


                            // Filtro por categoria
                            if (!filtro.getTipo().isEmpty()) {
                                if (!filtro.getTipo().equals("Todos os Tipos")) {
                                    for (Automovel automovel1 : new ArrayList<>(automovelList)) {
                                        if (!automovel1.getIdTipo().equals(filtro.getTipo())) {
                                            automovelList.remove(automovel1);
                                        }
                                    }
                                }
                            }

                            // Filtro por valor minimo
                            if (filtro.getValorMin() > 0) {
                                for (Automovel automovel1 : new ArrayList<>(automovelList)) {
                                    if (automovel1.getValorDeVenda() < filtro.getValorMin()) {
                                        automovelList.remove(automovel1);
                                    }
                                }
                            }

                            // Filtro por valor maximo
                            if (filtro.getValorMax() > 0) {
                                for (Automovel anuncio : new ArrayList<>(automovelList)) {
                                    if (automovel.getValorDeVenda() > filtro.getValorMax()) {
                                        automovelList.remove(anuncio);
                                    }
                                }
                            }

                            // Filtro por nome pesquisado
                            if (!filtro.getPesquisa().isEmpty()) {
                                for (Automovel automovel1 : new ArrayList<>(automovelList)) {
                                    if (!automovel1.getTitulo().toLowerCase().contains(filtro.getPesquisa().toLowerCase())) {
                                        automovelList.remove(automovel1);
                                    }
                                }
                            }
                            if (automovelList.isEmpty()) {
                                text_info.setText("Nenhum anúncio encontrado.");
                            } else {
                                text_info.setText("");
                            }

                        }
                        text_info.setText("");
                        Collections.reverse(automovelList);
                        adapterListaAutomovel.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    } else {
                        text_info.setText("Nenhuma Aeronave Cadastrada");
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            text_info.setText("");
            progressBar.setVisibility(View.GONE);
        }
    }


    private void configRv() {
        rv_automoveis.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_automoveis.setHasFixedSize(true);
        adapterListaAutomovel = new AdapterListaAutomovel(automovelList, this);
        rv_automoveis.setAdapter(adapterListaAutomovel);

        rv_automoveis.setListener(new SwipeLeftRightCallback.Listener() {
            @Override
            public void onSwipedLeft(int position) {
                //Delete
                showDialogDelete(automovelList.get(position));
            }

            @Override
            public void onSwipedRight(int position) {
                //Edit
                showDialogEdit(automovelList.get(position));
            }
        });
    }

    private void showDialogEdit(Automovel automovel) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
        alertDialog.setTitle("Deseja Editar o automovel?");
        alertDialog.setNegativeButton("Não", ((dialog, whitch) -> {

            dialog.dismiss();
            adapterListaAutomovel.notifyDataSetChanged();
        })).setPositiveButton("Sim", ((dialog, whitch) -> {
            Intent intent = new Intent(requireActivity(), EmpresaFormAutoActivity.class);
            intent.putExtra("automovelSelecionado", automovel);
            startActivity(intent);

            adapterListaAutomovel.notifyDataSetChanged();
        }));

        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }

    private void showDialogDelete(Automovel automovel) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
        alertDialog.setTitle("Deseja remover o automovel ?");
        alertDialog.setNegativeButton("Não", ((dialog, which) -> {
            dialog.dismiss();
            adapterListaAutomovel.notifyDataSetChanged();
        })).setPositiveButton("Sim", ((dialog, which) -> {
            automovelList.remove(automovel);
            automovel.remover();

            adapterListaAutomovel.notifyDataSetChanged();
        }));

        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }

    private void configCliques() {
        btn_inserir.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), EmpresaFormAutoActivity.class));
        });
        btn_tipos.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), TipoAutomovelActivity.class);
            intent.putExtra("Todos os Tipos", true);
            startActivityForResult(intent, REQUEST_TIPO);
        });
        btn_filtros.setOnClickListener(v -> startActivity(new Intent(requireActivity(), FiltrosActivity.class)));
    }

    private void iniciarComponentes(View view) {
        rv_automoveis = view.findViewById(R.id.rv_automoveis);
        btn_inserir = view.findViewById(R.id.btn_inserir);
        progressBar = view.findViewById(R.id.progressBar);
        text_info = view.findViewById(R.id.text_info);
        btn_tipos = view.findViewById(R.id.btn_tipos);
        btn_filtros = view.findViewById(R.id.btn_filtros);
        search_view = view.findViewById(R.id.search_view);

    }

    @Override
    public void OnClick(Automovel automovel) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == requireActivity().RESULT_OK) {
            if (requestCode == REQUEST_TIPO) {
                Tipo tipoSelecionada = (Tipo) data.getExtras().getSerializable("tipoSelecionado");
                SPFiltro.setFiltro(requireActivity(), "tipo", tipoSelecionada.getNome());

                configFiltros();
            }
        }
    }

    private void ocultarTeclado() {
        InputMethodManager inputMethodManager = (InputMethodManager)
                getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(search_view.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

}