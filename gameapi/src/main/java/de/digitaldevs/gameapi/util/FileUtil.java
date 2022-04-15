package de.digitaldevs.gameapi.util;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;

public final class FileUtil {

    public static void copy(@NotNull final File source, @NotNull final File destination) throws IOException {
        if (source.isDirectory()) {
            if (!destination.exists()) {
                //noinspection ResultOfMethodCallIgnored
                destination.mkdirs();
            }

            final String[] files = source.list();
            if (files == null) return;
            for (String file : files) {
                final File newSource = new File(source, file);
                final File newDestination = new File(destination, file);
                copy(newSource, newDestination);
            }
        } else {
            final InputStream inputStream = Files.newInputStream(source.toPath());
            final OutputStream outputStream = Files.newOutputStream(destination.toPath());

            final byte[] buffer = new byte[1024];
            int length;

            // Copy the file content in bytes
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            outputStream.close();
        }
    }

    public static void delete(@NotNull final File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) return;
            for (final File child : files) {
                delete(child);
            }
        }
        //noinspection ResultOfMethodCallIgnored
        file.delete();
    }

}
