package repository;

public class CsvFileNameProvider implements FileNameProvider {

    @Override
    public String getTaskFileName() {
        return "task.csv";
    }

    @Override
    public String getHistoryFileName() {
        return "history.csv";
    }
}