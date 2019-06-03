package nl.stage.vule.tasksTester.exceptionMapper

import nl.stage.vule.tasksTester.exceptionMapper.exceptions.UserIsNotDeveloperException
import nl.stage.vule.tasksTester.services.dto.ExceptionDto
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class UserIsNotDeveloperExceptionMapper : ExceptionMapper<UserIsNotDeveloperException> {

    override fun toResponse(e: UserIsNotDeveloperException): Response {
        return Response.status(Response.Status.FORBIDDEN).entity(ExceptionDto(e.message)).build()
    }
}