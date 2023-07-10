package test;

import automatapila.AutomataPila;
import auxiliar.Cadena;

public class TestAutomata {
    public static void main(String[] args) {
       // Cadena entrada = new Cadena("ab=(1+23+ad)*as+(4+ab+(1+as+rere));");

        //Cadena entrada = new Cadena("VAR = (CatA + ((CatA + CatB) * CatC)) * (CatD * CatF);");
        //Cadena entrada = new Cadena("var1 = (var2) = sa ;");
        Cadena entrada = new Cadena("AB = A*B/100-59;");
        AutomataPila automata = new AutomataPila(entrada);

        automata.q0();

    }
}
