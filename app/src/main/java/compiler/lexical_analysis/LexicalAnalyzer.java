package compiler.lexical_analysis;

import compiler.common.ReservedWord;

public class LexicalAnalyzer {
    private static final String PUNCTUATION=",;(){}[]+-*/%=!<>|&";
    private final SourceFile sourceFile;
    private int currentCharacter = 0;
    private String currentLine = "";
    private boolean endOfFile = false;

    public LexicalAnalyzer(SourceFile sourceFile) {
        this.sourceFile = sourceFile;
    }
    public Token nextToken() {
        if (endOfFile) return new NoToken();
        if (currentCharacter >= currentLine.length()) { //TODO - make sure this is correct
            getNextLine();
            if (endOfFile) return new NoToken();
        }
        // first consume any white space
        skipWhiteSpace();
        if (endOfFile) return new NoToken();
        // now find any non-punctuation character sequence, which is then a token
        String token = getNextNonPunctuation();
        // if there is a token, figure out what we have
        if (!token.isEmpty()) {
            return identifyRawToken(token);
        }
        else {
            // to do - figure out what our punctuation is
        }
        // if not check if we have a punctuation and process it
        if (endOfFile) return new NoToken();

        return new IdentifierToken("Unknown"); // TODO - this is the 'unknown' state until we're done developing
    }

    protected void getNextLine() {
        currentLine = sourceFile.nextLine();
        if (currentLine == null) {
            endOfFile = true;
        } else {
            currentCharacter = 0;
        }
    }

    protected char getCurrentCharacter() {
        if (currentCharacter < currentLine.length()) {
            return currentLine.charAt(currentCharacter);
        } else {
            return ' ';
        }
    }
    protected char getNextCharacter() {
        int nextCharacter = currentCharacter + 1;
        if (nextCharacter < currentLine.length() ) return currentLine.charAt(nextCharacter);
        return ' ';
    }

    private void skipWhiteSpace() {
        while (!endOfFile) {
            if (currentCharacter > currentLine.length()) {
                getNextLine();
            }
            else if (!Character.isWhitespace(getCurrentCharacter())) {
                break;
            } else { // TODO - here is where we catch line comments
                currentCharacter++;
            }
        }
    }

    protected String getNextNonPunctuation() {
        int characterToConsume = currentCharacter;
        StringBuilder token = new StringBuilder();
        while (characterToConsume < currentLine.length() ) {
            CharSequence charToCompare = currentLine.substring(characterToConsume, characterToConsume+1);
            if (PUNCTUATION.contains(charToCompare)) {
                break;
            }
            else if (Character.isWhitespace(charToCompare.charAt(0))) {
                break;
            } // TODO - here is where we catch the method calls on an object (a.get()) if the period is the first char, return it, otherwise put it back
            else {
                token.append(charToCompare);
                characterToConsume++;
            }
        }
        currentCharacter = characterToConsume;
        return token.toString();
    }

    protected Token identifyRawToken(String rawToken) {
        ReservedWord reservedWord = ReservedWord.toReservedWord(rawToken);
        if (reservedWord != null) return new ReservedWordToken(reservedWord);
        if (Character.isDigit(rawToken.charAt(0))) { // TODO - this doesn't work for negatives!
            try {
                return new IntegerToken(Integer.parseInt(rawToken));
            }
            catch (NumberFormatException e) {
                return new LexicalError(rawToken, sourceFile.getLineNumber(), "Not a valid integer");
            }
        }
        return new IdentifierToken(rawToken);

    }
}
