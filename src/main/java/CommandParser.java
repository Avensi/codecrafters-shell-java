import java.util.ArrayList;
import java.util.List;

public class CommandParser {

    private static final char SINGLE_QUOTE = '\'';
    private static final char DOUBLE_QUOTE = '\"';
    private static final char SPACE = ' ';
    private static final char ESCAPE = '\\';
    private static final char DOLLAR = '$';
    private static final char NEWLINE = '\n';
    private static final List<String> REDIRECT_OPERATORS = List.of(">", "1>", "2>");

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

    public int getRedirectIndex(List<String> tokens){
        for (int i=0; i<tokens.size(); i++){
            if (REDIRECT_OPERATORS.contains(tokens.get(i))){
               return i;
            }
        }
        return -1;
    }

    private boolean isValidDoubleQuoteEscape(char nextChar) {
        return nextChar == DOUBLE_QUOTE || nextChar == ESCAPE || nextChar == DOLLAR || nextChar == NEWLINE;
    }
}
