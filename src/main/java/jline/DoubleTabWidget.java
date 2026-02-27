package jline;

import org.jline.reader.LineReader;
import org.jline.reader.Reference;
import org.jline.widget.Widgets;

import java.util.List;

public class DoubleTabWidget extends Widgets {
    private final ExecutableCompleter executableCompleter;

    private boolean firstTabSeen = false;
    private String lastPrefix = null;

    public static DoubleTabWidget create(LineReader reader, ExecutableCompleter executableCompleter) {
        DoubleTabWidget widgets = new DoubleTabWidget(reader, executableCompleter);
        widgets.addWidget("complete", widgets::complete);
        widgets.getKeyMap().bind(new Reference("complete"), "\t");

        return widgets;
    }

    private DoubleTabWidget(LineReader reader, ExecutableCompleter executableCompleter) {
        super(reader);
        this.executableCompleter = executableCompleter;
    }

    public boolean complete() {
        String prefix = super.reader.getBuffer().toString();
        List<String> matches = executableCompleter.getMatches(prefix).stream().toList();

        if (matches.isEmpty()){
            printBell();
            firstTabSeen = false;
            lastPrefix = null;
        } else if (matches.size() == 1){
            reader.getBuffer().write(matches.getFirst().substring(prefix.length()) + " ");
            callWidget(LineReader.REDISPLAY);
            firstTabSeen = false;
            lastPrefix = null;
        } else {
            if (prefix.equals(lastPrefix)) {
                firstTabSeen = true;
            } else {
                firstTabSeen = false;
                lastPrefix = prefix;
            }

            if (firstTabSeen){
                reader.getTerminal().writer().println();
                reader.getTerminal().writer().println(String.join("  ", matches));
                reader.getTerminal().flush();

                reader.callWidget(LineReader.REDRAW_LINE);
                reader.callWidget(LineReader.REDISPLAY);

                firstTabSeen = true;
            } else {
                printBell();
            }
        }

        return true;
    }

    private void printBell() {
        reader.getTerminal().writer().print("\u0007");
        reader.getTerminal().flush();
    }
}
