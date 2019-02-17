package com.dpagliotto.gimb.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dpagliotto.gimb.R;
import com.dpagliotto.gimb.components.CustomButtonPrimary;
import com.dpagliotto.gimb.components.CustomEditText;
import com.dpagliotto.gimb.model.Dependente;
import com.dpagliotto.gimb.view.helper.MaskWatcher;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class DependenteHomeFragment extends BaseFragment {

    private static DependenteHomeFragment instance;

    private CustomEditText edtSaldo;
    private CustomButtonPrimary btnPagamento;

    private Listener listener = new Listener();

    private Dependente dependente;

    public static DependenteHomeFragment getInstance() {
        if (instance == null)
            instance = new DependenteHomeFragment();

        return instance;
    }

    public DependenteHomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dependente_home, container, false);

        edtSaldo = view.findViewById(R.id.edtDependenteHomeSaldo);
        edtSaldo.addTextChangedListener(MaskWatcher.buildMonetario(edtSaldo));
        btnPagamento = view.findViewById(R.id.btnDependenteHomePagar);
        btnPagamento.setOnClickListener(listener);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        btnPagamento.setEnabled(false);
        edtSaldo.setText("0");
        if (dependente != null && dependente.getSaldo() > 0) {
            edtSaldo.setText(String.format(Locale.getDefault(),"%f", dependente.getSaldo()));
            btnPagamento.setEnabled(true);
        }
    }

    private class Listener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view == btnPagamento) {
                Toast.makeText(getActivity(), "Ta pagando porra", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
