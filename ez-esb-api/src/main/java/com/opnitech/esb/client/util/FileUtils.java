package com.opnitech.esb.client.util;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import com.opnitech.esb.client.exception.ServiceException;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public final class FileUtils {

    private FileUtils() {
        // Default constructor
    }

    public static String readFile(String fileName) throws ServiceException {

        StringBuilder result = new StringBuilder("");

        ClassLoader classLoader = FileUtils.class.getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }
        }
        catch (IOException e) {
            throw new ServiceException(e);
        }

        return result.toString();

    }

}
