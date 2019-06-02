package nl.han.ica.oose.roubaix.planningspoker.exceptionMapper

import nl.han.ica.oose.roubaix.planningspoker.exceptionMapper.exceptions.EmptyParameterException
import nl.han.ica.oose.roubaix.planningspoker.services.dto.ExceptionDto
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class EmptyParameterExceptionMapper : ExceptionMapper<EmptyParameterException> {

    override fun toResponse(e: EmptyParameterException): Response {
        return Response.status(Response.Status.BAD_REQUEST).entity(ExceptionDto(e.message)).build()
    }
}