package compiler.lexical_analysis;

import compiler.common.ReservedWord;

public record ReservedWordToken(ReservedWord reservedWord) implements Token {
}
