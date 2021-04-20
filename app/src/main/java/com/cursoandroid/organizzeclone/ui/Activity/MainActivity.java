package com.cursoandroid.organizzeclone.ui.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.cursoandroid.organizzeclone.R;
import com.cursoandroid.organizzeclone.data.datasource.ConfiguracaoFirebase;
import com.cursoandroid.organizzeclone.ui.adapter.ApresentacaoPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configuraBotaoPular();
        configuraSliderDeApresentacao();
    }

    private void configuraBotaoPular() {
        Button btPular = findViewById(R.id.main_pular_button);
        btPular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abreTelaSemiLogin();
            }
        });
    }

    private void abreTelaSemiLogin() {
        startActivity(new Intent(this, SemiLoginActivity.class));
        finish();
    }

    private void configuraSliderDeApresentacao() {
        ViewPager2 slider = findViewById(R.id.main_sliderPager);
        TabLayout tabLayout = findViewById(R.id.main_tablayout);
        ApresentacaoPagerAdapter adapter = new ApresentacaoPagerAdapter(getSupportFragmentManager(), getLifecycle(), new ApresentacaoPagerAdapter.Listenner() {
            @Override
            public void lastFragment() {
             startActivity(new Intent(MainActivity.this, SemiLoginActivity.class));
             finish();
            }
        });
        slider.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, slider, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
            }
        }).attach();
    }

    @Override
    protected void onStart() {
        super.onStart();
        verificarUsuarioLogado();
    }

    public void verificarUsuarioLogado() {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        //autenticacao.signOut();
        if (autenticacao.getCurrentUser() != null) {
            abrirTelaPrincipal();
        }
    }

    public void abrirTelaPrincipal() {
        startActivity(new Intent(this, PrincipalActivity.class));

    }


}
