package automatapila;

import auxiliar.Cadena;
import java.util.Stack;

// Automata de pila que permite generar cadenas que cumplan con la gramatica que describe identificadores de variables en c
public class AutomataPila {
    private Cadena entrada; // Cadena de entrada, es decir, la linea de codigo que se esta analizando
    private final Stack<Character> pila; // Pila de caracteres que nos ayudara a realizar el analisis sintactico

    public AutomataPila() {
        this.pila = new Stack<Character>();
        this.pila.push('Z'); //Simbolo inicial de la pila, unicamente sera eliminado si se recibe el caracter de fin de codigo ';'
    }
    public AutomataPila(Cadena entrada) {
        this();
        this.entrada = entrada;
    }

    // Manejaremos a los estados del automata, como si fueran funciones
    // de tal manera que el automata aceptara cadenas por pila vacia y por estado final
    // cadenas del tipo: identificador=expresion;
    // donde identificador es una cadena de letras, digitos y guion bajo
    // expresion es una cadena de letras, digitos, guion bajo, espacios, operadores y parentesis que cumpla con la gramatica de expresiones aritmeticas
    // los operadores solo tendremos +, -, *, / y %

    // Estado inicial del automata
    public void q0(){
        this.debug(0);

        if(!this.entrada.esVacia()){
            if((this.entrada.esLetra() || this.entrada.esGuionBajo()) && this.pila.peek() == 'Z'){
                this.entrada.siguienteCaracter();
                this.q1();
            } else if(this.entrada.esEspacio()){
                this.entrada.siguienteCaracter();
                this.q0();
            }
            else{
                System.out.println("Error de sintaxis");
            }
        }
        else{
            System.out.println("Error: La cadena de entrada esta vacia");
        }
    }

    // Estado que se encarga de leer el identificador y la asignacion
    public void q1(){
        this.debug(1);

        if(!this.entrada.esVacia()){
            if((this.entrada.esLetra() || this.entrada.esDigito() || this.entrada.esGuionBajo()) && this.pila.peek() == 'Z'){
                this.entrada.siguienteCaracter();
                this.q1();
            } else if(this.entrada.esAsignacion() && this.pila.peek() == 'Z'){
                this.entrada.siguienteCaracter();
                this.q2();
            } else if(this.entrada.esEspacio()){
                this.entrada.siguienteCaracter();
                this.q13();
            }
            else{
                System.out.println("Error de sintaxis");
            }
        } else {
            System.out.println("Error de sintaxis");
        }
    }
    // Estado principal que controla la gramatica de los estados
    public void q2(){
        this.debug(2);

        if(!this.entrada.esVacia()){
            if(this.entrada.esAbreParentesis() && (this.pila.peek() == 'Z' || this.pila.peek() == '(')){
                this.pila.push(this.entrada.getCaracter()); //Ingresamos el parentesis a la pila
                this.entrada.siguienteCaracter();
                this.q2();
            } else if((this.entrada.esLetra() || this.entrada.esGuionBajo()) && (this.pila.peek() == 'Z' || this.pila.peek() == '(')){
                this.entrada.siguienteCaracter();
                this.q3();
            } else if(this.entrada.esLetra() && esOperador(this.pila.peek())){
                // Unicamente si la pila no esta vacia
                if(!this.pila.empty()){
                    this.pila.pop(); // Eliminamos el operador de la pila
                    this.entrada.siguienteCaracter();
                    this.q3();
                } else {
                    System.out.println("Error de sintaxis");
                }
            } else if(this.entrada.esDigito() && (this.pila.peek() == 'Z' || this.pila.peek() == '(')){
                this.entrada.siguienteCaracter();
                this.q4();
            } else if(this.entrada.esDigito() && esOperador(this.pila.peek())){
                if(!this.pila.empty()){
                    this.pila.pop(); // Eliminamos el operador de la pila
                    this.entrada.siguienteCaracter();
                    this.q4();
                } else {
                    System.out.println("Error de sintaxis");
                }
            } else if(this.entrada.esAbreParentesis() && esOperador(this.pila.peek())){
                if(!this.pila.empty()){
                    this.pila.pop(); // Eliminamos el operador de la pila
                    this.pila.push(this.entrada.getCaracter());
                    this.entrada.siguienteCaracter();
                    this.q3();
                } else {
                    System.out.println("Error de sintaxis");
                }
            } else if(this.entrada.esEspacio()){
                this.entrada.siguienteCaracter();
                this.q2();
            }
            else {
                System.out.println("Error de sintaxis");
            }
        } else {
            System.out.println("Error de sintaxis");
        }
    }

