package DesignPattern;

import java.util.ArrayList;
import java.util.List;

interface FileStructure {
    void showDetails(int intent);
}

class File implements FileStructure {
    String fileName;

    File(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void showDetails(int intent) {
        System.out.println(" ".repeat(intent) + "- " + fileName);
    }
}

class Folder implements FileStructure {
    String folderName;
    int intent = 1;
    List<FileStructure> folderHavingFiles = new ArrayList<>();

    Folder(String folderName) {
        this.folderName = folderName;
    }

    void addFileToFolder(FileStructure file) {
        folderHavingFiles.add(file);
    }

    void removeFileFromFolder(FileStructure file) {
        folderHavingFiles.remove(file);
    }

    @Override
    public void showDetails(int intent) {
        System.out.println(" ".repeat(intent) + "+ " + folderName);
        intent = intent + 2;
        for (FileStructure file : folderHavingFiles) {
            file.showDetails(intent);
        }
    }
}

public class CompositePattern {
    static void main() {
        File file1 = new File("java.pdf");
        File file2 = new File("resume.txt");

        Folder rootFolder = new Folder("root");

        rootFolder.addFileToFolder(file1);
        rootFolder.addFileToFolder(file2);

        Folder imageFolder = new Folder("images");

        File file3 = new File("pokemon.png");

        imageFolder.addFileToFolder(file3);

        rootFolder.addFileToFolder(imageFolder);

        Folder officePhotoFolder = new Folder("Office_Photo");

        File file4 = new File("officeClientMeetUp.png");

        officePhotoFolder.addFileToFolder(file4);

        imageFolder.addFileToFolder(officePhotoFolder);

        rootFolder.showDetails(0);
    }
}