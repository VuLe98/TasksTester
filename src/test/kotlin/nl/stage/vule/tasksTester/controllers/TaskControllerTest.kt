package nl.stage.vule.tasksTester.controllers

import nl.stage.vule.tasksTester.exceptionMapper.exceptions.EmptyParameterException
import nl.stage.vule.tasksTester.services.TaskResponse
import nl.stage.vule.tasksTester.services.TaskService
import nl.stage.vule.tasksTester.services.dto.TaskDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

class TaskControllerTest {

    lateinit var taskService: TaskService
    lateinit var taskController: TaskController
    private var taskResponse : TaskResponse? = null

    private var testToken : String = "ABCDEF-1"
    private var testDto : TaskDto = TaskDto("TEST-1234", "FFSPUC-100", 0, 1, testToken)

    private var testTasksArray : ArrayList<TaskDto> = ArrayList()

    @BeforeEach
    internal fun setup(){
        taskService = mock(TaskService::class.java)
        taskController = TaskController(taskService)

        testTasksArray.add(testDto)
    }


    @Test
    fun doesControllerInteractWithServiceChooseTasks(){
        //Setup
        `when`(taskService.addTask(testToken, testDto)).thenReturn(taskResponse)

        //Test
        taskController.addTask(testToken,testDto)

        //Verify
        verify(taskService).addTask(testToken,testDto)
    }


    @Test
    fun doesControllerInteractWithServiceChooseTasksBadRequestToken(){
        //Setup
        `when`(taskService.addTask(testToken,testDto)).thenReturn(taskResponse)

        //Test
        val EmptyParameterException = assertThrows(EmptyParameterException::class.java) { taskController.addTask( "", testDto)}

        //Verify
        assertEquals("Token is empty", EmptyParameterException.message)
    }

    @Test
    fun doesControllerInteractWithServiceUpdateSprintNoOfTasks() {
        //Setup
        doNothing().`when`(taskService).updateSprintNoOfTasks(testToken, testTasksArray)

        //Test
        taskController.updateSprintNoOfTasks(testToken,testTasksArray)

        //Verify
        verify(taskService).updateSprintNoOfTasks(testToken,testTasksArray)
    }

    @Test
    fun doesControllerInteractWithServiceUpdateSprintNoOfTasksBadRequestToken() {
        //Setup
        doNothing().`when`(taskService).updateSprintNoOfTasks(testToken, testTasksArray)

        //Test
        val EmptyParameterException = assertThrows(EmptyParameterException::class.java) { taskController.updateSprintNoOfTasks( "",testTasksArray)}

        //Verify
        assertEquals("Token is empty", EmptyParameterException.message)
    }

    @Test
    fun doesControllerInteractWithServiceGetTasks(){
        //Setup
        `when`(taskService.getTasks(testToken)).thenReturn(taskResponse)

        //Test
        taskController.getTasks(testToken)

        //Verify
        verify(taskService).getTasks(testToken)
    }

    @Test
    fun doesControllerInteractWithServiceGetTasksBadRequestToken(){
        //Setup
        `when`(taskService.getTasks(testToken)).thenReturn(taskResponse)

        //Test
        val EmptyParameterException = assertThrows(EmptyParameterException::class.java) { taskController.getTasks( "")}

        //Verify
        assertEquals("Token is empty", EmptyParameterException.message)
    }




}