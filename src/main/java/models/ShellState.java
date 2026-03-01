package models;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ShellState {
    private Path currentDir = Paths.get(System.getProperty("user.dir")).toAbsolutePath().normalize();

    public ShellState(){
    }

    public Path getCurrentDir() {
        return currentDir;
    }

    public void setCurrentDir(Path currentDir) {
        this.currentDir = currentDir;
    }
}
