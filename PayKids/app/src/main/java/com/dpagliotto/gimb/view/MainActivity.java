package com.dpagliotto.gimb.view;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dpagliotto.gimb.R;
import com.dpagliotto.gimb.db.dao.BaseDAO;
import com.dpagliotto.gimb.model.BaseModel;
import com.dpagliotto.gimb.model.Dependente;
import com.dpagliotto.gimb.model.Titular;
import com.dpagliotto.gimb.support.AppHelper;
import com.dpagliotto.gimb.view.helper.Helper;
import com.facebook.stetho.Stetho;

import java.util.List;

public class MainActivity extends BaseActivity {

    private FrameLayout mContent;
    private Fragment mCurrentFragment;
    private DrawerLayout mDrawer;

    private NavigationView mSideNavigation;

    private RelativeLayout mLayoutProgress;
    private TextView mProgressMessage;

    private Handler mHandler;

    //private static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        Stetho.initializeWithDefaults(this);

        AppHelper.mApplicationContext = getApplicationContext();
        //GPSService.instantiate(this);
        //instance = this;

        // Config Toolbar
        {
            Toolbar toolbar = findViewById(R.id.mainToolbar);
            setSupportActionBar(toolbar);

            mDrawer = findViewById(R.id.drawer_layout);
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, mDrawer, toolbar, R.string.abrir, R.string.fechar);
            mDrawer.addDrawerListener(toggle);
            toggle.syncState();
        }

        mSideNavigation = findViewById(R.id.side_navigation_view);

        mHandler = new Handler(getMainLooper());

        mContent = findViewById(R.id.contentFragment);

        mLayoutProgress = findViewById(R.id.layoutProgress);
        mLayoutProgress.setVisibility(View.GONE);

        mProgressMessage = findViewById(R.id.txtMainProgressMessage);
        mProgressMessage.setText("");

        // se nao tem titulares cadastrado, abrir tela de cadastro
        BaseDAO dao = new BaseDAO(this, Titular.class);
        List<Titular> titulares = dao.listarTodos();
        if (titulares == null || titulares.size() == 0) {
            Intent intent = new Intent(this, DefaultFragmentContainerActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String perfil = preferences.getString(getString(R.string.perfil), "");
        if (perfil != null && perfil.trim().length() > 0) {
            BaseDAO dao = new BaseDAO(this, Titular.class);
            List<Titular> titulares = dao.listarTodos();
            if (titulares != null && titulares.size() > 0) {
                if ("TIT".equalsIgnoreCase(perfil)) { // Perfil de titular
                    selectFragment(Helper.OPT_TITULAR_HOME_FRAGMENT);
                    setUserData(titulares.get(0));
                } else if ("DEP".equalsIgnoreCase(perfil)) { // Perfil de Dependente
                    selectFragment(Helper.OPT_DEPENDENTE_HOME_FRAGMENT);

                    for (Dependente dependente: titulares.get(0).getDependentes()) {
                        if (dependente.getEmail().equalsIgnoreCase(preferences.getString(getString(R.string.email), ""))) {
                            setUserData(dependente);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
			// Do not close app, put in background
            moveTaskToBack(true);
        }
    }

    public void setUserData(BaseModel model) {
        if (model != null) {
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

            String nome = "";
            String email = "";
            if (model instanceof Titular) {
                nome = String.format("%s %s", ((Titular) model).getNome(), ((Titular) model).getSobrenome());
                email = ((Titular) model).getEmail();
            } else if (model instanceof Dependente) {
                nome = String.format("%s %s", ((Dependente) model).getNome(), ((Dependente) model).getSobrenome());
                email = ((Dependente) model).getEmail();
            }

            TextView txt = mSideNavigation.getHeaderView(0).findViewById(R.id.txtNavHeaderName);
            if (txt != null)
                txt.setText(nome);

            txt = mSideNavigation.getHeaderView(0).findViewById(R.id.txtNavHeaderEmail);
            if (txt != null)
                txt.setText(email);
        }
    }

    private void selectFragment(Integer option) {
        if (mLayoutProgress.getVisibility() == View.VISIBLE) return;

        Fragment fragment = null;

        if (option.equals(Helper.OPT_TITULAR_HOME_FRAGMENT)) {
            setTitle("home");
            fragment = TitularHomeFragment.getInstance();
        }
        else if (option.equals(Helper.OPT_DEPENDENTE_HOME_FRAGMENT)) {
            setTitle("home");
            fragment = DependenteHomeFragment.getInstance();
        }

        if (mCurrentFragment != fragment) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (mCurrentFragment != null)
                ft.remove(mCurrentFragment);

            mCurrentFragment = fragment;

            ft.add(mContent.getId(), mCurrentFragment).commit();
        }
    }

    public void hideStatus() {
        setStatus(false, "");
    }

    public void setStatus(final boolean show, final String message) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mLayoutProgress.setVisibility(View.GONE);
                mProgressMessage.setText("");

                if (show) {
                    mLayoutProgress.setVisibility(View.VISIBLE);
                    mProgressMessage.setText(message);
                }
            }
        };

        mHandler.post(runnable);
    }

    public void showMessage(final String message) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Helper.customAlertDialog(MainActivity.this, message, Helper.positiveBtnOK(null));
            }
        };

        mHandler.post(runnable);
    }

}
