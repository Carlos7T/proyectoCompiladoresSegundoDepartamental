
package equipo.proyectocompiladores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;


public class Scanner {
  private final String source;
  private final List<Token> tokens = new ArrayList<>();

  private int start = 0;
  private int current = 0;
  private int line = 1;

  private static final Map<String, TipoToken> palabrasReservadas;
  static {
      palabrasReservadas = new HashMap<>();
      palabrasReservadas.put("y", TipoToken.Y);
      palabrasReservadas.put("clase", TipoToken.CLASE);
      palabrasReservadas.put("ademas", TipoToken.ADEMAS);
      palabrasReservadas.put("falso", TipoToken.FALSO);
      palabrasReservadas.put("para", TipoToken.PARA);
      palabrasReservadas.put("fun", TipoToken.FUN); //definir funciones
      palabrasReservadas.put("si", TipoToken.SI);
      palabrasReservadas.put("nulo", TipoToken.NULO);
      palabrasReservadas.put("o", TipoToken.O);
      palabrasReservadas.put("imprimir", TipoToken.IMPRIMIR);
      palabrasReservadas.put("retornar", TipoToken.RETORNAR);
      palabrasReservadas.put("super", TipoToken.SUPER);
      palabrasReservadas.put("este", TipoToken.ESTE);
      palabrasReservadas.put("verdadero", TipoToken.VERDADERO);
      palabrasReservadas.put("var", TipoToken.VAR); //definir variables
      palabrasReservadas.put("mientras", TipoToken.MIENTRAS);
  }


  public Scanner(String source) {
    this.source = source;
  }

  public List<Token> scanTokens() {
    while (!isAtEnd()) {
      start = current;
      scanToken();
    }

    tokens.add(new Token(TipoToken.EOF, "", null, line));
    return tokens;
  }

  private void addToken(TipoToken tipo) {
    addToken(tipo, null);
  }

  private void addToken(TipoToken tipo, Object literal) {
    String lexema = source.substring(start, current);
    tokens.add(new Token(tipo, lexema, literal, line));
  }

  private void scanToken() {
    char c = advance();

    switch (c) {
        
      case '(':
        addToken(TipoToken.PAREN_IZQ);
        break;
      case ')':
        addToken(TipoToken.PAREN_DER);
        break;
      case '{':
        addToken(TipoToken.LLAVE_IZQ);
        break;
      case '}':
        addToken(TipoToken.LLAVE_DER);
        break;
      case ',':
        addToken(TipoToken.COMA);
        break;
      case '.':
        addToken(TipoToken.PUNTO);
        break;
      case '-':
        addToken(TipoToken.MENOS);
        break;
      case '+':
        addToken(TipoToken.MAS);
        break;
      case ';':
        addToken(TipoToken.PUNTO_Y_COMA);
        break;
      case '*':
        addToken(TipoToken.POR);
        break;
      case '!':
        addToken(match('=') ? TipoToken.NO_IGUAL : TipoToken.NEGACION);
        break;
      case '=':
        addToken(match('=') ? TipoToken.DOBLE_IGUAL : TipoToken.IGUAL);
        break;
      case '<':
        addToken(match('=') ? TipoToken.MENOR_IGUAL : TipoToken.MENOR);
        break;
      case '>':
        addToken(match('=') ? TipoToken.MAYOR_IGUAL : TipoToken.MAYOR);
        break;    
      case ' ':
      case '\r':
      case '\t':
        break;
      case '\n':
        line++;
        break; 
      case '"':
        string();
        break;

      default:
        if (isDigit(c)) {
          number();
        } else if (isAlpha(c)) {
          identifier();
        } else {
            Interprete.error(line, "Unexpected character.");
        }
        break;
    }
  }

  private void identifier() {
    while (isAlphaNumeric(peek())) {
      advance();
    }

    String text = source.substring(start, current);

    TipoToken type = TipoToken.IDENTIFICADOR;
    if (palabrasReservadas.containsKey(text)) {
      type = palabrasReservadas.get(text);
    }

    addToken(type);
  }

  private void number() {
    while (isDigit(peek())) {
      advance();
    }

    
    if (peek() == '.' && isDigit(peekNext())) {
      
      advance();

      while (isDigit(peek())) {
        advance();
      }
    }

    addToken(TipoToken.NUMERO, Double.parseDouble(source.substring(start, current)));
  }

  private void string() {
    while (peek() != '"' && !isAtEnd()) {
      if (peek() == '\n') {
        line++;
      }
      advance();
    }

    
    if (isAtEnd()) {
        Interprete.error(line, "Unterminated string.");
      return;
    }

    
    advance();

    
    String value = source.substring(start + 1, current - 1);
    addToken(TipoToken.CADENA, value);
  }

  private boolean isAtEnd() {
    return current >= source.length();
  }
  private char advance() {
    current++;
    return source.charAt(current - 1);
  }

  private boolean match(char expected) {
    if (isAtEnd()) return false;
    if (source.charAt(current) != expected) return false;

    current++;
    return true;
  }

  private char peek() {
    if (isAtEnd()) return '\0';
    return source.charAt(current);
  }

  private boolean isDigit(char c) {
    return c >= '0' && c <= '9';
  }

  private char peekNext() {
    if (current + 1 >= source.length()) return '\0';
    return source.charAt(current + 1);
  }

  private boolean isAlphaNumeric(char c) {
    return isDigit(c) || isAlpha(c);
  }

  private boolean isAlpha(char c) {
    return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
  }

}