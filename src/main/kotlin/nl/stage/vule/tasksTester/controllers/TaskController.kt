package nl.stage.vule.tasksTester.controllers

import nl.stage.vule.tasksTester.exceptionMapper.exceptions.EmptyParameterException
import nl.stage.vule.tasksTester.services.TaskService
import nl.stage.vule.tasksTester.services.dto.TaskDto
import javax.ws.rs.*
import javax.ws.rs.core.Response

@Path("/room")

class TaskController constructor(var taskService : TaskService = TaskService()){

    @Path("add")
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    fun addTask(@QueryParam("token") token: String, addedTask : TaskDto) : Response{
        if(token.isNullOrBlank()) {
            throw EmptyParameterException("Token is empty")
        }
        else {
            val response = taskService.addTask(token,addedTask)
            return Response.ok().entity(response).build()
        }
    }

    @Path("update")
    @PUT
    @Consumes("application/json")
    fun updateSprintNoOfTasks(@QueryParam("token") token: String ,updatedTasks : ArrayList<TaskDto>){
        if(token.isNullOrBlank()) {
            throw EmptyParameterException("Token is empty")
        }
        else {
            taskService.updateSprintNoOfTasks(token,updatedTasks)
        }
    }

    @Path("get")
    @GET
    @Consumes("application/json")
    @Produces("application/json")
    fun getTasks(@QueryParam("token") token: String) : Response{
        if(token.isNullOrBlank()) {
            throw EmptyParameterException("Token is empty")
        }
        else {
            val response = taskService.getTasks(token)
            return Response.ok().entity(response).build()
        }
    }
}
