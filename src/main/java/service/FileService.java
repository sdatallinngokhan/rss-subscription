package service;

import java.util.List;

/** CRUD Operations
 * Create -> writeFile()
 * Read -> readFile()
 * Update -> updateFile()
 * Delete -> deleteFile()
 */
public interface FileService {

    List<String> readFile(String pathString);
    void writeFile(String pathString, List<String> fileContentList);
    void updateFile(String pathString, List<String> fileContentList);
    void deleteFile(String pathString);
}
