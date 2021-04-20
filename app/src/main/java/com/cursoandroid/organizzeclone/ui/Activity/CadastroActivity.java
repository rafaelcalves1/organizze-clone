package com.cursoandroid.organizzeclone.ui.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cursoandroid.organizzeclone.R;
import com.cursoandroid.organizzeclone.config.ConfiguracaoFirebase;
import com.cursoandroid.organizzeclone.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {
    private EditText campoNome, campoEmail, campoSenha;
    private Button botaoCadastrar;
    private FirebaseAuth autenticacao;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        campoNome = findViewById(R.id.editNome);
        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);
        botaoCadastrar = findViewById(R.id.buttonCadastrar);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textoNome = campoNome.getText().toString();
                String textoEmail = campoEmail.getText().toString();
                String textoSenha = campoSenha.getText().toString();

                if(!textoNome.isEmpty()){
                    if(!textoEmail.isEmpty()){
                        if(!textoSenha.isEmpty()){
                            usuario = new Usuario();

                            usuario.setNome( textoNome);
                            usuario.setEmail( textoEmail);
                            usuario.setSenha( textoSenha );
                            cadastrarUsuario( );
                        }else{
                            Toast.makeText(CadastroActivity.this,"Preencha a Senha", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(CadastroActivity.this, "Preencha o Email", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(CadastroActivity.this, "Preencha o Nome", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void cadastrarUsuario(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    finish();
                }else{
                    String excecao = "";

                    try{
                        throw task.getException();
                    }catch ( FirebaseAuthWeakPasswordException e){
                            excecao = getString(R.string.mensagem_senha_fraca);
                    }catch ( FirebaseAuthInvalidCredentialsException e ){
                            excecao = getString(R.string.mensagem_email_invalido);
                    }catch ( FirebaseAuthUserCollisionException e ){
                            excecao = getString(R.string.mensagem_conta_ja_existe);
                    }catch (Exception e ){
                            excecao = getString(R.string.mensagem_erro_cadastro) + e.getMessage();
                            e.printStackTrace();
                    }

                    Toast.makeText(CadastroActivity.this, excecao, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
