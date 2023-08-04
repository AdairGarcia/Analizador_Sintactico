package test;

import arbol.Arbol;

import java.util.List;

public class TestArbol {
    public static void main(String[] args) {
        Arbol arbol = new Arbol();
        List<String> expresiones = arbol.crearArbol("a=b+8+7;");
        arbol.imprimirArbol(expresiones, 0);

    }
}
