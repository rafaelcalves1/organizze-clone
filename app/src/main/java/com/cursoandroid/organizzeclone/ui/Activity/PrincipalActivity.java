package com.cursoandroid.organizzeclone.ui.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cursoandroid.organizzeclone.R;
import com.cursoandroid.organizzeclone.data.datasource.ConfiguracaoFirebase;
import com.cursoandroid.organizzeclone.ui.utils.Base64Custom;
import com.cursoandroid.organizzeclone.data.model.Movimentacao;
import com.cursoandroid.organizzeclone.data.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class PrincipalActivity extends AppCompatActivity {
    private String saldoGeral;
    private CheckBox visivel;
    private String mesAno;
    private Double despesaTotal;
    private Double receitaTotal;
    private Double resumoConta;
    private Double faturaCartaoRecebida;
    private Double faturaTotalCartao;
    private Double saldoBancoRecebido;
    private Double saldoTotalBanco;
    private String tipo;
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseref = ConfiguracaoFirebase.getFirebaseDataBase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        visivel = findViewById(R.id.checkVisivel);

        //recuperaValores();
        recuperaResumo();

        visivel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkBox();
            }
        });
    }

    public void recuperaResumo(){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseref.child("usuarios").child(idUsuario);

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);

                despesaTotal = usuario.getDespesaTotal();
                receitaTotal = usuario.getReceitaTotal();
                resumoConta = receitaTotal - despesaTotal;

                DecimalFormat decimalFormat = new DecimalFormat("0.##");
                String resultadoFormatado = decimalFormat.format(resumoConta);

                TextView textSaldoGeral = findViewById(R.id.saldo_geral_dinheiro);
                textSaldoGeral.setText("R$ " + resultadoFormatado);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void recuperaValores(){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef =

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Movimentacao movimentacao = snapshot.getValue(Movimentacao.class);
                switch (tipo){
                    case "d":
                        faturaCartaoRecebida = movimentacao.getValor();
                        faturaTotalCartao += faturaCartaoRecebida;

                        DecimalFormat decimalFormat = new DecimalFormat("0.##");
                        String fatura = decimalFormat.format(faturaTotalCartao);

                        TextView faturaCartao = findViewById(R.id.fatura_atual_cartao);
                        faturaCartao.setText("R$ "+ fatura);
                        break;
                    case "r":
                        saldoBancoRecebido = movimentacao.getValor();
                        saldoTotalBanco += saldoBancoRecebido;

                        DecimalFormat decimalFormat2 = new DecimalFormat("0.##");
                        String saldo = decimalFormat2.format(saldoTotalBanco);

                        TextView text = findViewById(R.id.saldo_conta_banco1);
                        text.setText("R$ " + saldo);

                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void checkBox(){
        TextView textoSaldoGeral = findViewById(R.id.saldo_geral_dinheiro);
        if(visivel.isChecked()){
            textoSaldoGeral.setBackgroundResource(R.color.colorDivider);
            visivel.setButtonDrawable(R.drawable.ic_visibility_off);
            textoSaldoGeral.setTextColor(getResources().getColor(R.color.transparente));
        }else{
            textoSaldoGeral.setBackgroundResource(R.color.transparente);
            visivel.setButtonDrawable(R.drawable.ic_visibility);
            textoSaldoGeral.setTextColor(getResources().getColor(R.color.colorTextSub));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_sair:
                autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
                autenticacao.signOut();
                startActivity(new Intent(this, SemiLoginActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void ajustarSaldos(View view){
    }

    public void adCartaoCredito(View view){
    }

    public void adNovasMetas(View view){
    }

    public void adDespesa(View view){
        startActivity(new Intent(this, DespesasActivity.class));
    }

    public void adReceita(View view){
        startActivity(new Intent(this, ReceitasActivity.class));
    }
}