    // Este estado se encarga de leer los identificadores de la gramatica de expresiones
    public void q3(){
        this.debug(3);

        if(!this.entrada.esVacia()){
            if((this.entrada.esLetra() || this.entrada.esDigito() || this.entrada.esGuionBajo()) && (this.pila.peek() == 'Z' || this.pila.peek() == '(')){
                this.entrada.siguienteCaracter();
                this.q3();
            } else if(this.entrada.esCierraParentesis()){
                if(!this.pila.empty()){
                    this.pila.pop(); // Eliminamos el parentesis de la pila
                    this.entrada.siguienteCaracter();
                    this.q3();
                } else {
                    System.out.println("Error de sintaxis");
                }
            } else if(this.entrada.esOperador()){
                this.pila.push(this.entrada.getCaracter()); //Ingresamos el operador a la pila
                this.entrada.siguienteCaracter();
                this.q5();
            } else if(this.entrada.esPuntoComa() && this.pila.peek() == 'Z'){
                this.entrada.siguienteCaracter();
                this.q9();
            } else if(this.entrada.esAsignacion()){
                this.entrada.siguienteCaracter();
                this.q10(); // Estados adicionales para la gramatica de asignacion
            } else if(this.entrada.esEspacio()){
                this.entrada.siguienteCaracter();
                this.q17();
            }
            else {
                System.out.println("Error de sintaxis");
            }

        } else {
            System.out.println("Error de sintaxis");
        }
    }

    // Este estado se encarga de leer los digitos de la gramatica de expresiones
    public void q4(){
        this.debug(4);

        if(!this.entrada.esVacia()){
            if(this.entrada.esDigito() && (this.pila.peek() == 'Z' || this.pila.peek() == '(')){
                this.entrada.siguienteCaracter();
                this.q4();
            } else if(this.entrada.esCierraParentesis()) {
                if(!this.pila.empty()){
                    this.pila.pop(); // Eliminamos el parentesis de la pila
                    this.entrada.siguienteCaracter();
                    this.q4();
                } else {
                    System.out.println("Error de sintaxis");
                }
            } else if(this.entrada.esOperador()){
                this.pila.push(this.entrada.getCaracter()); //Ingresamos el operador a la pila
                this.entrada.siguienteCaracter();
                this.q5();
            } else if(this.entrada.esPuntoComa() && this.pila.peek() == 'Z'){
                this.entrada.siguienteCaracter();
                this.q9();
            } else if(this.entrada.esEspacio()){
                this.entrada.siguienteCaracter();
                this.q14();
            }
            else {
                System.out.println("Error de sintaxis");
            }

        } else {
            System.out.println("Error de sintaxis");
        }
    }

    // Este estado se encarga de leer los operadores de la gramatica de expresiones
    public void q5(){
        this.debug(5);

        if(!this.entrada.esVacia()){
            if(this.entrada.esLetra() || this.entrada.esGuionBajo()){
                if(!this.pila.empty()){
                    this.pila.pop(); // Eliminamos el operador de la pila
                    this.entrada.siguienteCaracter();
                    this.q6();
                } else {
                    System.out.println("Error de sintaxis");
                }

            } else if(this.entrada.esDigito()){
                if(!this.pila.empty()){
                    this.pila.pop(); // Eliminamos el operador de la pila
                    this.entrada.siguienteCaracter();
                    this.q7();
                } else {
                    System.out.println("Error de sintaxis");
                }
            } else if(this.entrada.esAbreParentesis() && esOperador(this.pila.peek())){
                if(!this.pila.empty()){
                    this.pila.pop(); // Eliminamos el operador de la pila
                    this.pila.push(this.entrada.getCaracter()); //Ingresamos el parentesis a la pila
                    this.entrada.siguienteCaracter();
                    this.q2();
                } else {
                    System.out.println("Error de sintaxis");
                }
            } else if(this.entrada.esEspacio()){;
                this.entrada.siguienteCaracter();
                this.q5(); // en este estado podemos ignorar espacios en blanco
            }
            else {
                System.out.println("Error de sintaxis");
            }

        } else {
            System.out.println("Error de sintaxis");
        }
    }

