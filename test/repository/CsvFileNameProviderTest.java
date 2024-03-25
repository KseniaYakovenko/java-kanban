package repository;

public class CsvFileNameProviderTest implements FileNameProvider {

    public String getTaskFileName() {
        return "taskTest.csv";
    }

    public String getHistoryFileName() {
        return "historyTest.csv";
    }
}