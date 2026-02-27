package jline;

import org.jline.reader.LineReader;
import org.jline.reader.Reference;
import org.jline.widget.Widgets;

import java.util.List;

public class DoubleTabWidget extends Widgets {
    private final Completer completer;

    private boolean firstTabSeen = false;
    private String lastPrefix = null;

    public static DoubleTabWidget create(LineReader reader, Completer completer) {
        DoubleTabWidget widgets = new DoubleTabWidget(reader, completer);
        widgets.addWidget("complete", widgets::complete);
        widgets.getKeyMap().bind(new Reference("complete"), "\t");

        return widgets;
    }

    private DoubleTabWidget(LineReader reader, Completer completer) {
        super(reader);
        this.completer = completer;
    }

    public boolean complete() {
        String prefix = super.reader.getBuffer().toString();
        List<String> matches = completer.getMatches(prefix).stream().toList();

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
