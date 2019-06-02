package nl.han.ica.oose.roubaix.planningspoker.services

import nl.han.ica.oose.roubaix.planningspoker.datasources.TaskDao
import nl.han.ica.oose.roubaix.planningspoker.datasources.TokenDao
import nl.han.ica.oose.roubaix.planningspoker.exceptionMapper.exceptions.UserIsNotDeveloperException
import nl.han.ica.oose.roubaix.planningspoker.services.dto.TaskDto
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

class TaskServiceTest{

    lateinit var taskService: TaskService
    lateinit var taskDao : TaskDao
    lateinit var tokenDao: TokenDao
    private var taskResponse : TaskResponse? = null

    private var testToken : String = "ABCDEF-1"
    private var testDto : TaskDto = TaskDto("TEST-1234", "FFSPUC-100",0,1, testToken)

    @BeforeEach
    internal fun setup(){
        taskDao = Mockito.mock(TaskDao::class.java)
        tokenDao = Mockito.mock(TokenDao::class.java)

        taskService = TaskService(taskDao, tokenDao)

        taskResponse?.tasks?.add(testDto)

    }

    @Test
    fun doesEndpointDelegateToDAO(){
        `when`(tokenDao.checkIfUserIsDeveloper(testToken)).thenReturn(true)
        `when`(taskDao.addTaskToDatabase(testDto)).thenReturn(taskResponse)

        val actualResponse = taskService.addTask(testToken,testDto)

        verify(taskDao).addTaskToDatabase(testDto)
    }

    @Test
    fun chooseTasksRightToken() {
        `when`(tokenDao.checkIfUserIsDeveloper(testToken)).thenReturn(true)
        `when`(taskDao.addTaskToDatabase(testDto)).thenReturn(taskResponse)

        val actualResponse = taskService.addTask(testToken,testDto)

        assertEquals(taskResponse,actualResponse)
    }

    @Test
    fun chooseTasksWrongToken(){
        `when`(tokenDao.checkIfUserIsDeveloper(testToken)).thenReturn(true)
        `when`(taskDao.addTaskToDatabase(testDto)).thenReturn(taskResponse)

        val PlayerIsNotAdminException = Assertions.assertThrows(UserIsNotDeveloperException::class.java) { taskService.addTask( "", testDto) }

        assertEquals("This player is not the admin", PlayerIsNotAdminException.message)
    }




}