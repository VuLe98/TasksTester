package nl.han.ica.oose.roubaix.planningspoker.controllers

import nl.han.ica.oose.roubaix.planningspoker.exceptionMapper.exceptions.EmptyParameterException
import nl.han.ica.oose.roubaix.planningspoker.services.TaskService
import nl.han.ica.oose.roubaix.planningspoker.services.dto.TaskDto
import javax.ws.rs.*
import javax.ws.rs.core.Response

@Path("/")

class TaskController constructor(var taskService : TaskService = TaskService()){

    @Path("room/add")
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    fun addTask(@QueryParam("token") token: String, addedTask : TaskDto) : Response{
        if(token.isNullOrBlank()) {
            throw EmptyParameterException("Roomnumber or token is empty")
        }
        else {
            val response = taskService.addTask(token,addedTask)
            return Response.ok().entity(response).build()
        }
    }

    @Path("room/update")
    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    fun updateSprintNoOfTasks(@QueryParam("token") token: String ,updatedTasks : ArrayList<TaskDto>){
        if(token.isNullOrBlank()) {
            throw EmptyParameterException("Roomnumber or token is empty")
        }
        else {
            taskService.updateSprintNoOfTasks(token,updatedTasks)
        }
    }

    @Path("room/get")
    @GET
    @Consumes("application/json")
    @Produces("application/json")
    fun getTasks(@QueryParam("token") token: String) : Response{
        if(token.isNullOrBlank()) {
            throw EmptyParameterException("Roomnumber or token is empty")
        }
        else {
            val response = taskService.getTasks(token)
            return Response.ok().entity(response).build()
        }
    }
}
