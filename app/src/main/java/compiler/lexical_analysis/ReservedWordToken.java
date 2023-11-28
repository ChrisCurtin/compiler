package compiler.lexical_analysis;

import compiler.common.ReservedWord;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReservedWordToken implements Token {
    ReservedWord reservedWord;
}
