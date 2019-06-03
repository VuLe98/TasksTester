package nl.stage.vule.tasksTester.datasources

import nl.stage.vule.tasksTester.datasources.jdbc.DatabaseConnection
import nl.stage.vule.tasksTester.exceptionMapper.exceptions.SQLError
import nl.stage.vule.tasksTester.services.TaskResponse
import nl.stage.vule.tasksTester.services.dto.TaskDto
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

internal class TaskDaoTest {
    val TASKID: String = "TEST-1234"
    val DURATION: Int = 123

    lateinit var sut: TaskDao
    lateinit var dbConnection: DatabaseConnection
    lateinit var connection: Connection
    lateinit var preparedStatement: PreparedStatement
    lateinit var resultSet: ResultSet

    lateinit var sqlException: SQLException

    private var taskResponse: TaskResponse? = null

    private var testTasksArray : ArrayList<TaskDto> = ArrayList()

    private var testToken = "ABCDEF-1"
    val testDto: TaskDto = TaskDto(taskID = TASKID, taskDescription = "TESTBESCHRIJVING", taskDuration = DURATION, taskSprintNo = 1, taskDeveloper = testToken)

    val testDtoUpdated: TaskDto = TaskDto(taskID = TASKID, taskDescription = "TESTBESCHRIJVING", taskDuration = DURATION, taskSprintNo = 2, taskDeveloper = testToken)

    @BeforeEach
    fun setUp() {

        sut = mock(TaskDao::class.java)

        preparedStatement = mock(PreparedStatement::class.java)
        resultSet = mock(ResultSet::class.java)
        sqlException = mock(SQLException::class.java)

        dbConnection = mock(DatabaseConnection::class.java)
        connection = mock(Connection::class.java)

        `when`(preparedStatement.executeQuery()).thenReturn(resultSet)
        `when`(connection.prepareStatement(anyString())).thenReturn(preparedStatement)

        sut = TaskDao(connection)

        testTasksArray.add(testDto)

        taskResponse?.tasks?.add(testDtoUpdated)
    }

    @Test
    @Throws(SQLException::class)
    fun doesEndpointDelegateToDBConnection() {
        //Setup

        //Test
        sut.addTaskToDatabase(testDto)

        //Verify
        verify(connection).prepareStatement(anyString())
    }

    @Test
    @Throws(SQLException::class)
    fun doesAddTaskToDatabaseUseSetString() {
        //Setup

        //Test
        sut.addTaskToDatabase(testDto)

        //Verify
        verify(preparedStatement).setString(2, testDto.taskDescription)
    }

    @Test
    @Throws(SQLException::class)
    fun doesAddTaskToDatabaseExecuteUpdate() {
        //Setup
        testTasksArray.add(testDto)

        //Test
        sut.addTaskToDatabase(testDto)

        //Verify
        verify(preparedStatement).executeUpdate()
    }

    @Test
    @Throws(SQLException::class)
    fun doesAddTaskToDatabaseHandleSQLExceptionCorrect() {
        //Setup
        `when`(connection.prepareStatement(anyString())).thenThrow(sqlException)

        //Test
        sut.addTaskToDatabase(testDto)

        //Verify
        verify(sqlException).printStackTrace()
    }

    @Test
    @Throws(SQLException::class)
    fun doesUpdateSprintNumberOfRemainingTasksFromDatabaseUseSetString() {
        //Setup


        //Test
        sut.updateSprintNumberOfRemainingTasks(testTasksArray)

        //Verify
        verify(preparedStatement).setString(2, testDto.taskID)
    }

    @Test
    @Throws(SQLException::class)
    fun doesUpdateSprintNumberOfRemainingTasksFromDatabaseExecuteUpdate() {
        //Setup


        //Test
        sut.updateSprintNumberOfRemainingTasks(testTasksArray)

        //Verify
        verify(preparedStatement).executeUpdate()
    }

    @Test
    @Throws(SQLException::class)
    fun doesUpdateSprintNumberOfRemainingTasksFromDatabaseHandleSQLExceptionCorrect(){
        `when`(connection.prepareStatement(anyString())).thenThrow(sqlException)

        val exception: SQLError = Assertions.assertThrows(SQLError::class.java) {  sut.updateSprintNumberOfRemainingTasks(testTasksArray) }

        verify(sqlException).printStackTrace()

        Assertions.assertEquals("Something went wrong in the database", exception.message)
    }

    @Test
    @Throws(SQLException::class)
    fun doesGetCurrentSprintOfTaskGetInfo(){
        `when`(resultSet.next()).thenReturn(true).thenReturn(false)
        `when`(resultSet.getInt("SPRINTNO")).thenReturn(testDto.taskSprintNo)

        sut.getCurrentSprintOfTask(testDto.taskID!!)

        verify(resultSet).getInt("SPRINTNO")
    }

    @Test
    @Throws(SQLException::class)
    fun doesGetCurrentSprintOfTaskHandleSQLExceptionCorrect(){
        `when`(connection.prepareStatement(anyString())).thenThrow(sqlException)

        val exception: SQLError = Assertions.assertThrows(SQLError::class.java) {  sut.getCurrentSprintOfTask(testDto.taskID!!) }

        verify(sqlException).printStackTrace()

        Assertions.assertEquals("Something went wrong in the database", exception.message)
    }

    @Test
    @Throws(SQLException::class)
    fun doesGetTasksFromDatabaseGetInfo() {
        `when`(resultSet.next()).thenReturn(true).thenReturn(false)
        `when`(resultSet.getString("DESCRIPTION")).thenReturn(testDto.taskDescription)
        `when`(resultSet.getInt("DURATION")).thenReturn(testDto.taskDuration)
        `when`(resultSet.getString("TASKID")).thenReturn(testDto.taskID)
        `when`(resultSet.getInt("SPRINTNO")).thenReturn(testDto.taskSprintNo)
        `when`(resultSet.getString("DEVELOPER")).thenReturn(testDto.taskDeveloper)

        //Test
        sut.getTasksFromDatabase()

        //Verify
        verify(resultSet).getString("DESCRIPTION")
        verify(resultSet).getInt("DURATION")
        verify(resultSet).getString("TASKID")
        verify(resultSet).getInt("SPRINTNO")
        verify(resultSet).getString("DEVELOPER")
    }

    @Test
    @Throws(SQLException::class)
    fun doesGetTasksFromDatabaseExecuteQuery() {
        //Setup


        //Test
        sut.getTasksFromDatabase()

        //Verify
        verify(preparedStatement).executeQuery()
    }

    @Test
    @Throws(SQLException::class)
    fun doesGetTasksFromDatabaseHandleSQLExceptionCorrect() {
        //Setup
        `when`(connection.prepareStatement(anyString())).thenThrow(sqlException)

        //Test
        sut.getTasksFromDatabase()

        //Verify
        verify(sqlException).printStackTrace()
    }
}
