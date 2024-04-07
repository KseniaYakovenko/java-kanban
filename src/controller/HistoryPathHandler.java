package controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.IntersectionByTime;
import exception.ManagerSaveException;
import exception.NotFoundException;
import model.Task;
import service.TaskManager;

import java.io.IOException;

public class HistoryPathHandler extends BasePathHandler implements HttpHandler {

    public HistoryPathHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        try {
            String response = getGson().toJson(manager.getHistory().stream().map(Task::mapperToHistoryListDto).toList());
            sendText(httpExchange, 200, response);

        } catch (IntersectionByTime e) {
            errorHandler.handle(httpExchange, e);
        } catch (ManagerSaveException e) {
            errorHandler.handle(httpExchange, e);
        } catch (IOException e) {
            errorHandler.handle(httpExchange, e);
        } catch (NumberFormatException e) {
            errorHandler.handle(httpExchange, e);
        } catch (NotFoundException e) {
            errorHandler.handle(httpExchange, e);
        } catch (Exception e) {
            errorHandler.handle(httpExchange, e);
        } finally {
            httpExchange.close();
        }
    }
}