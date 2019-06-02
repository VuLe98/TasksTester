package nl.han.ica.oose.roubaix.planningspoker.datasources

import nl.han.ica.oose.roubaix.planningspoker.datasources.jdbc.DatabaseConnection
import nl.han.ica.oose.roubaix.planningspoker.exceptionMapper.exceptions.SQLError
import nl.han.ica.oose.roubaix.planningspoker.services.TaskResponse
import nl.han.ica.oose.roubaix.planningspoker.services.dto.TaskDto
import org.apache.commons.lang.RandomStringUtils
import java.sql.*
import java.util.*
import kotlin.collections.ArrayList

class TaskDao constructor(var dbConnection : Connection = DatabaseConnection.getInstance()!!){

    fun addTaskToDatabase(addedTask : TaskDto): TaskResponse {
        val taskResponse = TaskResponse()
        try {
                val st: PreparedStatement = dbConnection.prepareStatement("INSERT INTO TASK (TASKID, DESCRIPTION, DURATION, SPRINTNO, DEVELOPER) VALUES(?,?,?,?,?)")
                var randomID : String = RandomStringUtils.randomAlphabetic(8)
                st.setString(1,randomID)
                st.setString(2,addedTask.taskDescription)
                st.setInt(3, addedTask.taskDuration!!)
                st.setInt(4,addedTask.taskSprintNo!!)
                st.setString(5,addedTask.taskDeveloper)

                st.executeUpdate()
                st.close()

                taskResponse.tasks?.add(TaskDto(randomID,addedTask.taskDescription, addedTask.taskDuration,addedTask.taskSprintNo,addedTask.taskDeveloper))
        }
        catch(e : SQLException){
            e.printStackTrace()
        }
        return taskResponse
    }

    fun updateSprintNumberOfRemainingTasks(tasksArray : ArrayList<TaskDto>){
        try {
            for (task in tasksArray) {
                val st: PreparedStatement = dbConnection.prepareStatement("UPDATE TASK SET SPRINTNO = ? WHERE TASKID = ?")
                var currentSprintOfTask = getCurrentSprintOfTask(task.taskID!!)
                st.setInt(1, currentSprintOfTask + 1)
                st.setString(2,task.taskID)

                st.executeUpdate()
                st.close()
            }
        }
        catch(e : SQLException){
            e.printStackTrace()
            throw SQLError("Something went wrong in the database")
        }
    }

    fun getCurrentSprintOfTask(taskID : String) : Int{
        var currentSprintOfTask : Int? = null
        try {
            val st: PreparedStatement = dbConnection.prepareStatement("SELECT SPRINTNO FROM TASK WHERE TASKID = ?")
            st.setString(1, taskID)

            val resultSet: ResultSet? = st.executeQuery()

            while (resultSet!!.next()){
                currentSprintOfTask = resultSet.getInt("SPRINTNO")

            }
            return currentSprintOfTask!!
        }
        catch(e : SQLException){
            e.printStackTrace()
            throw SQLError("Something went wrong in the database")
        }
    }

    fun getTasksFromDatabase(): TaskResponse {
        val taskResponse = TaskResponse()
        var resultSet : ResultSet?
        try {
                val st: PreparedStatement = dbConnection.prepareStatement("SELECT * FROM TASK")
                resultSet = st.executeQuery()

                while(resultSet!!.next()) {
                    val taskDescription = resultSet.getString("DESCRIPTION")
                    val taskDuration = resultSet.getInt("DURATION")
                    val taskID = resultSet.getString("TASKID")
                    val taskSprintNo = resultSet.getInt("SPRINTNO")
                    val taskDeveloper = resultSet.getString("DEVELOPER")
                    taskResponse.tasks?.add(TaskDto(taskID,taskDescription, taskDuration, taskSprintNo, taskDeveloper))
                }
        }
        catch(e : SQLException){
            e.printStackTrace()
        }
        return taskResponse
    }
}
