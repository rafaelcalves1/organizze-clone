package com.cursoandroid.organizzeclone.data.datasource;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFirebase{

    private static DatabaseReference firebase;
    private static FirebaseAuth autenticacao;


    //Retorna a  instancia  do FirebaseAuth
    public static FirebaseAuth getFirebaseAutenticacao(){
        if(autenticacao == null){
            autenticacao = FirebaseAuth.getInstance();
        }
        return  autenticacao;
    }

    public static DatabaseReference getFirebaseDataBase(){
        if(firebase == null ){
            firebase = FirebaseDatabase.getInstance().getReference();
        }
        return firebase;
    }
}
