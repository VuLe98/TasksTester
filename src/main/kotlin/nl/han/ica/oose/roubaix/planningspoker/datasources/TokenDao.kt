package nl.han.ica.oose.roubaix.planningspoker.datasources

import nl.han.ica.oose.roubaix.planningspoker.datasources.jdbc.DatabaseConnection
import nl.han.ica.oose.roubaix.planningspoker.exceptionMapper.exceptions.SQLError
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class TokenDao constructor(var dbConnection : Connection = DatabaseConnection.getInstance()!!){

    fun checkIfUserIsDeveloper(token : String?) : Boolean{
        try{
            val st : PreparedStatement = dbConnection.prepareStatement("SELECT ISADMIN FROM DEVELOPER WHERE TOKEN = ?")
            st.setString(1,token)

            val resultSet : ResultSet? = st.executeQuery()

            return resultSet!!.next()
        }
        catch(e : SQLException){
            e.printStackTrace()
            throw SQLError("Something went wrong in the database")
        }
    }
}