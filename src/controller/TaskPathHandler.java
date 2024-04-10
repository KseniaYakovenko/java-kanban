package controller;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.BadRequestException;
import exception.IntersectionByTime;
import exception.ManagerSaveException;
import exception.NotFoundException;
import model.Task;
import service.TaskManager;

import java.io.IOException;

public class TaskPathHandler extends BasePathHandler implements HttpHandler {

    public TaskPathHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestPath = httpExchange.getRequestURI().getPath();
        Endpoint endpoint = getEndpoint(requestPath, httpExchange.getRequestMethod());
        String response;
        try {
            switch (endpoint) {
                case GET_ALL: {
                    response = gson.toJson(manager.getAllTask().stream().map(Task::mapperToTaskDto).toList());
                    sendText(httpExchange, 200, response);
                    break;
                }
                case GET_BY_ID: {
                    int id = getId(requestPath);
                    response = gson.toJson(manager.getTaskById(id).mapperToTaskDto());
                    sendText(httpExchange, 200, response);
                    break;
                }
                case POST: {
                    String body = parseBody(httpExchange);
                    Task task = gson.fromJson(body, Task.class);

                    if (task.getId() == null || task.getId() == 0) {
                        int id = manager.createTask(task);
                        response = gson.toJson(manager.getTaskById(id).mapperToTaskDto());
                        sendText(httpExchange, 201, response);
                    } else {
                        manager.updateTask(task);
                        response = gson.toJson(task.mapperToTaskDto());
                        sendText(httpExchange, 200, response);
                    }
                    break;
                }
                case DELETE: {
                    String body = parseBody(httpExchange);
                    Task task = gson.fromJson(body, Task.class);
                    int id = task.getId();
                    manager.deleteTask(id);
                    sendText(httpExchange, 204);
                    break;
                }
                default:
                    errorHandler.sendText(httpExchange, 404, "Такого эндпоинта не существует");
            }

        } catch (IntersectionByTime e) {
            errorHandler.handle(httpExchange, e);
        } catch (ManagerSaveException e) {
            errorHandler.handle(httpExchange, e);
        } catch (BadRequestException e) {
            errorHandler.handle(httpExchange, e);
        } catch (NumberFormatException e) {
            errorHandler.handle(httpExchange, e);
        } catch (JsonSyntaxException e) {
            errorHandler.handle(httpExchange, e);
        } catch (NotFoundException e) {
            errorHandler.handle(httpExchange, e);
        } catch (IOException e) {
            errorHandler.handle(httpExchange, e);
        } catch (Exception e) {
            errorHandler.handle(httpExchange, e);
        } finally {
            httpExchange.close();
        }
    }
}