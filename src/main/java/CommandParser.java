import java.util.ArrayList;
import java.util.List;

public class CommandParser {

    public List<String> parseCommand(String input){
        List<String> result = new ArrayList<>();
        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;
        StringBuilder currentWord = new StringBuilder();
        for (char c: input.toCharArray()){
            if (c == '\'' && !inDoubleQuote){
                inSingleQuote = !inSingleQuote;
            }
            else if (c == '\"' && !inSingleQuote){
                inDoubleQuote = !inDoubleQuote;
            }
            else if (c == ' ' && !inSingleQuote && !inDoubleQuote){
                if (!currentWord.isEmpty()){
                    result.add(currentWord.toString());
                    currentWord.setLength(0);
                }
            } else {
                currentWord.append(c);
            }
        }
        if(!currentWord.isEmpty()){
            result.add(currentWord.toString());
        }
        return result;
    }
}
