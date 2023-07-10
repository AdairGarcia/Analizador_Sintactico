package presentacion;

import java.util.List;
import java.util.Scanner;

import arbol.Arbol;
import auxiliar.Cadena;
import automatapila.AutomataPila;

// Presentacion del automata de pila y el arbol de derivacion
public class AnalizadorSintactico {
    public static void main(String[] args) {
        System.out.println("""
                ---------------------
                Analizador sintactico
                Garcia Torres Adair
                Partida Flores Yael Alejandro
                4CM3
                ---------------------
                """);

        int opcion = -1;
        String entrada;
        Scanner sc = new Scanner(System.in);
        do{
            System.out.println("""
                    
                    1. Analizar linea de codigo
                    0. Salir
                    
                    """);
            System.out.print("Tu opcion: ");
            opcion = Integer.parseInt(sc.nextLine());

            switch (opcion){
                case 0:
                    System.out.println("Saliendo...");
                    break;

                case 1:
                    System.out.print("Ingresa la linea de codigo a analizar: ");
                    entrada = sc.nextLine();

                    Cadena linea = new Cadena(entrada);
                    AutomataPila automata = new AutomataPila(linea);
                    automata.q0();

                    System.out.println("""
                            
                                ---------------------
                                Arbol de derivacion
                                ---------------------
                                
                            """);

                    Arbol arbol = new Arbol();
                    List<String> expresiones = arbol.crearArbol(entrada);
                    arbol.imprimirArbol(expresiones, 0);
                    break;

                default:
                    System.out.println("Opcion no valida");
            }
        }while(opcion != 0);

    }
}
