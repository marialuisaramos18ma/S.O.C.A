package com.example.projetoauto.helper;


import static java.text.DateFormat.getDateInstance;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Mascara {


    public static String getData(long dataPublicacao, int tipo) {

        final int DIA_MES_ANO = 1;
        final int HORA_MINUTO = 2;
        final int DIA_MES_ANO_HORA_MINUTO = 3;
        final int DIA_MES = 4;

        Locale locale = new Locale("PT", "br");
        String fuso = "America/Sao_Paulo";

        SimpleDateFormat dia = new SimpleDateFormat("dd", locale);
        dia.setTimeZone(TimeZone.getTimeZone(fuso));

        SimpleDateFormat mes = new SimpleDateFormat("MM", locale);
        mes.setTimeZone(TimeZone.getTimeZone(fuso));

        SimpleDateFormat ano = new SimpleDateFormat("yyyy", locale);
        ano.setTimeZone(TimeZone.getTimeZone(fuso));

        SimpleDateFormat hora = new SimpleDateFormat("HH", locale);
        hora.setTimeZone(TimeZone.getTimeZone(fuso));

        SimpleDateFormat minuto = new SimpleDateFormat("mm", locale);
        minuto.setTimeZone(TimeZone.getTimeZone(fuso));

        DateFormat dateFormat = getDateInstance();
        Date netDate = (new Date(dataPublicacao));
        dateFormat.format(netDate);

        String horaC = hora.format(netDate);
        String minutoC = minuto.format(netDate);

        String diaC = dia.format(netDate);
        String mesC = mes.format(netDate);
        String anoC = ano.format(netDate);

        if (tipo == 4) {
            switch (mesC) {
                case "01":
                    mesC = "Janeiro";
                    break;
                case "02":
                    mesC = "Fevereiro";
                    break;
                case "03":
                    mesC = "Março";
                    break;
                case "04":
                    mesC = "Abril";
                    break;
                case "05":
                    mesC = "Maio";
                    break;
                case "06":
                    mesC = "Junho";
                    break;
                case "07":
                    mesC = "Julho";
                    break;
                case "08":
                    mesC = "agosto";
                    break;
                case "09":
                    mesC = "Setembro";
                    break;
                case "10":
                    mesC = "Outubro";
                    break;
                case "11":
                    mesC = "Novembro";
                    break;
                case "12":
                    mesC = "Dezembro";
                    break;
            }
        }

        String time;
        switch (tipo) {
            case DIA_MES_ANO:
                time = diaC + "/" + mesC + "/" + anoC;
                break;
            case HORA_MINUTO:
                time = horaC + ":" + minutoC;
                break;
            case DIA_MES_ANO_HORA_MINUTO:
                time = diaC + "/" + mesC + "/" + anoC + " às " + horaC + ":" + minutoC;
                break;
            case DIA_MES:
                time = diaC + " " + mesC;
                break;
            default:
                time = "Erro";
                break;

        }
        return time;

    }


    public static String getValor(double valor) {

        NumberFormat nf = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(
                new Locale("PT", "br")));
        return nf.format(valor);
    }

}


