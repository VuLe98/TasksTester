package nl.han.ica.oose.roubaix.planningspoker.datasources

import nl.han.ica.oose.roubaix.planningspoker.datasources.jdbc.DatabaseConnection
import nl.han.ica.oose.roubaix.planningspoker.exceptionMapper.exceptions.SQLError
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.*
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class TokenDaoTest{
    lateinit var sut: TokenDao
    lateinit var dbConnection: DatabaseConnection
    lateinit var connection: Connection
    lateinit var preparedStatement: PreparedStatement
    lateinit var resultSet: ResultSet

    private val testToken : String =  "ABCDEF-1"
    lateinit var sqlException: SQLException

    @BeforeEach
    @Throws(SQLException::class)
    internal fun setup() {
        sut = mock(TokenDao::class.java)

        preparedStatement = mock(PreparedStatement::class.java)
        resultSet = mock(ResultSet::class.java)
        sqlException = mock(SQLException::class.java)

        dbConnection = mock(DatabaseConnection::class.java)
        connection = mock(Connection::class.java)

        Mockito.`when`(preparedStatement.executeQuery()).thenReturn(resultSet)
        Mockito.`when`(connection.prepareStatement(anyString())).thenReturn(preparedStatement)

        sut = TokenDao(connection)
    }

    @Test
    @Throws(SQLException::class)
    fun doesEndpointDelegateToDBConnection() {
        sut.checkIfUserIsDeveloper(testToken)

        verify(connection).prepareStatement(anyString())
    }

    @Test
    @Throws(SQLException::class)
    fun doesCheckIfPlayerIsAdminUseSetString() {
        sut.checkIfUserIsDeveloper(testToken)

        verify(preparedStatement).setString(1, testToken)

    }

    @Test
    @Throws(SQLException::class)
    fun doesCheckIfPlayerIsAdminExecuteQuery() {
        sut.checkIfUserIsDeveloper(testToken)

        verify(preparedStatement).executeQuery()

    }

    @Test
    @Throws(SQLException::class)
    fun doesCheckIfPlayerIsAdminHandleSQLExceptionCorrect(){
        Mockito.`when`(connection.prepareStatement(anyString())).thenThrow(sqlException)

        val exception: SQLError = Assertions.assertThrows(SQLError::class.java) {  sut.checkIfUserIsDeveloper(testToken) }

        verify(sqlException).printStackTrace()

        Assertions.assertEquals("Something went wrong in the database", exception.message)
    }
}