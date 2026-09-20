package com.Ajie8021.arithmetic.cli;

import java.nio.file.Path;

public record CommandLineOptions(
        Mode mode, int number, int range,
        Path exerciseFile, Path answerFile) {

    public enum Mode {
        GENERATE,
        GRADE
    }
}
