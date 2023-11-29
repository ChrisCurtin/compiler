package compiler.common;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public enum Punctuation {
    COMMA(","), SEMICOLON(";"), PERIOD("."), COLON(":"), LPAREN("("), RPAREN(")"),
    LBRACE("{"), RBRACE("}"), LBRACKET("["), RBRACKET("]"),
    PLUS("+"), MINUS("-"), TIMES("*"), DIV("/"), MOD("%"),
    ASSIGNMENT("=", true), NOT("!", true),
    LOGICAND("&&"), LOGICOR("||"), BITAND("|", true),
    EQUAL("=="), LESS("<", true), LESSEQUAL("<="), GREATER(">", true), GREATEREQUAL(">="), NOTEQUAL("!=");

    private final String name;
    @Getter
    private final boolean canBeTwoCharacters;

    Punctuation(String name) {
        this.name = name;
        this.canBeTwoCharacters = false;
    }

    Punctuation(String name, boolean canBeTwoCharacters) {
        this.name = name;
        this.canBeTwoCharacters = canBeTwoCharacters;
    }

    private static final Map<String, Punctuation> BY_LABEL = new HashMap<>();

    static {
        for (Punctuation e : values()) {
            BY_LABEL.put(e.name, e);
        }
    }

    public static Punctuation toPunctuation(String label) {
        return BY_LABEL.get(label);
    }

    public static Punctuation combinePunctuation(Punctuation left, Punctuation right) {
        return toPunctuation(left.name + right.name);
    }
}
