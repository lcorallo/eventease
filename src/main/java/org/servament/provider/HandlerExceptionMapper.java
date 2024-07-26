package org.servament.provider;

import org.servament.dto.ErrorResponseDTO;
import org.servament.exception.EventEaseException;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class HandlerExceptionMapper implements ExceptionMapper<EventEaseException> {

    @Context
    private UriInfo uriInfo;
    //Source: https://blog.payara.fish/returning-beautiful-validation-error-messages-in-jakarta-rest-with-exception-mappers

    @Override
    public Response toResponse(EventEaseException e) {
        Status errorResponseStatus;
        ErrorResponseDTO error = new ErrorResponseDTO(e.getErrorCode(), e.getMessage(), e.getCause() != null ? e.getCause().getMessage() : null);
        String exceptionClassName = e.getClass().getSimpleName();

        switch (exceptionClassName) {
            case "EventServiceIllegalInputException":
            case "EventOperationIllegalInputException":
                errorResponseStatus = Status.BAD_REQUEST;
                break;
            case "EventPublicationException":
            case "EventClosingException":
            case "EventCompletingException":
                errorResponseStatus = Status.FORBIDDEN;
                break;
            case "EventServiceNotFoundException":
            case "EventOperationNotFoundException":
            case "BookingnNotFoundException":
                errorResponseStatus = Status.NOT_FOUND;
                break;
            default:
                errorResponseStatus = Status.INTERNAL_SERVER_ERROR;
                break;
        }

        return Response.status(errorResponseStatus).entity(error).build();

    }
    
}
