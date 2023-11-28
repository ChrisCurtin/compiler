package compiler.lexical_analysis;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LexicalError implements Token {
    String token;
    int lineNumber;
    String errorReason;
}
