package com.cursoandroid.organizzeclone.ui.Activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cursoandroid.organizzeclone.R;
import com.cursoandroid.organizzeclone.data.datasource.ConfiguracaoFirebase;
import com.cursoandroid.organizzeclone.ui.utils.Base64Custom;
import com.cursoandroid.organizzeclone.ui.utils.DateUtil;
import com.cursoandroid.organizzeclone.data.model.Movimentacao;
import com.cursoandroid.organizzeclone.data.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;
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

public class DespesasActivity extends AppCompatActivity {
    private EditText campoValor, campoCategorias;
    private TextInputEditText campoDescricao;
    private TextView textPagamentos, textdata;
    private ImageView imageCategorias, imagePagamentos;
    private Movimentacao movimentacao;
    private Double despesaTotal;
    private Double despesaAtualizada;
    private DatabaseReference firebaseref = ConfiguracaoFirebase.getFirebaseDataBase();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        campoValor = findViewById(R.id.despesas_text_edit_valor);
        campoDescricao = findViewById(R.id.despesas_text_edit_descricao);
        campoCategorias = findViewById(R.id.despesas_text_edit_categorias);
        textPagamentos = findViewById(R.id.despesas_text_tipo_pagamento);
        textdata = findViewById(R.id.despesas_text_data);

        imagePagamentos = findViewById(R.id.despesas_imagem_tipo_pagamento);

        textdata.setText(DateUtil.dataAtual());

        recuperarDespesaTotal();

    }

    public void btVoltar(View view) {
        onBackPressed();
    }

    public void salvarDespesa(View view) {

        String data = textdata.getText().toString();
        Double valorRecuperado = Double.parseDouble(campoValor.getText().toString());

        movimentacao = new Movimentacao();
        movimentacao.setValor(valorRecuperado);
        movimentacao.setCategoria(campoCategorias.getText().toString());
        movimentacao.setDescricao(campoDescricao.getText().toString());
        movimentacao.setTipo("d");
        movimentacao.setTipoPag(textPagamentos.getText().toString());
        movimentacao.setData(data);

        despesaAtualizada = valorRecuperado + despesaTotal;
        atualizaDespesas(despesaAtualizada);
        onBackPressed();
        movimentacao.salvar(data);

    }

    public void dialogCalendario(View view) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(DespesasActivity.this,
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

    public void recuperarDespesaTotal() {
        String emailsUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailsUsuario);
        final DatabaseReference usuarioRef = firebaseref.child("usuarios")
                .child(idUsuario);
        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                despesaTotal = usuario.getDespesaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void atualizaDespesas(Double despesa) {
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseref.child("usuarios").child(idUsuario);

        usuarioRef.child("despesaTotal").setValue(despesa);

    }

}
