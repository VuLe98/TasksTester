package nl.han.ica.oose.roubaix.planningspoker.services

import nl.han.ica.oose.roubaix.planningspoker.datasources.TaskDao
import nl.han.ica.oose.roubaix.planningspoker.datasources.TokenDao
import nl.han.ica.oose.roubaix.planningspoker.exceptionMapper.exceptions.UserIsNotDeveloperException
import nl.han.ica.oose.roubaix.planningspoker.services.dto.TaskDto

class TaskService constructor(var taskDAO : TaskDao = TaskDao(), var tokenDao : TokenDao = TokenDao()){

    fun addTask(token : String, addedTask : TaskDto) : TaskResponse?{
        var listOfChosenTasks : TaskResponse? = null
        val developerIsAdmin = tokenDao.checkIfUserIsDeveloper(token)
        if(developerIsAdmin) {
            listOfChosenTasks = taskDAO.addTaskToDatabase(addedTask)
        }
        else if(!developerIsAdmin){
            throw UserIsNotDeveloperException("This player is not the admin")
        }
        return listOfChosenTasks
    }

    fun updateSprintNoOfTasks(token: String, tasksArray : ArrayList<TaskDto>){
        val developerIsAdmin = tokenDao.checkIfUserIsDeveloper(token)
        if(developerIsAdmin) {
            taskDAO.updateSprintNumberOfRemainingTasks(tasksArray)
        }
        else{
            throw UserIsNotDeveloperException("This player is not the admin")
        }
    }

    fun getTasks(token : String) : TaskResponse?{
        var listOfDatabaseTasks : TaskResponse? = null
        val developerIsAdmin = tokenDao.checkIfUserIsDeveloper(token)
        if(developerIsAdmin) {
            listOfDatabaseTasks = taskDAO.getTasksFromDatabase()
        }
        else if(!developerIsAdmin){
            throw UserIsNotDeveloperException("This player is not the admin")
        }
        return listOfDatabaseTasks
    }
}
