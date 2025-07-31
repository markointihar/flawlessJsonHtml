package com.marko.flawlessJsonHtml.converter;

import java.nio.file.Path;

public record FileConfig(Path inputFile, Path outputFile) {
    public FileConfig {
        if (inputFile == null) {
            throw new IllegalArgumentException("Input file path cannot be null");
        }
        if (outputFile == null) {
            throw new IllegalArgumentException("Output file path cannot be null");
        }
    }
}
