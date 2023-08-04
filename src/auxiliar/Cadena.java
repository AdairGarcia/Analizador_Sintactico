package auxiliar;

// Clase que nos permitira manejar las cadenas de entrada
public class Cadena {
    private String entrada;

    public Cadena(String entrada){
        //this.entrada = entrada.replaceAll("\\s+", "");
        this.entrada = entrada;
    }

    // Metodo para saber si la cadena de entrada esta vacia
    public boolean esVacia(){
        return this.entrada.length() == 0;
    }

    // Metodo que elimina el primer caracter de la cadena de entrada
    public void siguienteCaracter(){
        if(!this.esVacia()){
            this.entrada = this.entrada.substring(1);
        }
        else{
            this.entrada = "";
        }
    }

    // Metodos que nos diran que tipo de caracter es el que se esta leyendo
    public boolean esLetra(){
        return Character.isLetter(this.entrada.charAt(0));
    }

    public boolean esDigito(){
        return Character.isDigit(this.entrada.charAt(0));
    }

    public boolean esGuionBajo(){
        return this.entrada.charAt(0) == '_';
    }

    public boolean esEspacio(){
        return this.entrada.charAt(0) == ' ';
    }


    public boolean esPuntoComa(){
        return this.entrada.charAt(0) == ';';
    }

    public boolean esOperador(){
        return this.entrada.charAt(0) == '+' ||
                this.entrada.charAt(0) == '-' ||
                this.entrada.charAt(0) == '*' ||
                this.entrada.charAt(0) == '/' ||
                this.entrada.charAt(0) == '%';
    }

    public boolean esAsignacion(){
        return this.entrada.charAt(0) == '=';
    }

    public boolean esAbreParentesis(){
        return this.entrada.charAt(0) == '(';
    }

    public boolean esCierraParentesis(){
        return this.entrada.charAt(0) == ')';
    }

    public char getCaracter(){
        return this.entrada.charAt(0);
    }

    public String getEntrada(){
        return this.entrada;
    }

}
