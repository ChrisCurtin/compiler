package compiler.lexical_analysis;


import compiler.common.ReservedWord;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LexicalAnalyzerTest {

    @Test
    public void givenEmptyLine_then_returnNoToken() {
        SourceFile sourceFile = mockSourceFile("source.java");
        doReturn("", null).when(sourceFile).nextLine();
        LexicalAnalyzer analyzer = new LexicalAnalyzer(sourceFile);

        Token result = analyzer.nextToken();
        assertInstanceOf(NoToken.class, result);
    }
    @Test
    public void givenSpacesOnly_then_returnNoToken() {
        SourceFile sourceFile = mockSourceFile("source.java");
        doReturn("    ", null).when(sourceFile).nextLine();
        LexicalAnalyzer analyzer = new LexicalAnalyzer(sourceFile);

        Token result = analyzer.nextToken();
        assertInstanceOf(NoToken.class, result);
    }

    @Test
    public void givenIdentifier_then_returnIdentifierToken() {
        String identifier = "bob";
        SourceFile sourceFile = mockSourceFile("source.java");
        doReturn("   " + identifier, null).when(sourceFile).nextLine();
        LexicalAnalyzer analyzer = new LexicalAnalyzer(sourceFile);

        Token result = analyzer.nextToken();
        assertInstanceOf(IdentifierToken.class, result);
        IdentifierToken token = (IdentifierToken)result;
        assertEquals(identifier, token.getIdentifierName());
    }

    @Test
    public void givenReservedWord_then_returnReservedWordToken() {
        String reservedWord = "if";
        SourceFile sourceFile = mockSourceFile("source.java");
        doReturn("   " + reservedWord, null).when(sourceFile).nextLine();
        LexicalAnalyzer analyzer = new LexicalAnalyzer(sourceFile);

        Token result = analyzer.nextToken();
        assertInstanceOf(ReservedWordToken.class, result);
        ReservedWordToken token = (ReservedWordToken) result;
        assertEquals(ReservedWord.IF, token.getReservedWord());
    }

    private SourceFile mockSourceFile(String fileName) {
        return spy(new SourceFile(fileName));
    }

}