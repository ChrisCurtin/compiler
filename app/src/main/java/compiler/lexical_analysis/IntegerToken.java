package compiler.lexical_analysis;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class IntegerToken implements  Token {
    private int value;
}
