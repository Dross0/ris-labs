package ru.gaidamaka.archive;

import org.apache.commons.cli.*;
import ru.gaidamaka.archive.config.ArchiveConfig;


public class CliArchiveConfigParser implements ArchiveConfiguration {

    private final Option fileOption = new Option("f", "filename", true, "Archive file name");
    private final Option bufferSizeOption = new Option("b", "buffer", true, "Read buffer size");

    private final String[] arguments;

    private ArchiveConfig archiveConfig;

    public CliArchiveConfigParser(String[] args) {
        arguments = args;
    }

    @Override
    public ArchiveConfig getConfig() {
        if (archiveConfig == null) {
            archiveConfig = parseConfigFromArgs();
        }
        return archiveConfig;
    }

    private ArchiveConfig parseConfigFromArgs() {
        Options options = new Options();
        options.addOption(fileOption);
        options.addOption(bufferSizeOption);
        DefaultParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, arguments);
            String fileName = cmd.getOptionValue(fileOption.getOpt());
            String bufferSize = cmd.getOptionValue(bufferSizeOption.getOpt());
            if (fileName == null && bufferSize == null){
                throw new IllegalArgumentException("Неверные параметры запуска");
            } else if (bufferSize == null) {
                return new ArchiveConfig(fileName);
            }
            return new ArchiveConfig(
                    fileName,
                    Integer.parseInt(bufferSize)
            );
        } catch (ParseException e) {
            throw new IllegalArgumentException("Ошибка при парсинге аргументов", e);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Размер буфера не является числом", e);
        }
    }
}
