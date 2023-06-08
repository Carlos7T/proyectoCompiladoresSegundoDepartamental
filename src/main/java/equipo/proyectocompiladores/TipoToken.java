/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package equipo.proyectocompiladores;

public enum TipoToken {
    // Crear un tipoToken por palabra reservada
    // Crear un tipoToken: identificador, una cadena y numero
    // Crear un tipoToken por cada "Signo del lenguaje" (ver clase Scanner)


    // Palabras clave
    Y,
    CLASE,
    ADEMAS,
    FALSO,
    PARA,
    FUN,
    SI,
    NULO,
    O,
    IMPRIMIR,
    RETORNAR,
    SUPER,
    ESTE,
    VERDADERO,
    VAR,
    MIENTRAS,

    // Signos del lenguaje
    PAREN_IZQ,
    PAREN_DER,
    LLAVE_IZQ,
    LLAVE_DER,
    COMA,
    PUNTO,
    PUNTO_Y_COMA,
    MENOS,
    MAS,
    POR,
    DIVIDIDO,
    NEGACION,
    IGUAL,
    NO_IGUAL,
    MENOR,
    MENOR_IGUAL,
    MAYOR,
    MAYOR_IGUAL,
    DOBLE_IGUAL,

    // Identificador, cadena y número
    IDENTIFICADOR,
    CADENA,
    NUMERO,

    // Final de cadena
    EOF
}
