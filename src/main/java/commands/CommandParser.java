package commands;

import models.PipelineSegment;

import java.util.ArrayList;
import java.util.List;

public class CommandParser {

    private static final char SINGLE_QUOTE = '\'';
    private static final char DOUBLE_QUOTE = '\"';
    private static final char SPACE = ' ';
    private static final char ESCAPE = '\\';
    private static final char DOLLAR = '$';
    private static final char NEWLINE = '\n';
    private static final char PIPE = '|';
    private static final List<String> REDIRECT_OPERATORS = List.of(">", "1>", "2>", "1>>", ">>", "2>>");

    public List<String> parseCommand(String input){
        List<String> result = new ArrayList<>();
        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;
        StringBuilder currentWord = new StringBuilder();

        for(int i=0; i<input.length(); i++){
            char currentChar = input.charAt(i);
            if (currentChar == ESCAPE && !inSingleQuote){
                if (i + 1 >= input.length()) {
                    currentWord.append(currentChar);
                    continue;
                }

                char nextChar = input.charAt(i + 1);

                if(inDoubleQuote){
                    if (isValidDoubleQuoteEscape(nextChar)) {
                        currentWord.append(nextChar);
                        i++;
                    } else {
                        currentWord.append(currentChar);
                    }
                } else {
                    currentWord.append(nextChar);
                    i++;
                }
            }
            else if (currentChar == SINGLE_QUOTE && !inDoubleQuote){
                inSingleQuote = !inSingleQuote;
            }
            else if (currentChar == DOUBLE_QUOTE && !inSingleQuote){
                inDoubleQuote = !inDoubleQuote;
            }
            else if (currentChar == SPACE && !inSingleQuote && !inDoubleQuote){
                if (!currentWord.isEmpty()){
                    result.add(currentWord.toString());
                    currentWord.setLength(0);
                }
            } else {
                currentWord.append(currentChar);
            }

        }
        if(!currentWord.isEmpty()){
            result.add(currentWord.toString());
        }
        return result;
    }

    private boolean isValidDoubleQuoteEscape(char nextChar) {
        return nextChar == DOUBLE_QUOTE || nextChar == ESCAPE || nextChar == DOLLAR || nextChar == NEWLINE || nextChar == PIPE;
    }

    private int getRedirectIndex(List<String> tokens){
        for (int i=0; i<tokens.size(); i++){
            if (REDIRECT_OPERATORS.contains(tokens.get(i))){
               return i;
            }
        }
        return -1;
    }

    private PipelineSegment buildSegment(List<String> tokens) {
        int redirectIndex = getRedirectIndex(tokens);
        if (redirectIndex != -1) {
            String op = tokens.get(redirectIndex);
            String file = tokens.get(redirectIndex + 1);
            List<String> cleanTokens = new ArrayList<>(tokens.subList(0, redirectIndex));
            return new PipelineSegment(cleanTokens, op, file);
        }
        return new PipelineSegment(tokens, null, null);
    }

    public List<PipelineSegment> parseSegments(List<String> tokens){
        List<PipelineSegment> segments = new ArrayList<>();

        List<String> currentTokens = new ArrayList<>();
        for (String token: tokens){
            if (token.equals(String.valueOf(PIPE))){
                segments.add(buildSegment(currentTokens));
                currentTokens.clear();
            } else {
                currentTokens.add(token);
            }
        }
        segments.add(buildSegment(currentTokens));
        return segments;
    }
}
