package nl.han.ica.oose.roubaix.planningspoker.exceptionMapper

import nl.han.ica.oose.roubaix.planningspoker.exceptionMapper.exceptions.InvalidTokenException
import nl.han.ica.oose.roubaix.planningspoker.services.dto.ExceptionDto
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class InvalidTokenExceptionMapper : ExceptionMapper<InvalidTokenException> {

    override fun toResponse(e: InvalidTokenException): Response {
        return Response.status(Response.Status.FORBIDDEN).entity(ExceptionDto(e.message)).build()
    }
}
