package controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.IntersectionByTime;
import exception.ManagerSaveException;
import exception.NotFoundException;
import model.SubTask;
import model.dto.SubTaskDto;
import service.TaskManager;

import java.io.IOException;
import java.util.List;

public class SubTaskPathHandler extends BasePathHandler implements HttpHandler {

    public SubTaskPathHandler(TaskManager manager) {
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
                    List<SubTask> subTaskList = manager.getAllSubTask();
                    List<SubTaskDto> dtoList = subTaskList.stream().map(SubTask::mapperToDto).toList();
                    response = getGson().toJson(dtoList);
                    sendText(httpExchange, 200, response);
                    break;
                }
                case GET_BY_ID: {
                    int id = getId(requestPath);
                    response = getGson().toJson(manager.getSubTaskById(id).mapperToDto());
                    sendText(httpExchange, 200, response);
                    break;
                }
                case POST: {
                    String body = parseBody(httpExchange);
                    SubTask subTask = getGson().fromJson(body, SubTask.class);

                    if (subTask.getId() == null || subTask.getId() == 0) {
                        int id = manager.createSubTask(subTask);
                        response = getGson().toJson(manager.getSubTaskById(id).mapperToDto());
                        sendText(httpExchange, 201, response);
                    } else {
                        manager.updateSubTask(subTask);
                        response = getGson().toJson(subTask.mapperToDto());
                        sendText(httpExchange, 200, response);
                    }
                    break;
                }
                case DELETE: {
                    String body = parseBody(httpExchange);
                    SubTask subTask = getGson().fromJson(body, SubTask.class);
                    int id = subTask.getId();
                    manager.deleteSubTask(id);
                    sendText(httpExchange, 204, "Deleted");
                    break;
                }
                default:
                    errorHandler.sendText(httpExchange, 404, "Такого эндпоинта не существует");
            }

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