    // Este estado se encarga de leer los identificadores de la gramatica de expresiones con operador
    public void q6(){
        this.debug(6);

        if(!this.entrada.esVacia()){
            if((this.entrada.esLetra() || this.entrada.esDigito() || this.entrada.esGuionBajo()) && (this.pila.peek() == 'Z' || this.pila.peek() == '(')){
                this.entrada.siguienteCaracter();
                this.q6();
            } else if(this.entrada.esCierraParentesis() && this.pila.peek() == '('){
                if(!this.pila.empty()){
                    this.pila.pop(); // Eliminamos el parentesis de la pila
                    this.entrada.siguienteCaracter();
                    this.q8();
                } else {
                    System.out.println("Error de sintaxis");
                }
            } else if(this.entrada.esOperador()){
                this.pila.push(this.entrada.getCaracter()); //Ingresamos el operador a la pila
                this.entrada.siguienteCaracter();
                this.q5();
            } else if(this.entrada.esPuntoComa() && this.pila.peek() == 'Z'){
                this.entrada.siguienteCaracter();
                this.q9();
            } else if(this.entrada.esEspacio()){
                this.entrada.siguienteCaracter();
                this.q14();
            }
            else {
                System.out.println("Error de sintaxis");
            }

        } else {
            System.out.println("Error de sintaxis");
        }
    }

    // Este estado se encarga de leer los digitos de la gramatica de expresiones con operador
    public void q7(){
        this.debug(7);

        if(!this.entrada.esVacia()){
            if(this.entrada.esDigito() && (this.pila.peek() == 'Z' || this.pila.peek() == '(')){
                this.entrada.siguienteCaracter();
                this.q7();
            } else if(this.entrada.esCierraParentesis() && this.pila.peek() == '('){
                if(!this.pila.empty()){
                    this.pila.pop(); // Eliminamos el parentesis de la pila
                    this.entrada.siguienteCaracter();
                    this.q8();
                } else {
                    System.out.println("Error de sintaxis");
                }
            } else if(this.entrada.esOperador()){
                this.pila.push(this.entrada.getCaracter()); //Ingresamos el operador a la pila
                this.entrada.siguienteCaracter();
                this.q5();
            } else if(this.entrada.esPuntoComa() && this.pila.peek() == 'Z'){
                this.entrada.siguienteCaracter();
                this.q9();
            } else if(this.entrada.esEspacio()){
                this.entrada.siguienteCaracter();
                this.q15();
            }
            else {
                System.out.println("Error de sintaxis");
            }

        } else {
            System.out.println("Error de sintaxis");
        }
    }

    // Este estado ha completado una expresion y permite generar otra expresion o no
    public void q8(){
        this.debug(8);

        if(!this.entrada.esVacia()){
            if(this.entrada.esCierraParentesis() && this.pila.peek() == '('){
                if(!this.pila.empty()){
                    this.pila.pop();
                    this.entrada.siguienteCaracter();
                    this.q8();
                } else {
                    System.out.println("Error de sintaxis");
                }
            } else if(this.entrada.esOperador()){
                this.pila.push(this.entrada.getCaracter()); //Ingresamos el operador a la pila
                this.entrada.siguienteCaracter();
                this.q2();
            } else if(this.entrada.esPuntoComa() && this.pila.peek() == 'Z'){
                this.entrada.siguienteCaracter();
                this.q9();
            } else if(this.entrada.esEspacio()){
                this.entrada.siguienteCaracter();
                this.q8();  // en este estado podemos ignorar espacios en blanco
            }
            else {
                System.out.println("Error de sintaxis");
            }

        } else {
            System.out.println("Error de sintaxis");
        }
    }

    // Este es el estado de aceptacion y averriguara si la expresion es correcta o no
    public void q9(){
        this.debug(9);

        if(!this.pila.empty() && this.pila.peek() == 'Z'){
            this.pila.pop();
        } else {
            System.out.println("""
                    ********
                    Error de sintaxis
                    ********
                    """);
        }

        if(this.entrada.esVacia() && this.pila.empty()){
            System.out.println("""
                    ********
                    La expresion es correcta
                    ********
                    """);
        } else {
            System.out.println("Error de sintaxis");
        }
    }

