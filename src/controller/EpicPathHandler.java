package controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.IntersectionByTime;
import exception.ManagerSaveException;
import exception.NotFoundException;
import model.Epic;
import model.SubTask;
import model.dto.EpicDto;
import model.dto.SubTaskDto;
import service.TaskManager;

import java.io.IOException;
import java.util.List;


public class EpicPathHandler extends BasePathHandler implements HttpHandler {

    public EpicPathHandler(TaskManager manager) {
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
                    List<Epic> epicsList = manager.getAllEpic();
                    List<EpicDto> dtoList = epicsList.stream().map(Epic::mapperToDto).toList();
                    response = getGson().toJson(dtoList);
                    sendText(httpExchange, 200, response);
                    break;
                }
                case GET_BY_ID: {
                    int id = getId(requestPath);
                    response = getGson().toJson(manager.getEpicById(id).mapperToDto());
                    sendText(httpExchange, 200, response);
                    break;
                }
                case GET_EPIC_SUBTASKS: {
                    int id = getId(requestPath);
                    List<SubTask> subTaskList = manager.getEpicSubtasks(id);
                    List<SubTaskDto> subTaskDtoList = subTaskList.stream().map(SubTask::mapperToDto).toList();
                    response = getGson().toJson(subTaskDtoList);
                    sendText(httpExchange, 200, response);
                    break;
                }
                case POST: {
                    String body = parseBody(httpExchange);
                    Epic epic = getGson().fromJson(body, Epic.class);

                    if (epic.getId() == null || epic.getId() == 0) {
                        int id = manager.createEpic(epic);
                        response = getGson().toJson(manager.getEpicById(id).mapperToDto());
                        sendText(httpExchange, 201, response);
                    } else {
                        manager.updateEpic(epic);
                        response = getGson().toJson(epic.mapperToDto());
                        sendText(httpExchange, 200, response);
                    }
                    break;
                }
                case DELETE: {
                    String body = parseBody(httpExchange);
                    Epic epic = getGson().fromJson(body, Epic.class);
                    int id = epic.getId();
                    manager.deleteEpic(id);
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