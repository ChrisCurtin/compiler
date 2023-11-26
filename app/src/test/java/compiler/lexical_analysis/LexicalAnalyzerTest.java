package compiler.lexical_analysis;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LexicalAnalyzerTest {

    @Test
    public void givenEmptyLine_then_returnNoToken() {
        LexicalAnalyzer analyzer = new LexicalAnalyzer();
        Token result = analyzer.nextToken();
        assertInstanceOf(NoToken.class, result);
    }

}