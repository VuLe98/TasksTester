package nl.stage.vule.tasksTester.services

import nl.stage.vule.tasksTester.datasources.TaskDao
import nl.stage.vule.tasksTester.datasources.TokenDao
import nl.stage.vule.tasksTester.exceptionMapper.exceptions.UserIsNotDeveloperException
import nl.stage.vule.tasksTester.services.dto.TaskDto

class TaskService constructor(var taskDAO : TaskDao = TaskDao(), var tokenDao : TokenDao = TokenDao()){

    fun addTask(token : String, addedTask : TaskDto) : TaskResponse?{
        var listOfChosenTasks : TaskResponse? = null
        val developerIsAdmin : Boolean = tokenDao.checkIfUserIsDeveloper(token)
        if(developerIsAdmin) {
            listOfChosenTasks = taskDAO.addTaskToDatabase(addedTask)
        }
        else if(!developerIsAdmin){
            throw UserIsNotDeveloperException("This user is not an developer of this project")
        }
        return listOfChosenTasks
    }

    fun updateSprintNoOfTasks(token: String, tasksArray : ArrayList<TaskDto>){
        val developerIsAdmin : Boolean = tokenDao.checkIfUserIsDeveloper(token)
        if(developerIsAdmin) {
            taskDAO.updateSprintNumberOfRemainingTasks(tasksArray)
        }
        else{
            throw UserIsNotDeveloperException("This user is not an developer of this project")
        }
    }

    fun getTasks(token : String) : TaskResponse?{
        var listOfDatabaseTasks : TaskResponse? = null
        val developerIsAdmin = tokenDao.checkIfUserIsDeveloper(token)
        if(developerIsAdmin) {
            listOfDatabaseTasks = taskDAO.getTasksFromDatabase()
        }
        else if(!developerIsAdmin){
            throw UserIsNotDeveloperException("This user is not an developer of this project")
        }
        return listOfDatabaseTasks
    }
}
