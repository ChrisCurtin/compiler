package compiler.lexical_analysis;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class IdentifierToken implements Token {
    private String identifierName;
}
