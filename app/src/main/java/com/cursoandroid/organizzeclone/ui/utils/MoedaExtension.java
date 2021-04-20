package com.cursoandroid.organizzeclone.ui.utils;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class MoedaExtension {

    public static String converteMoeda(String valor) {
        return NumberFormat.getCurrencyInstance(Locale.getDefault()).format(valor);
    }


}