    /*
     * Estados adicionales que permitiran manejar asignaciones multiples
     * tal que a = b = c = 1;
     */

    // Este es el estado principal de la gramatica de asignaciones multiples
    public void q10(){
        this.debug(10);

        if(!this.entrada.esVacia()){
            if((this.entrada.esLetra() || this.entrada.esGuionBajo()) && (this.pila.peek() == 'Z' || this.pila.peek() == '(')){
                this.entrada.siguienteCaracter();
                this.q11();
            } else if(this.entrada.esAbreParentesis()){
                this.pila.push(this.entrada.getCaracter()); //Ingresamos el parentesis a la pila
                this.entrada.siguienteCaracter();
                this.q10();
            } else if(this.entrada.esDigito()){
                this.entrada.siguienteCaracter();
                this.q12();
            } else if(this.entrada.esEspacio()){
                this.entrada.siguienteCaracter();
                this.q10(); // en este estado podemos ignorar espacios en blanco
            }
            else {
                System.out.println("Error de sintaxis");
            }
        } else {
            System.out.println("Error de sintaxis");
        }
    }

    // Este estado se encarga de leer las letras de la gramatica de asignaciones multiples
    public void q11(){
        this.debug(11);

        if(!this.entrada.esVacia()){
            if((this.entrada.esLetra() || this.entrada.esDigito() || this.entrada.esGuionBajo()) && (this.pila.peek() == 'Z' || this.pila.peek() == '(')){
                this.entrada.siguienteCaracter();
                this.q11();
            } else if(this.entrada.esCierraParentesis() && this.pila.peek() == '('){
                if(!this.pila.empty()){
                    this.pila.pop(); // Eliminamos el parentesis de la pila
                    this.entrada.siguienteCaracter();
                    this.q11();
                } else {
                    System.out.println("Error de sintaxis");
                }
            } else if(this.entrada.esAsignacion()){
                this.entrada.siguienteCaracter();
                this.q10();
            } else if(this.entrada.esPuntoComa() && this.pila.peek() == 'Z'){
                this.entrada.siguienteCaracter();
                this.q9();
            } else if(this.entrada.esEspacio()){
                this.entrada.siguienteCaracter();
                this.q16(); // en este estado podemos ignorar espacios en blanco
            }
            else {
                System.out.println("Error de sintaxis");
            }
        } else {
            System.out.println("Error de sintaxis");
        }
    }

    // Este estado se encarga de leer los digitos de la gramatica de asignaciones multiples
    public void q12(){
        this.debug(12);

        if(!this.entrada.esVacia()){
            if(this.entrada.esDigito() && (this.pila.peek() == 'Z' || this.pila.peek() == '(')){
                this.entrada.siguienteCaracter();
                this.q12();
            } else if(this.entrada.esCierraParentesis() && this.pila.peek() == '('){
                if(!this.pila.empty()){
                    this.pila.pop(); // Eliminamos el parentesis de la pila
                    this.entrada.siguienteCaracter();
                    this.q12();
                } else {
                    System.out.println("Error de sintaxis");
                }
            } else if(this.entrada.esPuntoComa() && this.pila.peek() == 'Z'){
                this.entrada.siguienteCaracter();
                this.q9();
            } else if(this.entrada.esEspacio()){
                this.entrada.siguienteCaracter();
                this.q15(); // en este estado podemos ignorar espacios en blanco
            }
            else {
                System.out.println("Error de sintaxis");
            }
        } else {
            System.out.println("Error de sintaxis");
        }
    }

    /* Estados de los espacios en blanco */
    public void q13(){
        this.debug(13);
        if(!this.entrada.esVacia()){
            if(this.entrada.esEspacio()){
                this.entrada.siguienteCaracter();
                this.q13();
            } else if(this.entrada.esAsignacion()){
                this.entrada.siguienteCaracter();
                this.q2();
            } else {
                System.out.println("Error de sintaxis");
            }
        } else {
            System.out.println("Error de sintaxis");
        }
    }

