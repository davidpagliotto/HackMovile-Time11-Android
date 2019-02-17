package com.dpagliotto.paykids.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dpagliotto.paykids.R;
import com.dpagliotto.paykids.components.CustomButtonPrimary;
import com.dpagliotto.paykids.components.CustomEditText;
import com.dpagliotto.paykids.db.dao.BaseDAO;
import com.dpagliotto.paykids.model.Dependente;
import com.dpagliotto.paykids.support.PermissionsHelper;
import com.dpagliotto.paykids.view.helper.MaskWatcher;

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
        if (dependente != null) {
            BaseDAO dao = new BaseDAO(getActivity(), Dependente.class);
            dependente = dao.buscaPorID(dependente.getId());

            if (dependente != null) {
                Log.v("QRScanner", dependente.getSaldo().toString());
                if (dependente.getSaldo() > 0)
                    edtSaldo.setText(Double.toString(dependente.getSaldo() * 10));
                btnPagamento.setEnabled(true);
            }
        }
    }

    public void setDependente(Dependente dependente) {
        this.dependente = dependente;
    }

    private class Listener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view == btnPagamento) {
                Intent intent = new Intent(getActivity(), QRCodeScannerActivity.class);

                Bundle extras = new Bundle();
                extras.putParcelable("DEP", dependente);
                intent.putExtras(extras);

                getActivity().startActivity(intent);
            }
        }
    }

}
