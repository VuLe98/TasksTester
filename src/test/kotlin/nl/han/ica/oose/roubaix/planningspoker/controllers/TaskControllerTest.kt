package nl.han.ica.oose.roubaix.planningspoker.controllers

import nl.han.ica.oose.roubaix.planningspoker.exceptionMapper.exceptions.EmptyParameterException
import nl.han.ica.oose.roubaix.planningspoker.services.TaskResponse
import nl.han.ica.oose.roubaix.planningspoker.services.TaskService
import nl.han.ica.oose.roubaix.planningspoker.services.dto.TaskDto
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
    private var testDto = TaskDto("TEST-1234", "FFSPUC-100", 0, 1, testToken)

    @BeforeEach
    internal fun setup(){
        taskService = mock(TaskService::class.java)
        taskController = TaskController(taskService)
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
        assertEquals("Roomnumber or token is empty", EmptyParameterException.message)
    }


}