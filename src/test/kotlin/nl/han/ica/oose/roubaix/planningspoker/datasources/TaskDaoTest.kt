package nl.han.ica.oose.roubaix.planningspoker.datasources

import nl.han.ica.oose.roubaix.planningspoker.datasources.jdbc.DatabaseConnection
import nl.han.ica.oose.roubaix.planningspoker.services.TaskResponse
import nl.han.ica.oose.roubaix.planningspoker.services.dto.TaskDto
import nl.han.ica.oose.roubaix.planningspoker.services.jira.JiraService
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

    lateinit var jiraServiceMock: JiraService

    private var taskResponse: TaskResponse? = null

    private var testToken = "ABCDEF-1"
    val testDto: TaskDto = TaskDto(taskID = TASKID, taskDescription = "TESTBESCHRIJVING", taskDuration = DURATION, taskSprintNo = 1, taskDeveloper = testToken)

    @BeforeEach
    fun setUp() {

        sut = mock(TaskDao::class.java)

        preparedStatement = mock(PreparedStatement::class.java)
        resultSet = mock(ResultSet::class.java)
        sqlException = mock(SQLException::class.java)

        dbConnection = mock(DatabaseConnection::class.java)
        connection = mock(Connection::class.java)

        jiraServiceMock = mock(JiraService::class.java)

        `when`(preparedStatement.executeQuery()).thenReturn(resultSet)
        `when`(connection.prepareStatement(anyString())).thenReturn(preparedStatement)

        sut = TaskDao(connection)

        taskResponse?.tasks?.add(testDto)
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
    fun doesExportTasksToDatabaseUseSetString() {
        //Setup


        //Test
        sut.addTaskToDatabase(testDto)

        //Verify
        verify(preparedStatement).setString(1, testDto.taskID)
    }

    @Test
    @Throws(SQLException::class)
    fun doesExportTasksToDatabaseExecuteUpdate() {
        //Setup


        //Test
        sut.addTaskToDatabase(testDto)

        //Verify
        verify(preparedStatement).executeUpdate()
    }

    @Test
    @Throws(SQLException::class)
    fun doesExportTasksToDatabaseHandleSQLExceptionCorrect() {
        //Setup
        `when`(connection.prepareStatement(anyString())).thenThrow(sqlException)

        //Test
        sut.addTaskToDatabase(testDto)

        //Verify
        verify(sqlException).printStackTrace()
    }
}
