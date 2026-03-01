package handlers;

import models.PipelineSegment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class RedirectionHandler implements AutoCloseable{
    private final PrintStream originalOut;
    private final PrintStream originalErr;

    public RedirectionHandler(PrintStream originalOut, PrintStream originalErr){
        this.originalOut = originalOut;
        this.originalErr = originalErr;
    }

    public void redirectJvmStreams(String redirectionOperator, String fileName) throws FileNotFoundException {
        if (fileName != null){
            FileOutputStream outputFile;
            FileOutputStream outputAppendFile;
            switch (redirectionOperator){
                case ">", "1>":
                    outputFile = new FileOutputStream(fileName);
                    System.setOut(new PrintStream(outputFile));
                    break;
                case "1>>", ">>":
                    outputAppendFile = new FileOutputStream(fileName, true);
                    System.setOut(new PrintStream(outputAppendFile));
                    break;
                case "2>":
                    outputFile = new FileOutputStream(fileName);
                    System.setErr(new PrintStream(outputFile));
                    break;
                case "2>>":
                    outputAppendFile = new FileOutputStream(fileName, true);
                    System.setErr(new PrintStream(outputAppendFile));
                    break;
            }
        }
    }

    public void redirectOsProcess(ProcessBuilder processBuilder, String redirectionOperator, String fileName) {
        if (fileName != null){
            switch (redirectionOperator) {
                case ">", "1>" -> {
                    processBuilder.redirectOutput(new File(fileName));
                    processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
                }
                case "1>>", ">>" -> {
                    processBuilder.redirectOutput(ProcessBuilder.Redirect.appendTo(new File(fileName)));
                    processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
                }
                case "2>" -> {
                    processBuilder.redirectError(new File(fileName));
                    processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
                }
                case "2>>" -> {
                    processBuilder.redirectError(ProcessBuilder.Redirect.appendTo(new File(fileName)));
                    processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
                }
            }
        }
    }

    public void configureProcessStreams(PipelineSegment segment, boolean isSegmentFirst, ProcessBuilder processBuilder, boolean isSegmentLast) {
        if (isSegmentFirst) {
            processBuilder.redirectInput(ProcessBuilder.Redirect.INHERIT);
        }
        if (isSegmentLast) {
            if (segment.redirectOp() != null && segment.fileName() != null){
                redirectOsProcess(processBuilder, segment.redirectOp(), segment.fileName());
            } else {
                processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            }
        }else {
            processBuilder.redirectInput(ProcessBuilder.Redirect.PIPE);
            processBuilder.redirectOutput(ProcessBuilder.Redirect.PIPE);
        }
    }

    public void restore() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Override
    public void close() {
        restore();
    }
}
