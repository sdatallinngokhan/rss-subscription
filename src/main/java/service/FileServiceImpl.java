package service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileServiceImpl implements FileService {

    @Override
    public List<String> readFile(String pathString) {
        try {
            Path path = Paths.get(pathString);

            return Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void writeFile(String pathString, List<String> fileContentList) {
        try {
            Path path = Paths.get(pathString);
            Files.write(path, fileContentList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateFile(String pathString, List<String> fileContentList) {

    }

    @Override
    public void deleteFile(String pathString) {
        try {
            Path path = Paths.get(pathString);

            Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
