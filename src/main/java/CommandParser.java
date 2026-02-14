import java.util.ArrayList;
import java.util.List;

public class CommandParser {

    private static final char SINGLE_QUOTE = '\'';
    private static final char DOUBLE_QUOTE = '\"';
    private static final char SPACE = ' ';
    private static final char ESCAPE = '\\';

    public List<String> parseCommand(String input){
        List<String> result = new ArrayList<>();
        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;
        StringBuilder currentWord = new StringBuilder();

        for(int i=0; i<input.length(); i++){
            char currentChar = input.charAt(i);
            if (currentChar == ESCAPE){
                if (i + 1 < input.length()) {
                    char nextChar = input.charAt(i + 1);
                    currentWord.append(nextChar);
                }
            } else {
                if (currentChar == SINGLE_QUOTE && !inDoubleQuote){
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

        }
        if(!currentWord.isEmpty()){
            result.add(currentWord.toString());
        }
        return result;
    }
}
