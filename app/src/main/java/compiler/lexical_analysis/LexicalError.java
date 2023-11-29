package compiler.lexical_analysis;

public record LexicalError(String token, int lineNumber, String errorReason) implements Token {
}
