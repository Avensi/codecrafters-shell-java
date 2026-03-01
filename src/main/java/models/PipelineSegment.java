package models;

import java.util.List;

public record PipelineSegment(List<String> tokens, String redirectOp, String fileName) {
}
