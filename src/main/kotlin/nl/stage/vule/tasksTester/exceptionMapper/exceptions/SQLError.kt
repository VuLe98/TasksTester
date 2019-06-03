package nl.stage.vule.tasksTester.exceptionMapper.exceptions;

import java.sql.SQLException

class SQLError (message: String) : SQLException(message)

