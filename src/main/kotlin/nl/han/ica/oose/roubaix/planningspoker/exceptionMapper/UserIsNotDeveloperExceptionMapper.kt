package nl.han.ica.oose.roubaix.planningspoker.exceptionMapper

import nl.han.ica.oose.roubaix.planningspoker.exceptionMapper.exceptions.UserIsNotDeveloperException
import nl.han.ica.oose.roubaix.planningspoker.services.dto.ExceptionDto
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class UserIsNotDeveloperExceptionMapper : ExceptionMapper<UserIsNotDeveloperException> {

    override fun toResponse(e: UserIsNotDeveloperException): Response {
        return Response.status(Response.Status.FORBIDDEN).entity(ExceptionDto(e.message)).build()
    }
}