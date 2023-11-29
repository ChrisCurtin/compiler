package compiler.lexical_analysis;


import compiler.common.Punctuation;
import compiler.common.ReservedWord;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LexicalAnalyzerTest {
    private static final String NULL_STRING = null;
    private static final String SOURCE_FILE_NAME = "source.java";

    @Test
    public void givenEmptyLine_then_returnNoToken() {
        SourceFile sourceFile = mockSourceFile(SOURCE_FILE_NAME);
        doReturn("", NULL_STRING).when(sourceFile).nextLine();
        LexicalAnalyzer analyzer = new LexicalAnalyzer(sourceFile);

        Token result = analyzer.nextToken();
        assertInstanceOf(NoToken.class, result);
    }
    @Test
    public void givenSpacesOnly_then_returnNoToken() {
        SourceFile sourceFile = mockSourceFile(SOURCE_FILE_NAME);
        doReturn("    ", NULL_STRING).when(sourceFile).nextLine();
        LexicalAnalyzer analyzer = new LexicalAnalyzer(sourceFile);

        Token result = analyzer.nextToken();
        assertInstanceOf(NoToken.class, result);
    }

    @Test
    public void givenIdentifier_then_returnIdentifierToken() {
        String identifier = "bob";
        SourceFile sourceFile = mockSourceFile(SOURCE_FILE_NAME);
        doReturn("   " + identifier + " ", NULL_STRING).when(sourceFile).nextLine();
        LexicalAnalyzer analyzer = new LexicalAnalyzer(sourceFile);

        Token result = analyzer.nextToken();
        assertInstanceOf(IdentifierToken.class, result);
        IdentifierToken token = (IdentifierToken)result;
        assertEquals(identifier, token.identifierName());
    }

    @Test
    public void givenReservedWord_then_returnReservedWordToken() {
        String reservedWord = "if";
        SourceFile sourceFile = mockSourceFile(SOURCE_FILE_NAME);
        doReturn("   " + reservedWord, NULL_STRING).when(sourceFile).nextLine();
        LexicalAnalyzer analyzer = new LexicalAnalyzer(sourceFile);

        Token result = analyzer.nextToken();
        assertInstanceOf(ReservedWordToken.class, result);
        ReservedWordToken token = (ReservedWordToken) result;
        assertEquals(ReservedWord.IF, token.reservedWord());
    }

    @Test
    public void givenSingleCharPunctuation_then_returnPunctuationToken() {
        String punctuation = "{";
        SourceFile sourceFile = mockSourceFile(SOURCE_FILE_NAME);
        doReturn("   " + punctuation, NULL_STRING).when(sourceFile).nextLine();
        LexicalAnalyzer analyzer = new LexicalAnalyzer(sourceFile);

        Token result = analyzer.nextToken();
        assertInstanceOf(PunctuationToken.class, result);
        PunctuationToken token = (PunctuationToken) result;
        assertEquals(Punctuation.LBRACE, token.punctuation());
    }
    @Test
    public void givenPossibleTwoCharPunctuation_then_returnPunctuationToken() {
        String punctuation = "!";
        SourceFile sourceFile = mockSourceFile(SOURCE_FILE_NAME);
        doReturn("   " + punctuation, NULL_STRING).when(sourceFile).nextLine();
        LexicalAnalyzer analyzer = new LexicalAnalyzer(sourceFile);

        Token result = analyzer.nextToken();
        assertInstanceOf(PunctuationToken.class, result);
        PunctuationToken token = (PunctuationToken) result;
        assertEquals(Punctuation.NOT, token.punctuation());
    }

    @Test
    public void givenIntegerString_then_returnIntegerToken() {
        String intValue = "12";
        SourceFile sourceFile = mockSourceFile(SOURCE_FILE_NAME);
        doReturn("   " + intValue, NULL_STRING).when(sourceFile).nextLine();
        LexicalAnalyzer analyzer = new LexicalAnalyzer(sourceFile);

        Token result = analyzer.nextToken();
        assertInstanceOf(IntegerToken.class, result);
        IntegerToken token = (IntegerToken) result;
        assertEquals(12, token.value());
    }

    @Test
    public void givenInvalidIntegerString_then_returnExceptionToken() {
        String intValue = "12j";
        SourceFile sourceFile = mockSourceFile(SOURCE_FILE_NAME);
        doReturn("   " + intValue, NULL_STRING).when(sourceFile).nextLine();
        LexicalAnalyzer analyzer = new LexicalAnalyzer(sourceFile);

        Token result = analyzer.nextToken();
        assertInstanceOf(LexicalError.class, result);
        LexicalError token = (LexicalError) result;
    }
    @Test
    public void givenTwoCharPunctuation_then_returnPunctuationToken() {
        String punctuation = "!=";
        SourceFile sourceFile = mockSourceFile(SOURCE_FILE_NAME);
        doReturn("   " + punctuation, NULL_STRING).when(sourceFile).nextLine();
        LexicalAnalyzer analyzer = new LexicalAnalyzer(sourceFile);

        Token result = analyzer.nextToken();
        assertInstanceOf(PunctuationToken.class, result);
        PunctuationToken token = (PunctuationToken) result;
        assertEquals(Punctuation.NOTEQUAL, token.punctuation());
    }

    @Test
    public void givenStringConstant_then_returnStringToken() {
        String stringConstant = "\"Hello!\"";
        SourceFile sourceFile = mockSourceFile(SOURCE_FILE_NAME);
        doReturn("   " + stringConstant, NULL_STRING).when(sourceFile).nextLine();
        LexicalAnalyzer analyzer = new LexicalAnalyzer(sourceFile);

        Token result = analyzer.nextToken();
        assertInstanceOf(StringToken.class, result);
        StringToken token = (StringToken) result;
        assertEquals("Hello!", token.value());
    }

    @Test
    public void givenLineComment_then_returnEndOfFile() {
        String stringConstant = "\"Hello!\" // test here";
        SourceFile sourceFile = mockSourceFile(SOURCE_FILE_NAME);
        doReturn("   " + stringConstant, NULL_STRING).when(sourceFile).nextLine();
        LexicalAnalyzer analyzer = new LexicalAnalyzer(sourceFile);

        Token result = analyzer.nextToken();
        assertInstanceOf(StringToken.class, result);
        StringToken token = (StringToken) result;
        assertEquals("Hello!", token.value());

        result = analyzer.nextToken();
        assertInstanceOf(NoToken.class, result);
    }

    @Test
    public void givenMultipleLinesWithStringConstant_then_returnStringToken() {
        String stringConstant = "\"Hello!\"";
        String lineTwo = "\"Again\"";
        SourceFile sourceFile = mockSourceFile(SOURCE_FILE_NAME);
        doReturn(stringConstant, lineTwo, NULL_STRING).when(sourceFile).nextLine();
        LexicalAnalyzer analyzer = new LexicalAnalyzer(sourceFile);

        Token result = analyzer.nextToken();
        assertInstanceOf(StringToken.class, result);
        StringToken token = (StringToken) result;
        assertEquals("Hello!", token.value());

        result = analyzer.nextToken();
        assertInstanceOf(StringToken.class, result);
        token = (StringToken) result;
        assertEquals("Again", token.value());
    }

    @Test
    public void givenMultipleTokens_then_parseCorrectly() {
        SourceFile sourceFile = mockSourceFile(SOURCE_FILE_NAME);
        doReturn("if (bob != \"tree\") ;   ", NULL_STRING).when(sourceFile).nextLine();
        LexicalAnalyzer analyzer = new LexicalAnalyzer(sourceFile);

        Token result = analyzer.nextToken();
        assertInstanceOf(ReservedWordToken.class, result);
        ReservedWordToken token = (ReservedWordToken) result;
        assertEquals(ReservedWord.IF, token.reservedWord());

        result = analyzer.nextToken();
        assertInstanceOf(PunctuationToken.class, result);
        PunctuationToken punctuationToken = (PunctuationToken) result;
        assertEquals(Punctuation.LPAREN, punctuationToken.punctuation());

        result = analyzer.nextToken();
        assertInstanceOf(IdentifierToken.class, result);
        IdentifierToken identifierToken = (IdentifierToken)result;
        assertEquals("bob", identifierToken.identifierName());

        result = analyzer.nextToken();
        assertInstanceOf(PunctuationToken.class, result);
        punctuationToken = (PunctuationToken) result;
        assertEquals(Punctuation.NOTEQUAL, punctuationToken.punctuation());

        result = analyzer.nextToken();
        assertInstanceOf(StringToken.class, result);
        StringToken stringToken = (StringToken) result;
        assertEquals("tree", stringToken.value());

        result = analyzer.nextToken();
        assertInstanceOf(PunctuationToken.class, result);
        punctuationToken = (PunctuationToken) result;
        assertEquals(Punctuation.RPAREN, punctuationToken.punctuation());

        result = analyzer.nextToken();
        assertInstanceOf(PunctuationToken.class, result);
        punctuationToken = (PunctuationToken) result;
        assertEquals(Punctuation.SEMICOLON, punctuationToken.punctuation());

        result = analyzer.nextToken();
        assertInstanceOf(NoToken.class, result); // confirm the spaces at the end don't cause an issue
    }

    @Test
    public void givenStringVariable_then_returnStringToken() {
        String punctuation = "!=";
        SourceFile sourceFile = mockSourceFile(SOURCE_FILE_NAME);
        doReturn("   " + punctuation, NULL_STRING).when(sourceFile).nextLine();
        LexicalAnalyzer analyzer = new LexicalAnalyzer(sourceFile);

        Token result = analyzer.nextToken();
        assertInstanceOf(PunctuationToken.class, result);
        PunctuationToken token = (PunctuationToken) result;
        assertEquals(Punctuation.NOTEQUAL, token.punctuation());
    }
    private SourceFile mockSourceFile(String fileName) {
        return spy(new SourceFile(fileName));
    }

}