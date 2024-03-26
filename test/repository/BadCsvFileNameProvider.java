
package repository;

public class BadCsvFileNameProvider implements FileNameProvider {

    public String getTaskFileName() {
        return "taskTestBad.csv";
    }

    public String getHistoryFileName() {
        return "historyTestBad.csv";
    }
}