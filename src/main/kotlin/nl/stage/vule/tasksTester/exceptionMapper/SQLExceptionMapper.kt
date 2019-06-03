package nl.stage.vule.tasksTester.exceptionMapper

import nl.stage.vule.tasksTester.exceptionMapper.exceptions.SQLError
import nl.stage.vule.tasksTester.services.dto.ExceptionDto
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class SQLExceptionMapper : ExceptionMapper<SQLError> {

    override fun toResponse(e: SQLError): Response {
        return Response.status(Response.Status.BAD_GATEWAY).entity(ExceptionDto(e.message)).build()
    }
}
