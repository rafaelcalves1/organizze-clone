package com.cursoandroid.organizzeclone.ui.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.cursoandroid.organizzeclone.R;
import com.cursoandroid.organizzeclone.data.datasource.ConfiguracaoFirebase;
import com.cursoandroid.organizzeclone.ui.utils.Base64Custom;
import com.cursoandroid.organizzeclone.ui.utils.DateUtil;
import com.cursoandroid.organizzeclone.data.model.Movimentacao;
import com.cursoandroid.organizzeclone.data.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ReceitasActivity extends AppCompatActivity {
    private EditText campoValor, campoCategorias;
    private EditText campoDescricao;
    private TextView textConta, textdata;
    private Movimentacao movimentacao;
    private Double receitaTotal;
    private Double receitaAtualizada;
    private DatabaseReference firebaseref = ConfiguracaoFirebase.getFirebaseDataBase();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receitas);

        campoValor = findViewById(R.id.receita_text_edit_valor);
        campoCategorias = findViewById(R.id.receita_text_edit_categorias);
        campoDescricao = findViewById(R.id.receita_text_edit_descricao);

        textConta = findViewById(R.id.receita_text_escolha_conta);
        textdata = findViewById(R.id.receita_text_data);

        textdata.setText(DateUtil.dataAtual());

        recuperaReceitaTotal();

    }
    public void btVoltar(View view) {
        onBackPressed();
    }


    public void salvarReceita(View view){

        String data = textdata.getText().toString();
        Double valorRecuperado = Double.parseDouble(campoValor.getText().toString());

        movimentacao = new Movimentacao();
        movimentacao.setValor(valorRecuperado);
        movimentacao.setTipo("r");
        movimentacao.setData(data);
        movimentacao.setDescricao(campoDescricao.getText().toString());
        movimentacao.setCategoria(campoCategorias.getText().toString());
        movimentacao.setTipoPag(textConta.getText().toString());

        receitaAtualizada = receitaTotal + valorRecuperado;
        atualizaReceita(receitaAtualizada);

        onBackPressed();
        movimentacao.salvar(data);

    }

    public void recuperaReceitaTotal(){
        String emailsUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailsUsuario);
        final DatabaseReference usuarioRef = firebaseref.child("usuarios")
                .child(idUsuario);
        usuarioRef.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                receitaTotal = usuario.getReceitaTotal();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void dialogCalendario(View view) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(ReceitasActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        calendar.set(year, month, day);
                        String format = "dd/MM/yyyy";
                        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
                        Date date;

                        try {
                            date = sdf.parse(sdf.format(calendar.getTime()));
                            String dayS = new SimpleDateFormat("dd", Locale.ENGLISH).format(date);
                            String monthS = new SimpleDateFormat("MM", Locale.ENGLISH).format(date);
                            String yearS = new SimpleDateFormat("yyyy", Locale.ENGLISH).format(date);

                            textdata.setText((dayS + "/" + monthS + "/" + yearS));
                        } catch (ParseException ignored) {

                        }
                    }
                }, year, month, day);
        datePickerDialog.show();
        datePickerDialog.getDatePicker();
    }

    public void atualizaReceita(Double receita){
        String emailsUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailsUsuario);
        DatabaseReference usuarioRef = firebaseref.child("usuarios").child(idUsuario);

        usuarioRef.child("receitaTotal").setValue(receita);
    }
}
