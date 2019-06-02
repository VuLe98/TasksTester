package nl.han.ica.oose.roubaix.planningspoker.datasources.jdbc

import java.io.IOException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement
import java.util.*

class DatabaseConnection {

    private var properties: Properties? = null

    companion object {
        var connection: Connection? = null

        fun getInstance(): Connection? {
            return if (connection == null) {
                DatabaseConnection().connect()
            } else {
                connection
            }
        }
    }

    init{
        try {
            Class.forName("com.mysql.cj.jdbc.Driver")
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }

    }

    fun connect(): Connection {
        if (properties == null) {
            properties = readProperties()
        }
        try {
            return DriverManager.getConnection(
                    properties!!.getProperty("db.url"),
                    properties!!.getProperty("db.user"),
                    properties!!.getProperty("db.password"))

        } catch (e: SQLException) {
            throw RuntimeException(e)
        }

    }

    private fun readProperties(): Properties {
        val properties = Properties()
        try {
            properties.load(javaClass.getResourceAsStream("/database.properties"))
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

        return properties
    }

}
