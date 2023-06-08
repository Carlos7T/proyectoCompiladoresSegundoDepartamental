
package equipo.proyectocompiladores;

import java.util.List;

public class Parser {

    private final List<Token> tokens;


    private int i = 0;
    private boolean hayErrores = false;

    private Token preanalisis;

    public Parser(List<Token> tokens){
        this.tokens = tokens;
    }

   
    public void parse(){
        i = 0;
        preanalisis = tokens.get(i);
        PROGRAM();

        if(!hayErrores && preanalisis.tipo !=  TipoToken.EOF){
            System.out.println("Error en la posición " + preanalisis.linea + ". No se esperaba el token " + preanalisis.tipo);
        }
        else if(!hayErrores && preanalisis.tipo == TipoToken.EOF){
            System.out.println("Consulta válida");
        }

        /*if(!preanalisis.equals(finCadena)){
            System.out.println("Error en la posición " + preanalisis.posicion + ". No se esperaba el token " + preanalisis.tipo);
        }else if(!hayErrores){
            System.out.println("Consulta válida");
        }*/
    }

    private void PROGRAM(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.NEGACION ||
        preanalisis.tipo == TipoToken.MENOS || 
        preanalisis.tipo == TipoToken.VERDADERO || 
        preanalisis.tipo == TipoToken.FALSO || 
        preanalisis.tipo == TipoToken.NULO || 
        preanalisis.tipo == TipoToken.ESTE || 
        preanalisis.tipo == TipoToken.NUMERO || 
        preanalisis.tipo == TipoToken.CADENA ||
        preanalisis.tipo == TipoToken.IDENTIFICADOR || 
        preanalisis.tipo == TipoToken.PAREN_IZQ||
        preanalisis.tipo == TipoToken.SUPER ||
        preanalisis.tipo == TipoToken.PARA ||
        preanalisis.tipo == TipoToken.SI ||
        preanalisis.tipo == TipoToken.IMPRIMIR ||
        preanalisis.tipo == TipoToken.RETORNAR ||
        preanalisis.tipo == TipoToken.MIENTRAS ||
        preanalisis.tipo == TipoToken.LLAVE_IZQ ||
        preanalisis.tipo == TipoToken.CLASE ||
        preanalisis.tipo == TipoToken.FUN ||
        preanalisis.tipo == TipoToken.VAR){
            DECLARATION();
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un token de tipo 'class' , 'fun' , 'var' , 'if', 'while', 'for', 'return', '{', '!', '-', 'true', 'false', 'null', 'this', 'number', 'string', 'identifier', '(', 'super'");

        }
    }
    private void DECLARATION(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.CLASE){
            CLASS_DECL();
            DECLARATION();
        }
        else if(preanalisis.tipo == TipoToken.FUN){
            FUN_DECL();
            DECLARATION();
        }
        else if(preanalisis.tipo == TipoToken.VAR){
            VAR_DECL();
            DECLARATION();
        }
        else if(preanalisis.tipo == TipoToken.NEGACION ||
        preanalisis.tipo == TipoToken.MENOS || 
        preanalisis.tipo == TipoToken.VERDADERO || 
        preanalisis.tipo == TipoToken.FALSO || 
        preanalisis.tipo == TipoToken.NULO || 
        preanalisis.tipo == TipoToken.ESTE || 
        preanalisis.tipo == TipoToken.NUMERO || 
        preanalisis.tipo == TipoToken.CADENA ||
        preanalisis.tipo == TipoToken.IDENTIFICADOR || 
        preanalisis.tipo == TipoToken.PAREN_IZQ||
        preanalisis.tipo == TipoToken.SUPER ||
        preanalisis.tipo == TipoToken.PARA ||
        preanalisis.tipo == TipoToken.SI ||
        preanalisis.tipo == TipoToken.IMPRIMIR ||
        preanalisis.tipo == TipoToken.RETORNAR ||
        preanalisis.tipo == TipoToken.MIENTRAS ||
        preanalisis.tipo == TipoToken.LLAVE_IZQ){
            STATEMENT();
            DECLARATION();
        }
    }
    private void CLASS_DECL(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.CLASE){
            coincidir(preanalisis);
            if(preanalisis.tipo == TipoToken.IDENTIFICADOR){
                coincidir(preanalisis);
                CLASS_INHER();
                if(preanalisis.tipo == TipoToken.LLAVE_IZQ){
                    coincidir(preanalisis);
                    FUNCTIONS();
                    if(preanalisis.tipo == TipoToken.LLAVE_DER){
                        coincidir(preanalisis);
                    }
                    else{
                        hayErrores = true;
                        System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un '}'");
                    }
                }
                else{
                    hayErrores = true;
                    System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un '{'");
                }
            }
            else{
                hayErrores = true;
                System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un identificador");
            }
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un 'class'");
        }
    }
    private void CLASS_INHER(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.MENOR){
            coincidir(preanalisis);
            if(preanalisis.tipo == TipoToken.IDENTIFICADOR){
                coincidir(preanalisis);
            }
            else{
                hayErrores = true;
                System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un identificador");
            }
        }
    }
    private void FUN_DECL(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.FUN){
            coincidir(preanalisis);
            FUNCTION();
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un 'fun'");
        }
    }
    private void VAR_DECL(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.VAR){
            coincidir(preanalisis);
            if(preanalisis.tipo == TipoToken.IDENTIFICADOR){
                coincidir(preanalisis);
                VAR_INIT();
                if(preanalisis.tipo == TipoToken.PUNTO_Y_COMA){
                    coincidir(preanalisis);
                }
                else{
                    hayErrores = true;
                    System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un ';'");
                }
            }
            else{
                System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un identificador");
                hayErrores = true;
            }
        }
        else{
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un 'var''");
            hayErrores = true;
        }
    }
    private void VAR_INIT(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.IGUAL){
            coincidir(preanalisis);
            EXPRESSION();
        }
    }
    private void STATEMENT(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.NEGACION ||
        preanalisis.tipo == TipoToken.MENOS || 
        preanalisis.tipo == TipoToken.VERDADERO || 
        preanalisis.tipo == TipoToken.FALSO || 
        preanalisis.tipo == TipoToken.NULO || 
        preanalisis.tipo == TipoToken.ESTE || 
        preanalisis.tipo == TipoToken.NUMERO || 
        preanalisis.tipo == TipoToken.CADENA ||
        preanalisis.tipo == TipoToken.IDENTIFICADOR || 
        preanalisis.tipo == TipoToken.PAREN_IZQ||
        preanalisis.tipo == TipoToken.SUPER){
            EXPR_STMT();
        }
        else if(preanalisis.tipo == TipoToken.PARA){
            FOR_STMT();
        }
        else if(preanalisis.tipo == TipoToken.SI){
            IF_STMT();
        }
        else if(preanalisis.tipo == TipoToken.IMPRIMIR){
            PRINT_STMT();
        }
        else if(preanalisis.tipo == TipoToken.RETORNAR){
            RETURN_STMT();
        }
        else if(preanalisis.tipo == TipoToken.MIENTRAS){
            WHILE_STMT();
        }
        else if(preanalisis.tipo == TipoToken.LLAVE_IZQ){
            BLOCK();
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un token de tipo 'if', 'while', 'for', 'return', '{', '!', '-', 'true', 'false', 'null', 'this', 'number', 'string', 'identifier', '(', 'super'");
        }
    }
    private void EXPR_STMT(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.NEGACION ||
        preanalisis.tipo == TipoToken.MENOS || 
        preanalisis.tipo == TipoToken.VERDADERO || 
        preanalisis.tipo == TipoToken.FALSO || 
        preanalisis.tipo == TipoToken.NULO || 
        preanalisis.tipo == TipoToken.ESTE || 
        preanalisis.tipo == TipoToken.NUMERO || 
        preanalisis.tipo == TipoToken.CADENA ||
        preanalisis.tipo == TipoToken.IDENTIFICADOR || 
        preanalisis.tipo == TipoToken.PAREN_IZQ||
        preanalisis.tipo == TipoToken.SUPER){
            EXPRESSION();
            if(preanalisis.tipo == TipoToken.PUNTO_Y_COMA){
                coincidir(preanalisis);
            }
            else{
                hayErrores = true;
                System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un token de tipo ';'");
            }
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición" + preanalisis.linea + ". Se esperaba un '!', '-', 'true', 'false', 'null', 'this', 'number', 'string', 'identifier', '(', 'super'");
        }
    }
    private void FOR_STMT(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.PARA){
            coincidir(preanalisis);
            if(preanalisis.tipo == TipoToken.PAREN_IZQ){
                coincidir(preanalisis);
                FOR_STMT_1();
                FOR_STMT_2();
                FOR_STMT_3();
                if(preanalisis.tipo == TipoToken.PAREN_DER){
                    coincidir(preanalisis);
                }
                else{
                    hayErrores = true;
                    System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un token de tipo ')'");
                }
            }
            else{
                hayErrores = true;
                System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un token de tipo '('");
            }
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un token de tipo 'for'");
        }
    }
    private void FOR_STMT_1(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.VAR){
            VAR_DECL();
        }
        else if(preanalisis.tipo == TipoToken.NEGACION ||
        preanalisis.tipo == TipoToken.MENOS || 
        preanalisis.tipo == TipoToken.VERDADERO || 
        preanalisis.tipo == TipoToken.FALSO || 
        preanalisis.tipo == TipoToken.NULO || 
        preanalisis.tipo == TipoToken.ESTE || 
        preanalisis.tipo == TipoToken.NUMERO || 
        preanalisis.tipo == TipoToken.CADENA ||
        preanalisis.tipo == TipoToken.IDENTIFICADOR || 
        preanalisis.tipo == TipoToken.PAREN_IZQ||
        preanalisis.tipo == TipoToken.SUPER){
            EXPR_STMT();
        }
        else if(preanalisis.tipo == TipoToken.PUNTO_Y_COMA){
            coincidir(preanalisis);
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un token de tipo 'var', '!', '-', 'true', 'false', 'null', 'this', 'number', 'string', 'identifier', '(', 'super");
        }
    }
    private void FOR_STMT_2(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.NEGACION ||
        preanalisis.tipo == TipoToken.MENOS || 
        preanalisis.tipo == TipoToken.VERDADERO || 
        preanalisis.tipo == TipoToken.FALSO || 
        preanalisis.tipo == TipoToken.NULO || 
        preanalisis.tipo == TipoToken.ESTE || 
        preanalisis.tipo == TipoToken.NUMERO || 
        preanalisis.tipo == TipoToken.CADENA ||
        preanalisis.tipo == TipoToken.IDENTIFICADOR || 
        preanalisis.tipo == TipoToken.PAREN_IZQ||
        preanalisis.tipo == TipoToken.SUPER){
            EXPRESSION();
            if(preanalisis.tipo == TipoToken.PUNTO_Y_COMA){
                coincidir(preanalisis);
            }
            else{
                hayErrores = true;
                System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un token de tipo ';'");

            }
        }
        else if(preanalisis.tipo == TipoToken.PUNTO_Y_COMA){
            coincidir(preanalisis);
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un token de tipo '!', '-', 'verdadero', 'falso', 'nulo', 'este', 'numero', 'cadena', 'identificador', '(', 'super', ';'");
        }
    }
    private void FOR_STMT_3(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.NEGACION ||
        preanalisis.tipo == TipoToken.MENOS || 
        preanalisis.tipo == TipoToken.VERDADERO || 
        preanalisis.tipo == TipoToken.FALSO || 
        preanalisis.tipo == TipoToken.NULO || 
        preanalisis.tipo == TipoToken.ESTE || 
        preanalisis.tipo == TipoToken.NUMERO || 
        preanalisis.tipo == TipoToken.CADENA ||
        preanalisis.tipo == TipoToken.IDENTIFICADOR || 
        preanalisis.tipo == TipoToken.PAREN_IZQ||
        preanalisis.tipo == TipoToken.SUPER){
            EXPRESSION();
        }
    }
    private void IF_STMT(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.SI){
            coincidir(preanalisis);
            if(preanalisis.tipo == TipoToken.PAREN_IZQ){
                coincidir(preanalisis);
                EXPRESSION();
                if(preanalisis.tipo == TipoToken.PAREN_DER){
                    coincidir(preanalisis);
                    STATEMENT();
                    ELSE_STMT();
                }
                else{
                    hayErrores = true;
                    System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un token de tipo ')'");
                }
            }
            else{
                hayErrores = true;
                System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un token de tipo '('");
            }
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un token de tipo 'if'");
        }
    }
    private void ELSE_STMT(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.ADEMAS){
            coincidir(preanalisis);
            STATEMENT();
        }
    }
    private void PRINT_STMT(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.IMPRIMIR){
            coincidir(preanalisis);
            EXPRESSION();
            if(preanalisis.tipo == TipoToken.PUNTO_Y_COMA){
                coincidir(preanalisis);
            }
            else{
                hayErrores = true;
                System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un token de tipo ';'");
            }
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un token de tipo 'print'");
        }
    }
    private void RETURN_STMT(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.RETORNAR){
            coincidir(preanalisis);
            RETURN_EXP_OPC();
            if(preanalisis.tipo == TipoToken.PUNTO_Y_COMA){
                coincidir(preanalisis);
            }
            else{
                hayErrores = true;
                System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un token de tipo ';'");
            }
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un token de tipo 'return'");
        }
    }
    private void RETURN_EXP_OPC(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.NEGACION ||
        preanalisis.tipo == TipoToken.MENOS || 
        preanalisis.tipo == TipoToken.VERDADERO || 
        preanalisis.tipo == TipoToken.FALSO || 
        preanalisis.tipo == TipoToken.NULO || 
        preanalisis.tipo == TipoToken.ESTE || 
        preanalisis.tipo == TipoToken.NUMERO || 
        preanalisis.tipo == TipoToken.CADENA ||
        preanalisis.tipo == TipoToken.IDENTIFICADOR || 
        preanalisis.tipo == TipoToken.PAREN_IZQ||
        preanalisis.tipo == TipoToken.SUPER){
            EXPRESSION();
        }
    }
    private void WHILE_STMT(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.MIENTRAS){
            coincidir(preanalisis);
            if(preanalisis.tipo == TipoToken.PAREN_IZQ){
                coincidir(preanalisis);
                EXPRESSION();
                if(preanalisis.tipo == TipoToken.PAREN_DER){
                    coincidir(preanalisis);
                    STATEMENT();
                }
                else{
                    hayErrores = true;
                    System.out.println("Error en la posición " + preanalisis.linea + ". Se  esperaba el token ')'");
                }
            }
            else{
                hayErrores = true;
                System.out.println("Error en la posición " + preanalisis.linea + ". Se  esperaba el token '('");
            }
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se  esperaba el token 'while'");
        }
    }
    private void BLOCK(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.LLAVE_IZQ){
            coincidir(preanalisis);
            BLOCK_DECL();
            if(preanalisis.tipo == TipoToken.LLAVE_DER){
                coincidir(preanalisis);
            }
            else{
                hayErrores = true;
                System.out.println("Error en la posición " + preanalisis.linea + ". Se  esperaba el token '}'");
            }
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se  esperaba el token '{'");
        }
    }
    private void BLOCK_DECL(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.CLASE ||
        preanalisis.tipo == TipoToken.FUN ||
        preanalisis.tipo == TipoToken.VAR ||
        preanalisis.tipo == TipoToken.NEGACION ||
        preanalisis.tipo == TipoToken.MENOS || 
        preanalisis.tipo == TipoToken.VERDADERO || 
        preanalisis.tipo == TipoToken.FALSO || 
        preanalisis.tipo == TipoToken.NULO || 
        preanalisis.tipo == TipoToken.ESTE || 
        preanalisis.tipo == TipoToken.NUMERO || 
        preanalisis.tipo == TipoToken.CADENA ||
        preanalisis.tipo == TipoToken.IDENTIFICADOR || 
        preanalisis.tipo == TipoToken.PAREN_IZQ||
        preanalisis.tipo == TipoToken.SUPER ||
        preanalisis.tipo == TipoToken.PARA ||
        preanalisis.tipo == TipoToken.SI ||
        preanalisis.tipo == TipoToken.IMPRIMIR ||
        preanalisis.tipo == TipoToken.RETORNAR ||
        preanalisis.tipo == TipoToken.MIENTRAS ||
        preanalisis.tipo == TipoToken.LLAVE_IZQ){
            DECLARATION();
            BLOCK_DECL();
        }
    }
    private void EXPRESSION(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.NEGACION ||
        preanalisis.tipo == TipoToken.MENOS || 
        preanalisis.tipo == TipoToken.VERDADERO || 
        preanalisis.tipo == TipoToken.FALSO || 
        preanalisis.tipo == TipoToken.NULO || 
        preanalisis.tipo == TipoToken.ESTE || 
        preanalisis.tipo == TipoToken.NUMERO || 
        preanalisis.tipo == TipoToken.CADENA ||
        preanalisis.tipo == TipoToken.IDENTIFICADOR || 
        preanalisis.tipo == TipoToken.PAREN_IZQ||
        preanalisis.tipo == TipoToken.SUPER){
            ASSIGNMENT();
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición" + preanalisis.linea + ". Se esperaba un '!', '-', 'true', 'false', 'null', 'this', 'number', 'string', 'identifier', '(', 'super'");
        }
    }
    private void ASSIGNMENT(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.NEGACION ||
        preanalisis.tipo == TipoToken.MENOS || 
        preanalisis.tipo == TipoToken.VERDADERO || 
        preanalisis.tipo == TipoToken.FALSO || 
        preanalisis.tipo == TipoToken.NULO || 
        preanalisis.tipo == TipoToken.ESTE || 
        preanalisis.tipo == TipoToken.NUMERO || 
        preanalisis.tipo == TipoToken.CADENA ||
        preanalisis.tipo == TipoToken.IDENTIFICADOR || 
        preanalisis.tipo == TipoToken.PAREN_IZQ||
        preanalisis.tipo == TipoToken.SUPER){
            LOGIC_OR();
            ASSIGNMENT_OPC();
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición" + preanalisis.linea + ". Se esperaba un '!', '-', 'true', 'false', 'null', 'this', 'number', 'string', 'identifier', '(', 'super'");
        }
    }
    private void ASSIGNMENT_OPC(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.IGUAL){
            coincidir(preanalisis);
            EXPRESSION();
        }
    }
    private void LOGIC_OR(){
       if(hayErrores) return;

       if(preanalisis.tipo == TipoToken.NEGACION ||
       preanalisis.tipo == TipoToken.MENOS || 
       preanalisis.tipo == TipoToken.VERDADERO || 
       preanalisis.tipo == TipoToken.FALSO || 
       preanalisis.tipo == TipoToken.NULO || 
       preanalisis.tipo == TipoToken.ESTE || 
       preanalisis.tipo == TipoToken.NUMERO || 
       preanalisis.tipo == TipoToken.CADENA ||
       preanalisis.tipo == TipoToken.IDENTIFICADOR || 
       preanalisis.tipo == TipoToken.PAREN_IZQ||
       preanalisis.tipo == TipoToken.SUPER){
            LOGIC_AND();
            LOGIC_OR_2();
       }
       else{
            hayErrores = true;
            System.out.println("Error en la posición" + preanalisis.linea + ". Se esperaba un '!', '-', 'true', 'false', 'null', 'this', 'number', 'string', 'identifier', '(', 'super'");
       }
    }
    private void LOGIC_OR_2(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.O){
            coincidir(preanalisis);
            LOGIC_AND();
            LOGIC_OR_2();
        }
    }
    private void LOGIC_AND(){
        if(hayErrores) return;
        if(preanalisis.tipo == TipoToken.NEGACION ||
        preanalisis.tipo == TipoToken.MENOS || 
        preanalisis.tipo == TipoToken.VERDADERO || 
        preanalisis.tipo == TipoToken.FALSO || 
        preanalisis.tipo == TipoToken.NULO || 
        preanalisis.tipo == TipoToken.ESTE || 
        preanalisis.tipo == TipoToken.NUMERO || 
        preanalisis.tipo == TipoToken.CADENA ||
        preanalisis.tipo == TipoToken.IDENTIFICADOR || 
        preanalisis.tipo == TipoToken.PAREN_IZQ||
        preanalisis.tipo == TipoToken.SUPER){
            EQUALITY();
            LOGIC_AND_2();
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición" + preanalisis.linea + ". Se esperaba un '!', '-', 'true', 'false', 'null', 'this', 'number', 'string', 'identifier', '(', 'super'");
        }
    }
    private void LOGIC_AND_2(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.Y){
            coincidir(preanalisis);
            EQUALITY();
            LOGIC_AND_2();
        }
    }
    private void EQUALITY(){
        if(hayErrores) return;
        if(preanalisis.tipo == TipoToken.NEGACION ||
        preanalisis.tipo == TipoToken.MENOS || 
        preanalisis.tipo == TipoToken.VERDADERO || 
        preanalisis.tipo == TipoToken.FALSO || 
        preanalisis.tipo == TipoToken.NULO || 
        preanalisis.tipo == TipoToken.ESTE || 
        preanalisis.tipo == TipoToken.NUMERO || 
        preanalisis.tipo == TipoToken.CADENA ||
        preanalisis.tipo == TipoToken.IDENTIFICADOR || 
        preanalisis.tipo == TipoToken.PAREN_IZQ||
        preanalisis.tipo == TipoToken.SUPER){
            COMPARISON();
            EQUALITY_2();
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición" + preanalisis.linea + ". Se esperaba un '!', '-', 'true', 'false', 'null', 'this', 'number', 'string', 'identifier', '(', 'super'");
        }
    }
    private void EQUALITY_2(){
        if(hayErrores) return;
        
        if(preanalisis.tipo == TipoToken.NO_IGUAL){
            coincidir(preanalisis);
            COMPARISON();
            EQUALITY_2();
        }
         
        else if(preanalisis.tipo == TipoToken.DOBLE_IGUAL){
            coincidir(preanalisis);
            COMPARISON();
            EQUALITY_2();
        }
    }
    private void COMPARISON(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.NEGACION ||
        preanalisis.tipo == TipoToken.MENOS || 
        preanalisis.tipo == TipoToken.VERDADERO || 
        preanalisis.tipo == TipoToken.FALSO || 
        preanalisis.tipo == TipoToken.NULO || 
        preanalisis.tipo == TipoToken.ESTE || 
        preanalisis.tipo == TipoToken.NUMERO || 
        preanalisis.tipo == TipoToken.CADENA ||
        preanalisis.tipo == TipoToken.IDENTIFICADOR || 
        preanalisis.tipo == TipoToken.PAREN_IZQ||
        preanalisis.tipo == TipoToken.SUPER){
            TERM();
            COMPARISON_2();
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición" + preanalisis.linea + ". Se esperaba un '!', '-', 'true', 'false', 'null', 'this', 'number', 'string', 'identifier', '(', 'super'");
        }
    }
    private void COMPARISON_2(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.MAYOR){
            coincidir(preanalisis);
            TERM();
            COMPARISON_2();
        }
        else if(preanalisis.tipo == TipoToken.MAYOR_IGUAL){
            coincidir(preanalisis);
            TERM();
            COMPARISON_2();
        }
        else if(preanalisis.tipo == TipoToken.MENOR){
            coincidir(preanalisis);
            TERM();
            COMPARISON_2();
        }
        else if(preanalisis.tipo == TipoToken.MENOR_IGUAL){
            coincidir(preanalisis);
            TERM();
            COMPARISON_2();
        }
    }
    private void TERM(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.NEGACION ||
        preanalisis.tipo == TipoToken.MENOS || 
        preanalisis.tipo == TipoToken.VERDADERO || 
        preanalisis.tipo == TipoToken.FALSO || 
        preanalisis.tipo == TipoToken.NULO || 
        preanalisis.tipo == TipoToken.ESTE || 
        preanalisis.tipo == TipoToken.NUMERO || 
        preanalisis.tipo == TipoToken.CADENA ||
        preanalisis.tipo == TipoToken.IDENTIFICADOR || 
        preanalisis.tipo == TipoToken.PAREN_IZQ||
        preanalisis.tipo == TipoToken.SUPER){
            FACTOR();
            TERM_2();
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición" + preanalisis.linea + ". Se esperaba un '!', '-', 'true', 'false', 'null', 'this', 'number', 'string', 'id', '(', 'super'");
        }
    }
    private void TERM_2(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.MENOS){
            coincidir(preanalisis);
            FACTOR();
            TERM_2();
        }
        else if(preanalisis.tipo == TipoToken.MAS){
            coincidir(preanalisis);
            FACTOR();
            TERM_2();
        }
    }
    private void FACTOR(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.NEGACION ||
        preanalisis.tipo == TipoToken.MENOS || 
        preanalisis.tipo == TipoToken.VERDADERO || 
        preanalisis.tipo == TipoToken.FALSO || 
        preanalisis.tipo == TipoToken.NULO || 
        preanalisis.tipo == TipoToken.ESTE || 
        preanalisis.tipo == TipoToken.NUMERO || 
        preanalisis.tipo == TipoToken.CADENA ||
        preanalisis.tipo == TipoToken.IDENTIFICADOR || 
        preanalisis.tipo == TipoToken.PAREN_IZQ||
        preanalisis.tipo == TipoToken.SUPER){
            UNARY();
            FACTOR_2();
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición" + preanalisis.linea + ". Se esperaba un '!', '-', 'true', 'false', 'null', 'this', 'number', 'string', 'id', '(', 'super'");
        }
    }
    private void FACTOR_2(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.DIVIDIDO){
            coincidir(preanalisis);
            UNARY();
            FACTOR_2();
        }
        else if(preanalisis.tipo == TipoToken.POR){
            coincidir(preanalisis);
            UNARY();
            FACTOR_2();
        }
    }
    private void UNARY(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.NEGACION){
            coincidir(preanalisis);
            UNARY();
        }
        else if(preanalisis.tipo == TipoToken.MENOS){
            coincidir(preanalisis);
            UNARY();
        }
        else if(preanalisis.tipo == TipoToken.VERDADERO || 
        preanalisis.tipo == TipoToken.FALSO || 
        preanalisis.tipo == TipoToken.NULO || 
        preanalisis.tipo == TipoToken.ESTE || 
        preanalisis.tipo == TipoToken.NUMERO || 
        preanalisis.tipo == TipoToken.CADENA ||
        preanalisis.tipo == TipoToken.IDENTIFICADOR || 
        preanalisis.tipo == TipoToken.PAREN_IZQ||
        preanalisis.tipo == TipoToken.SUPER){
            CALL();
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición" + preanalisis.linea + ". Se esperaba un '!', '-', 'true', 'false', 'null', 'this', 'number', 'string', 'id', '(' o 'super' ");
        }
    }
    private void CALL(){
        if(hayErrores) return;
        if(preanalisis.tipo == TipoToken.VERDADERO || 
        preanalisis.tipo == TipoToken.FALSO || 
        preanalisis.tipo == TipoToken.NULO || 
        preanalisis.tipo == TipoToken.ESTE || 
        preanalisis.tipo == TipoToken.NUMERO || 
        preanalisis.tipo == TipoToken.CADENA ||
        preanalisis.tipo == TipoToken.IDENTIFICADOR || 
        preanalisis.tipo == TipoToken.PAREN_IZQ||
        preanalisis.tipo == TipoToken.SUPER){
            PRIMARY();
            CALL_2();
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición" + preanalisis.linea + ". Se esperaba un 'true', 'false', 'null', 'this', 'number', 'string', 'id', '(', 'super'");
        }

    }
    private void CALL_2(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.PAREN_IZQ){
            coincidir(preanalisis);
            ARGUMENTS_OPC();
            if(preanalisis.tipo == TipoToken.PAREN_DER){
                coincidir(preanalisis);
                CALL_2();
            }
            else{
                hayErrores = true;
                System.out.println("Error en la posición" + preanalisis.linea + ". Se esperaba un parentesis derecho");
            }
        }
        else if(preanalisis.tipo == TipoToken.PUNTO){
            coincidir(preanalisis);
            if(preanalisis.tipo == TipoToken.IDENTIFICADOR){
                CALL_2();
            }
            else{
                hayErrores = true;
                System.out.println("Error en la posición" + preanalisis.linea + ". Se esperaba un identificador");
            }
        }
    }
    private void CAL_OPC(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.VERDADERO || 
        preanalisis.tipo == TipoToken.FALSO || 
        preanalisis.tipo == TipoToken.NULO || 
        preanalisis.tipo == TipoToken.ESTE || 
        preanalisis.tipo == TipoToken.NUMERO || 
        preanalisis.tipo == TipoToken.CADENA ||
        preanalisis.tipo == TipoToken.IDENTIFICADOR || 
        preanalisis.tipo == TipoToken.PAREN_IZQ||
        preanalisis.tipo == TipoToken.SUPER){
            CALL();
            if(preanalisis.tipo == TipoToken.PUNTO){
                coincidir(preanalisis);
            }
            else{
                hayErrores = true;
                System.out.println("Error en la posición" + preanalisis.linea + ". Se esperaba un punto");
            }
        }
        
    }
    private void PRIMARY(){
        if(hayErrores) return;
        if(preanalisis.tipo == TipoToken.VERDADERO){
            coincidir(preanalisis);
        }
        else if(preanalisis.tipo == TipoToken.FALSO){
            coincidir(preanalisis);
        }
        else if(preanalisis.tipo == TipoToken.NULO){
            coincidir(preanalisis);
        }
        else if(preanalisis.tipo == TipoToken.ESTE){
            coincidir(preanalisis);
        }
        else if(preanalisis.tipo == TipoToken.NUMERO){
            coincidir(preanalisis);
        }
        else if(preanalisis.tipo == TipoToken.CADENA){
            coincidir(preanalisis);
        }
        else if(preanalisis.tipo == TipoToken.IDENTIFICADOR){
            coincidir(preanalisis);
        }
        else if(preanalisis.tipo == TipoToken.PAREN_IZQ){
            coincidir(preanalisis);
            EXPRESSION();
            if(preanalisis.tipo == TipoToken.PAREN_DER){
                coincidir(preanalisis);
            }
            else{
                hayErrores = true;
                System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un parentesis derecho.");
            }
        }
        else if(preanalisis.tipo == TipoToken.SUPER){
            coincidir(preanalisis);
            if(preanalisis.tipo == TipoToken.PUNTO){
                coincidir(preanalisis);
                if(preanalisis.tipo == TipoToken.IDENTIFICADOR){
                    coincidir(preanalisis);
                }
                else{
                    hayErrores = true;
                    System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un identificador.");
                }
            }
            else{
                hayErrores = true;
                System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un punto.");
            }
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un 'true', 'false', 'null', 'this', 'number', 'string', 'id', '(' o 'super'.");
        }
    }
    private void FUNCTION(){
        if(hayErrores) return;
        if(preanalisis.tipo == TipoToken.IDENTIFICADOR){
            coincidir(preanalisis);
            if(preanalisis.tipo == TipoToken.PAREN_IZQ){
                coincidir(preanalisis);
                PARAMETERS_OPC();
                if(preanalisis.tipo == TipoToken.PAREN_DER){
                    coincidir(preanalisis);
                    BLOCK();
                }
                else{
                    hayErrores = true;
                    System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba parentesis derecho.");
                }
            }
            else{
                hayErrores = true;
                System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba parentesis izquierdo.");
            }
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba identificador.");
        }
    }
    private void FUNCTIONS(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.IDENTIFICADOR){
            FUNCTION();
            FUNCTIONS();
        }
    }
    private void PARAMETERS_OPC(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.IDENTIFICADOR){
            PARAMETERS();
        }
    }
    private void PARAMETERS(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.IDENTIFICADOR){
            coincidir(preanalisis);
            PARAMETERS_2();
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba identificador.");
        }
    } 
    private void PARAMETERS_2(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.COMA){
            coincidir(preanalisis);
            if(preanalisis.tipo == TipoToken.IDENTIFICADOR){
                coincidir(preanalisis);
                PARAMETERS_2();
            }
            else{
                hayErrores = true;
                System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba identificador.");
            }
        }
    }
    private void ARGUMENTS_OPC(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.NEGACION ||
        preanalisis.tipo == TipoToken.MENOS || 
        preanalisis.tipo == TipoToken.VERDADERO || 
        preanalisis.tipo == TipoToken.FALSO || 
        preanalisis.tipo == TipoToken.NULO || 
        preanalisis.tipo == TipoToken.ESTE || 
        preanalisis.tipo == TipoToken.NUMERO || 
        preanalisis.tipo == TipoToken.CADENA ||
        preanalisis.tipo == TipoToken.IDENTIFICADOR || 
        preanalisis.tipo == TipoToken.PAREN_IZQ||
        preanalisis.tipo == TipoToken.SUPER){
            ARGUMENTS();
        }
    }
    private void ARGUMENTS(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.MENOS || 
        preanalisis.tipo == TipoToken.VERDADERO || 
        preanalisis.tipo == TipoToken.NEGACION || 
        preanalisis.tipo == TipoToken.FALSO ||
        preanalisis.tipo == TipoToken.NULO || 
        preanalisis.tipo == TipoToken.ESTE || 
        preanalisis.tipo == TipoToken.NUMERO || 
        preanalisis.tipo == TipoToken.CADENA ||
        preanalisis.tipo == TipoToken.IDENTIFICADOR || 
        preanalisis.tipo == TipoToken.PAREN_IZQ||
        preanalisis.tipo == TipoToken.SUPER){
            EXPRESSION();
            ARGUMENTS_2();
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba DISTINCT, * o un identificador.");
        }
    }
    private void ARGUMENTS_2(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.COMA){
            coincidir(preanalisis);
            EXPRESSION();
            ARGUMENTS_2();
        }
        
    }

    


    void coincidir(Token t){
        if(hayErrores) return;

        if(preanalisis.tipo == t.tipo){
            i++;
            preanalisis = tokens.get(i);
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un  " + t.tipo);

        }
    }

}
