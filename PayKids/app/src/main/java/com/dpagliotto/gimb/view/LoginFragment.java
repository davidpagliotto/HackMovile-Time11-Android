package com.dpagliotto.gimb.view;


import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.dpagliotto.gimb.R;
import com.dpagliotto.gimb.components.CustomButtonPrimary;
import com.dpagliotto.gimb.components.CustomButtonSecondary;
import com.dpagliotto.gimb.components.CustomEditText;
import com.dpagliotto.gimb.db.dao.BaseDAO;
import com.dpagliotto.gimb.model.Cartao;
import com.dpagliotto.gimb.model.Dependente;
import com.dpagliotto.gimb.model.Titular;
import com.dpagliotto.gimb.support.HTTPHelper;
import com.dpagliotto.gimb.view.helper.MaskWatcher;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class LoginFragment extends BaseFragment {

    private static LoginFragment instance;

    // LoginFragment
    private CustomEditText edtLoginEmail;
    private CustomButtonPrimary btnLoginEntrar;

    private AlertDialog dialogUser;

    // Dialog Titular
    private CustomEditText edtUserEmail;
    private CustomEditText edtUserNome;
    private CustomEditText edtUserSobrenome;
    private CustomEditText edtUserCpf;
    private CustomEditText edtUserTelefone;
    private CustomButtonPrimary btnUserRegistrar;
    private CustomButtonSecondary btnUserCancelar;

    private Listener listener = new Listener();

    public LoginFragment() {

    }

    public static LoginFragment getInstance() {
        if (instance == null)
            instance = new LoginFragment();

        return instance;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        edtLoginEmail = view.findViewById(R.id.edtLoginEmail);
        btnLoginEntrar = view.findViewById(R.id.btnLoginEntrar);
        btnLoginEntrar.setOnClickListener(listener);

        this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        return view;
    }

    private class Listener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view == btnLoginEntrar) {
                String email = edtLoginEmail.getText().toString().trim();
                if (edtLoginEmail.getText().toString().trim().length() == 0) {

                    return;
                }
                else {
                    new RequestAsyncTask().execute(email);
                }
            }
            else if (view == btnUserRegistrar) {
                if (dialogUser != null && dialogUser.isShowing()) {
                    Titular titular = new Titular()
                            .setEmail(edtUserEmail.getText().toString())
                            .setNome(edtUserNome.getText().toString())
                            .setSobrenome(edtUserSobrenome.getText().toString())
                            .setCpf(edtUserCpf.getText().toString())
                            .setTelefone(edtUserTelefone.getText().toString());
                    dialogUser.dismiss();

                    new RequestAsyncTask().execute(titular);
                }
            }
            else if (view == btnUserCancelar) {
                if (dialogUser != null && dialogUser.isShowing())
                    dialogUser.dismiss();
            }
        }
    }

    private class RequestAsyncTask extends AsyncTask<Object, Void, Void> {

        private boolean dependenteCadastrado = false;
        private boolean titularCadastrado = false;

        private AsyncHttpResponseHandler responsePostTitulares = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String str = new String(responseBody);
                    if (str.trim().length() > 0) {
                        tratarRetornoPessoa(str);
                        titularCadastrado = true;

                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                        editor.putString(getString(R.string.perfil), "TIT");
                        editor.putString(getString(R.string.email), edtLoginEmail.getText().toString());
                        editor.apply();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        };

        private AsyncHttpResponseHandler responseGetDependente = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String str = new String(responseBody);
                    if (str.trim().length() > 0) {
                        tratarRetornoPessoa(str);
                        dependenteCadastrado = true;

                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                        editor.putString(getString(R.string.perfil), "DEP");
                        editor.putString(getString(R.string.email), edtLoginEmail.getText().toString());
                        editor.apply();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        };

        private AsyncHttpResponseHandler responseGetTitular = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String str = new String(responseBody);
                    if (str.trim().length() > 0) {
                        tratarRetornoPessoa(str);
                        titularCadastrado = true;

                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                        editor.putString(getString(R.string.perfil), "TIT");
                        editor.apply();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        };

        @Override
        protected Void doInBackground(Object... objects) {
            if (objects != null && objects.length > 0) {
                if (objects[0] instanceof  String)
                    getPessoas((String) objects[0]);
                else
                    postTitular(objects[0]);
            }
            return null;
        }

        private void getPessoas(final String email) {
            ((DefaultFragmentContainerActivity) getActivity()).mostrarStatus(true, "autenticando usuário");

            try {
                SyncHttpClient httpClient = new SyncHttpClient(false, 80, 443);

                httpClient.get(getActivity(), String.format(HTTPHelper.URL_DEPENDENTES_EMAIL_GET, email), null, "application/json", responseGetDependente);
                if (!dependenteCadastrado) {

                    httpClient.get(getActivity(), String.format(HTTPHelper.URL_TITULARES_EMAIL_GET, email), null, "application/json", responseGetTitular);
                    if (!titularCadastrado) {
                        ((DefaultFragmentContainerActivity) getActivity()).esconderStatus();

                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                mostrarCadastroTitular(email);
                            }
                        };
                        new Handler(getActivity().getMainLooper()).post(runnable);
                        return;
                    }
                }

                getActivity().finish();
            } catch (Exception e) {
                Log.e("", "doInBackground: " + e.getMessage());
            }

            ((DefaultFragmentContainerActivity) getActivity()).esconderStatus();
        }

        private void postTitular(Object obj) {
            ((DefaultFragmentContainerActivity) getActivity()).mostrarStatus(true, "autenticando usuário");
            Titular titular = (Titular) obj;
            try {
                SyncHttpClient httpClient = new SyncHttpClient(false, 80, 443);

                JSONObject jsonParams = new JSONObject();
                jsonParams.put("first_name", titular.getNome());
                jsonParams.put("last_name", titular.getSobrenome());
                jsonParams.put("taxpayer_id", titular.getCpf().replace(".", "").replace("-", ""));
                jsonParams.put("phone_number", titular.getTelefone().replace("(", "").replace(")", "").replace("-", ""));
                jsonParams.put("email", titular.getEmail());
                StringEntity entity = new StringEntity(jsonParams.toString());

                httpClient.post(getActivity(), HTTPHelper.URL_TITULARES_POST, entity, "application/json", responsePostTitulares);
            } catch (Exception e) {
                Log.e("", "doInBackground: " + e.getMessage());
            }

            ((DefaultFragmentContainerActivity) getActivity()).esconderStatus();
        }

        private void tratarRetornoPessoa(String str) {
            if (str.trim().length() > 0) {
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                HashMap map = gson.fromJson(str, HashMap.class);
                Titular titular = new Titular()
                        .setId(((Double)map.get("id")).intValue())
                        .setNome(map.get("nome") == null ? "" : map.get("nome").toString())
                        .setSobrenome(map.get("sobrenome") == null ? "" : map.get("sobrenome").toString())
                        .setCpf(map.get("cpf") == null ? "" : map.get("cpf").toString())
                        .setTelefone(map.get("telefone") == null ? "" : map.get("telefone").toString())
                        .setEmail(map.get("email") == null ? "" : map.get("email").toString())
                        .setIdzoop(map.get("idzoop") == null ? "" : map.get("idzoop").toString());

                BaseDAO dao = new BaseDAO(getActivity(), Titular.class);
                if (dao.createOrUpdate(titular)) {
                    if (map.get("cartao") != null && map.get("cartao") instanceof Map) {
                        Map mapCartao = (Map) map.get("cartao");
                        Cartao cartao = new Cartao()
                                .setId(((Double)mapCartao.get("id")).intValue())
                                .setNumero(mapCartao.get("numero") == null ? "" : mapCartao.get("numero").toString())
                                .setTitular(mapCartao.get("titular") == null ? "" : mapCartao.get("titular").toString())
                                .setAnoVencimento(mapCartao.get("anoVencimento") == null ? "" : mapCartao.get("anoVencimento").toString())
                                .setMesVencimento(mapCartao.get("mesVencimento") == null ? "" : mapCartao.get("mesVencimento").toString())
                                .setIdzoop(mapCartao.get("idzoop") == null ? "" : mapCartao.get("idzoop").toString())
                                .setTokenzoop(mapCartao.get("tokenzoop") == null ? "" : mapCartao.get("tokenzoop").toString());

                        BaseDAO daoCartao = new BaseDAO(getActivity(), Cartao.class);
                        daoCartao.createOrUpdate(cartao);

                        titular.setCartao(cartao);
                        dao.createOrUpdate(titular);
                    }
                    if (map.get("dependentes") != null && map.get("dependentes") instanceof List) {
                        for (Object objMap : ((List) map.get("dependentes"))) {
                            Map mapDependente = (Map) objMap;
                            Dependente dependente = new Dependente()
                                    .setId(((Double)mapDependente.get("id")).intValue())
                                    .setNome(mapDependente.get("nome") == null ? "" : mapDependente.get("nome").toString())
                                    .setSobrenome(mapDependente.get("sobrenome") == null ? "" : mapDependente.get("sobrenome").toString())
                                    .setTelefone(mapDependente.get("telefone") == null ? "" : mapDependente.get("telefone").toString())
                                    .setEmail(mapDependente.get("email") == null ? "" : mapDependente.get("email").toString())
                                    .setIdzoop(mapDependente.get("idzoop") == null ? "" : mapDependente.get("idzoop").toString())
                                    .setSaldo(mapDependente.get("saldo") == null ? 0.0 : (Double) mapDependente.get("saldo"))
                                    .setTitular(titular);

                            BaseDAO daoDependente = new BaseDAO(getActivity(), Dependente.class);
                            daoDependente.createOrUpdate(dependente);
                        }
                    }
                }
            }
        }

        private void mostrarCadastroTitular(String email) {
            View userDialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_titular, null);

            edtUserEmail = userDialogView.findViewById(R.id.edtUserEmail);
            edtUserNome = userDialogView.findViewById(R.id.edtUserNome);
            edtUserSobrenome = userDialogView.findViewById(R.id.edtUserSobrenome);
            edtUserCpf = userDialogView.findViewById(R.id.edtUserCpf);
            edtUserCpf.addTextChangedListener(MaskWatcher.buildCpf(edtUserCpf));
            edtUserTelefone = userDialogView.findViewById(R.id.edtUserTelefone);
            edtUserTelefone.addTextChangedListener(MaskWatcher.buildTelefone(edtUserTelefone));
            btnUserRegistrar = userDialogView.findViewById(R.id.btnUserRegistrar);
            btnUserCancelar = userDialogView.findViewById(R.id.btnUserCancelar);

            edtUserEmail.setText(email);
            btnUserRegistrar.setOnClickListener(listener);
            btnUserCancelar.setOnClickListener(listener);

            dialogUser = new AlertDialog.Builder(getActivity())
                    .setView(userDialogView)
                    .setCancelable(false)
                    .create();

            dialogUser.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogUser.show();
        }
    }

}



