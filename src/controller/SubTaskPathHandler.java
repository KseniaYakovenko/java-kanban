package controller;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.BadRequestException;
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
                    response = gson.toJson(dtoList);
                    sendText(httpExchange, 200, response);
                    break;
                }
                case GET_BY_ID: {
                    int id = getId(requestPath);
                    response = gson.toJson(manager.getSubTaskById(id).mapperToDto());
                    sendText(httpExchange, 200, response);
                    break;
                }
                case POST: {
                    String body = parseBody(httpExchange);
                    SubTaskDto subTaskDto = gson.fromJson(body, SubTaskDto.class);

                    if (subTaskDto.getId() == null || subTaskDto.getId() == 0) {
                        SubTask subTask = manager.dtoToModel(subTaskDto);
                        int id = manager.createSubTask(subTask);
                        response = gson.toJson(manager.getSubTaskById(id).mapperToDto());
                        sendText(httpExchange, 201, response);
                    } else {
                        manager.updateSubTask(manager.dtoToModel(subTaskDto));
                        response = gson.toJson(subTaskDto);
                        sendText(httpExchange, 200, response);
                    }
                    break;
                }
                case DELETE: {
                    String body = parseBody(httpExchange);
                    SubTask subTask = gson.fromJson(body, SubTask.class);
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
        } catch (NumberFormatException e) {
            errorHandler.handle(httpExchange, e);
        } catch (JsonSyntaxException e) {
            errorHandler.handle(httpExchange, e);
        } catch (NotFoundException e) {
            errorHandler.handle(httpExchange, e);
        } catch (BadRequestException e) {
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