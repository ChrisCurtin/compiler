package compiler.lexical_analysis;

import compiler.common.Punctuation;

public record PunctuationToken(Punctuation punctuation) implements Token {
}
