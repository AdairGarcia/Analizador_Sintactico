package arbol;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Arbol {

    public List<String> crearArbol(String entrada){
        List <String> expresiones = new ArrayList<>();

        String patron = "\\b\\w+\\b|[+\\-*/%=()]";
        Pattern pattern = Pattern.compile(patron);
        Matcher matcher = pattern.matcher(entrada);

        while(matcher.find()){
            expresiones.add(matcher.group());
        }

        return expresiones;
    }

    public void imprimirArbol(List <String> expresiones, int tabulador){
        Stack<Integer> pilaIndentacion = new Stack<>();
        int nivelActual = tabulador;

        for (String subcadena : expresiones) {
            if (subcadena.equals("(")) {
                imprimirIndentacion(nivelActual);
                System.out.println(subcadena);

                pilaIndentacion.push(nivelActual);
                nivelActual++;
            } else if (subcadena.equals(")")) {
                nivelActual = pilaIndentacion.pop();

                imprimirIndentacion(nivelActual);
                System.out.println(subcadena);
            } else {
                imprimirIndentacion(nivelActual);
                System.out.println(subcadena);

                if (esOperador(subcadena)) {
                    nivelActual++;
                }
            }
        }
    }

    public static void imprimirIndentacion(int nivelIndentacion) {
        for (int i = 0; i < nivelIndentacion; i++) {
            System.out.print(" ");
        }
    }

    public static boolean esOperador(String subcadena) {
        return subcadena.matches("[+\\-*/()]");
    }


}
