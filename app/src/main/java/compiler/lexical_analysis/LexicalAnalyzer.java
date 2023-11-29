package compiler.lexical_analysis;

import compiler.common.Punctuation;
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
        if (currentCharacter >= currentLine.length()) {
            getNextLine();
            if (endOfFile) return new NoToken();
        }
        // first consume any white space
        skipWhiteSpace();
        if (endOfFile) return new NoToken();
        // now find any non-punctuation character sequence, which is then a token
        Token nextToken = new NoToken();
        String token = getNextNonPunctuation();
        // if there is a token, figure out what we have
        if (!token.isEmpty()) {
            nextToken = identifyRawToken(token);
        }
        else {
           Punctuation punctuation = getPunctuation();
           if (punctuation != null) nextToken =  new PunctuationToken(punctuation);
        }
        return nextToken;
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
            else if (getCurrentCharacter() == '/') {
                if (getNextCharacter() == '/') { // line comment!
                    currentCharacter = currentLine.length() + 1; // force the next line in the file
                }
            }
            else if (!Character.isWhitespace(getCurrentCharacter())) {
                break;
            }
            else {
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
            else if ("\"".contentEquals(charToCompare)) {
                int endOfStringCharacter = parseStringConstant(characterToConsume);
                if (endOfStringCharacter > 0) {
                    token.append(currentLine, characterToConsume, endOfStringCharacter+1);
                    characterToConsume = endOfStringCharacter + 1;  // +1 to get the " character and move the position forward
                    break;
                }
                // TODO - what to do if incomplete string? i.e. no ending double quote?
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

    protected int parseStringConstant(int startingCharacter) {
        return currentLine.indexOf("\"",startingCharacter+1 );
    }

    protected Token identifyRawToken(String rawToken) {
        ReservedWord reservedWord = ReservedWord.toReservedWord(rawToken);
        if (reservedWord != null) return new ReservedWordToken(reservedWord);
        if (Character.isDigit(rawToken.charAt(0))) { // Note - this doesn't work for negatives!
            try {
                return new IntegerToken(Integer.parseInt(rawToken));
            }
            catch (NumberFormatException e) {
                return new LexicalError(rawToken, sourceFile.getLineNumber(), "Not a valid integer");
            }
        }
        else if (rawToken.charAt(0) == '\"') {
            return new StringToken(rawToken.substring(1, rawToken.length() -1));
        }
        return new IdentifierToken(rawToken);

    }

    protected Punctuation getPunctuation() {
        Punctuation punctuation = Punctuation.toPunctuation(String.valueOf(getCurrentCharacter()));
        Punctuation finalPunctuation = punctuation;
        if (punctuation != null) {
            if (punctuation.isCanBeTwoCharacters()) {
                Punctuation rightPunctuation = Punctuation.toPunctuation(String.valueOf(getNextCharacter()));
                if (rightPunctuation != null) {
                    finalPunctuation = Punctuation.combinePunctuation(punctuation, rightPunctuation);
                    if (finalPunctuation != null)  {
                        currentCharacter++; // consume the character
                    }

                }
            }
            currentCharacter++; // always consume the character
        }
        return finalPunctuation;
    }
}
