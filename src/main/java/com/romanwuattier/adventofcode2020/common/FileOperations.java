package com.romanwuattier.adventofcode2020.common;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

interface FileOperations {
    default String readDay(int day) {
        return getResourceAsString("day" + day + ".txt");
    }

    default String getResourceAsString(String resource) {
        return getFileAsString(new File(FileOperations.class.getClassLoader().getResource(resource).getFile()));
    }

    default String getFileAsString(File file) {
        try {
            return Files.readString(file.toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
