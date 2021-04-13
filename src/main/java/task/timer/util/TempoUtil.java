package task.timer.util;

public class TempoUtil {
    
    public static String somarTempo(String tempo1, String tempo2) {
        try {
            int hora1 = Integer.parseInt(tempo1.substring(0, 2));
            int min1 = Integer.parseInt(tempo1.substring(3, 5));
            int seg1 = Integer.parseInt(tempo1.substring(6, 8));

            int hora2 = Integer.parseInt(tempo2.substring(0, 2));
            int min2 = Integer.parseInt(tempo2.substring(3, 5));
            int seg2 = Integer.parseInt(tempo2.substring(6, 8));

            int hora3 = hora1 + hora2;
            int min3 = min1 + min2;
            int seg3 = seg1 + seg2;

            if (seg3 >= 60) {
                min3++;
                seg3 -= 60;
            }

            if (min3 >= 60) {
                hora3++;
                min3 -= 60;
            }

            return String.format("%02d:%02d:%02d", hora3, min3, seg3);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "00:00:00";
        }
    }

    public static String calcularTempoDuracao(String fimParam, String inicParam) {
        try {
            int horFim = Integer.parseInt(fimParam.substring(0, 2));
            int minFim = Integer.parseInt(fimParam.substring(3, 5));
            int segFim = Integer.parseInt(fimParam.substring(6, 8));

            int horIni = Integer.parseInt(inicParam.substring(0, 2));
            int minIni = Integer.parseInt(inicParam.substring(3, 5));
            int segIni = Integer.parseInt(inicParam.substring(6, 8));

            int horaFinal = 0;
            int minFinal;
            int segFinal;

            if (horFim > horIni) {
                horaFinal = horFim - horIni;
            }

            minFinal = minFim - minIni;
            segFinal = segFim - segIni;

            if (segFinal < 0) {
                segFinal += 60;
                minFinal -= 1;
            }

            if (minFinal < 0) {
                minFinal += 60;
                horaFinal -= 1;
            }

            // constroi a hora final
            return String.format("%02d:%02d:%02d", horaFinal, minFinal, segFinal);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "00:00:00";
    }
}
