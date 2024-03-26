package repository;

public class CsvTestFileNameProvider implements FileNameProvider {

    public String getTaskFileName() {
        return "taskTest.csv";
    }

    public String getHistoryFileName() {
        return "historyTest.csv";
    }
}