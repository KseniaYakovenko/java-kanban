
package repository;

public class BadCsvFileNameProviderTest implements FileNameProvider {

    public String getTaskFileName() {
        return "taskTestBad.csv";
    }

    public String getHistoryFileName() {
        return "historyTestBad.csv";
    }
}