package compiler.common;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum ReservedWord {
    IF("if"),
    ELSE("else"),
    INT("int");


    private final String name;
    ReservedWord(String name) {
        this.name = name;
    }
    private static final Map<String, ReservedWord> BY_LABEL = new HashMap<>();
    static {
        for (ReservedWord e: values()) {
            BY_LABEL.put(e.name, e);
        }
    }
    public static ReservedWord toReservedWord(String label) {
        return BY_LABEL.get(label);
    }
}
