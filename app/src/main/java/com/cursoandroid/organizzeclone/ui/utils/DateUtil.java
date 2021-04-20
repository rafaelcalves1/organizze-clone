package com.cursoandroid.organizzeclone.ui.utils;

import java.text.SimpleDateFormat;

public class DateUtil {

    public static String dataAtual(){
        Long  data = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataString = simpleDateFormat.format(data);
        return  dataString;
    }

    public static String mesAnoEscolhida(String data){

      String retornoData[] = data.split("/");

      String mes = retornoData[1];
      String ano = retornoData[2];

      String mesAno = mes + ano;
      return mesAno;
    }
}