    // Estado encargado de leer los espacios en blanco de identificadores y numeros
    public void q14(){
        this.debug(14);
        if(!this.entrada.esVacia()){
            if(this.entrada.esEspacio()){
                this.entrada.siguienteCaracter();
                this.q14();
            } else if(this.entrada.esCierraParentesis() && this.pila.peek() == '('){
                if(!this.pila.empty()){
                    this.pila.pop(); // Eliminamos el parentesis de la pila
                    this.entrada.siguienteCaracter();
                    this.q14();
                } else {
                    System.out.println("Error de sintaxis");
                }
            } else if(this.entrada.esPuntoComa() && this.pila.peek() == 'Z'){
                this.entrada.siguienteCaracter();
                this.q9();
            } else if(this.entrada.esOperador() && this.pila.peek() != 'A'){
                this.pila.push(this.entrada.getCaracter());
                this.entrada.siguienteCaracter();
                this.q5();
            } else if(this.entrada.esAsignacion() && this.pila.peek() == 'A'){
                this.pila.pop(); // Eliminamos el A de asignacion de la pila
                this.entrada.siguienteCaracter();
                this.q10();
            }
            else {
                System.out.println("Error de sintaxis");
                }
            } else {
            System.out.println("Error de sintaxis");
        }
    }

    // Estado encargado de leer los espacios en blanco de las operaciones
    public void q15(){
        this.debug(15);
        if(!this.entrada.esVacia()){
            if(this.entrada.esEspacio()){
                this.entrada.siguienteCaracter();
                this.q15();
            } else if(this.entrada.esCierraParentesis() && this.pila.peek() == '('){
                if(!this.pila.empty()){
                    this.pila.pop();
                    this.entrada.siguienteCaracter();
                    this.q15();
                } else {
                    System.out.println("Error de sintaxis");
                }
            } else if(this.entrada.esOperador()){
                this.pila.push(this.entrada.getCaracter());
                this.entrada.siguienteCaracter();
                this.q2();
            } else if(this.entrada.esPuntoComa() && this.pila.peek() == 'Z'){
                this.entrada.siguienteCaracter();
                this.q9();
            }
            else {
                System.out.println("Error de sintaxis");
            }
        } else {
            System.out.println("Error de sintaxis");
        }
    }

    public void q16(){
        this.debug(16);
        if(!this.entrada.esVacia()){
            if(this.entrada.esEspacio()){
                this.entrada.siguienteCaracter();
                this.q16();
            } else if(this.entrada.esCierraParentesis() && this.pila.peek() == '('){
                this.pila.pop();
                this.entrada.siguienteCaracter();
                this.q16();
            } else if(this.entrada.esAsignacion()){
                this.entrada.siguienteCaracter();
                this.q10();
            } else if(this.entrada.esPuntoComa() && this.pila.peek() == 'Z'){
                this.entrada.siguienteCaracter();
                this.q9();
            }
            else {
                System.out.println("Error de sintaxis");
            }
        } else {
            System.out.println("Error de sintaxis");
        }
    }

    public void q17(){
        if(!this.entrada.esVacia()){
            if(this.entrada.esEspacio()){
                this.entrada.siguienteCaracter();
                this.q17();
            } else if(this.entrada.esOperador()){
                this.pila.push(this.entrada.getCaracter());
                this.entrada.siguienteCaracter();
                this.q5();
            } else if(this.entrada.esCierraParentesis() && this.pila.peek() == '('){
                this.pila.pop();
                this.entrada.siguienteCaracter();
                this.q17();
            } else if(this.entrada.esAsignacion()){
                this.entrada.siguienteCaracter();
                this.q10();
            } else if(this.entrada.esPuntoComa() && this.pila.peek() == 'Z'){
                this.entrada.siguienteCaracter();
                this.q9();
            }
            else {
                System.out.println("Error de sintaxis");
            }
        } else {
            System.out.println("Error de sintaxis");
        }
    }


    public boolean esOperador(char cima){
        return cima == '+' ||
                cima == '-' ||
                cima == '*' ||
                cima == '/' ||
                cima == '%';
    }

    public void debug(int estado){
        System.out.println("-------------------------------------------------");

        System.out.println("Estado: " + estado);
        System.out.println("Entrada: " + this.entrada.getEntrada());
        System.out.println("Pila: " + this.pila.toString());

        System.out.println("-------------------------------------------------");
    }

}
