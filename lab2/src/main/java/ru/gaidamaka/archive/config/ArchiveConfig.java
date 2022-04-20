package ru.gaidamaka.archive.config;

public class ArchiveConfig {
    private static final int MIN_BUFFER_SIZE = 0;
    private static final int DEFAULT_BUFFER_SIZE = 8192;

    private final String filename;

    private final int bufferSize;

    public ArchiveConfig(String filename, int bufferSize) {
        this.filename = filename;
        validateBufferSize(bufferSize);
        this.bufferSize = bufferSize;
    }

    public ArchiveConfig(String filename) {
        this(filename, DEFAULT_BUFFER_SIZE);
    }

    private void validateBufferSize(long bufferSize) {
        if (bufferSize < MIN_BUFFER_SIZE) {
            throw new IllegalArgumentException("Размер буфера:" + bufferSize + " меньше минимально допустимого значения: " + MIN_BUFFER_SIZE);
        }
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public String getFilename() {
        return filename;
    }
}
