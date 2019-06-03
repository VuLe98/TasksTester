package nl.stage.vule.tasksTester.services

import nl.stage.vule.tasksTester.datasources.TaskDao
import nl.stage.vule.tasksTester.datasources.TokenDao
import nl.stage.vule.tasksTester.exceptionMapper.exceptions.UserIsNotDeveloperException
import nl.stage.vule.tasksTester.services.dto.TaskDto
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

class TaskServiceTest{

    lateinit var taskService: TaskService
    lateinit var taskDao : TaskDao
    lateinit var tokenDao: TokenDao
    private var taskResponse : TaskResponse? = null

    private var testToken : String = "ABCDEF-1"
    private var testDto : TaskDto = TaskDto("TEST-1234", "FFSPUC-100", 0, 1, testToken)
    private var testDtoUpdated: TaskDto = TaskDto("TEST-1234", "FFSPUC-100", 0, 2, testToken)

    private var testTasksArray : ArrayList<TaskDto> = ArrayList()

    @BeforeEach
    internal fun setup(){
        taskDao = mock(TaskDao::class.java)
        tokenDao = mock(TokenDao::class.java)

        taskService = TaskService(taskDao, tokenDao)

        testTasksArray.add(testDto)

        taskResponse?.tasks?.add(testDtoUpdated)

    }

    @Test
    fun doesEndpointDelegateToDAO(){
        `when`(tokenDao.checkIfUserIsDeveloper(testToken)).thenReturn(true)
        `when`(taskDao.addTaskToDatabase(testDto)).thenReturn(taskResponse)

        taskService.addTask(testToken,testDto)

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

        assertEquals("This user is not an developer of this project", PlayerIsNotAdminException.message)
    }

    @Test
    fun updateSprintNoOfTasksRightToken() {
        val request : TaskDto = testDtoUpdated
        val testListOfTasks : ArrayList<TaskDto> = ArrayList()
        testListOfTasks.add(request)

        val updated : TaskDto = testDtoUpdated

        val origin : TaskDto = testDto
        origin.taskSprintNo = 1

        `when`(tokenDao.checkIfUserIsDeveloper(testToken)).thenReturn(true)
        doAnswer { invocationOnMock ->
            origin.taskSprintNo = request.taskSprintNo
            origin
        }.`when`(taskDao).updateSprintNumberOfRemainingTasks(testListOfTasks)

        taskService.updateSprintNoOfTasks(testToken, testListOfTasks)

        assertEquals(updated.taskSprintNo, origin.taskSprintNo!!)
    }

    @Test
    fun updateSprintNoOfTasksWrongToken(){
        val request : TaskDto = testDtoUpdated
        val testListOfTasks : ArrayList<TaskDto> = ArrayList()
        testListOfTasks.add(request)

        val origin : TaskDto = testDto
        origin.taskSprintNo = 1

        `when`(tokenDao.checkIfUserIsDeveloper(testToken)).thenReturn(true)
        doAnswer { invocationOnMock ->
            origin.taskSprintNo = request.taskSprintNo
            origin
        }.`when`(taskDao).updateSprintNumberOfRemainingTasks(testListOfTasks)

        val exception: UserIsNotDeveloperException = Assertions.assertThrows(UserIsNotDeveloperException::class.java) {  taskService.updateSprintNoOfTasks("",testTasksArray) }

        assertEquals("This user is not an developer of this project", exception.message)
    }

    @Test
    fun getTasksRightToken() {
        `when`(tokenDao.checkIfUserIsDeveloper(testToken)).thenReturn(true)
        `when`(taskDao.getTasksFromDatabase()).thenReturn(taskResponse)

        val actualResponse = taskService.getTasks(testToken)

        assertEquals(taskResponse,actualResponse)
    }

    @Test
    fun getTasksWrongToken(){
        `when`(tokenDao.checkIfUserIsDeveloper(testToken)).thenReturn(true)
        `when`(taskDao.getTasksFromDatabase()).thenReturn(taskResponse)

        val PlayerIsNotAdminException = Assertions.assertThrows(UserIsNotDeveloperException::class.java) { taskService.getTasks( "") }

        assertEquals("This user is not an developer of this project", PlayerIsNotAdminException.message)
    }




}