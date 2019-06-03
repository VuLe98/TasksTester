package nl.stage.vule.tasksTester.exceptionMapper

import nl.stage.vule.tasksTester.exceptionMapper.exceptions.EmptyParameterException
import nl.stage.vule.tasksTester.services.dto.ExceptionDto
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class EmptyParameterExceptionMapper : ExceptionMapper<EmptyParameterException> {

    override fun toResponse(e: EmptyParameterException): Response {
        return Response.status(Response.Status.BAD_REQUEST).entity(ExceptionDto(e.message)).build()
    }
}