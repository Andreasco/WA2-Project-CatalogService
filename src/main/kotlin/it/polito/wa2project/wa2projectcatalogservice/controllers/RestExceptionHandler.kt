package it.polito.wa2project.wa2projectcatalogservice.controllers

import it.polito.wa2project.wa2projectcatalogservice.exceptions.TokenExpiredException
import it.polito.wa2project.wa2projectcatalogservice.exceptions.NotFoundException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.sql.SQLException
import javax.validation.ConstraintViolationException
import javax.validation.ValidationException

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
class RestExceptionHandler: ResponseEntityExceptionHandler(){

    /* These methods are grouped in handleMinorExceptions method

    // Handles entity not found errors in the service
    @ExceptionHandler(NotFoundException::class)
    protected fun handleNotFoundException(e: NotFoundException): ResponseEntity<String>{
        return ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
    }

    // Handles validation errors in the service
    @ExceptionHandler(ValidationException::class)
    protected fun handleValidationException(e: ValidationException): ResponseEntity<String> {
        return ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
    }*/

    /**
     * This handler handles the simplest exceptions, in particular:
     *
     * - Handles entity not found and validation errors in the services.
     *
     * - Handles the exception thrown when the methods in [it.polito.wa2project.wa2projectcatalogservice.services.UserDetailsServiceImpl]
     * cannot find a username.
     *
     * - Handles the exception thrown in [it.polito.wa2project.wa2projectcatalogservice.controllers.AuthenticationController.confirmRegistration]
     * if the token is expired.
     */
    @ExceptionHandler(
        ValidationException::class,
        NotFoundException::class,
        TokenExpiredException::class,
        UsernameNotFoundException::class,
        AccessDeniedException::class
    )
    protected fun handleMinorExceptions(e: Exception): ResponseEntity<String> {
        return ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
    }

    /**
     * Handles validation errors in [it.polito.wa2project.wa2projectcatalogservice.dto.auth.UserRegistrationBodyDTO]
     * and [it.polito.wa2project.wa2projectcatalogservice.dto.auth.LoginRequestBodyDTO].
     *
     * Especially, it handles password matching errors in [it.polito.wa2project.wa2projectcatalogservice.dto.auth.UserRegistrationBodyDTO].
     */
    override fun handleMethodArgumentNotValid(
        e: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        if (e.message != "") // Password matching error
            return ResponseEntity(e.message.substringAfterLast("[").substringBefore("]"),
                HttpStatus.BAD_REQUEST)
        return ResponseEntity(e.fieldErrors.map { it.defaultMessage }, HttpStatus.BAD_REQUEST) //Common field errors
    }

    /**
     * Handles errors during the parsing from requests bodies to objects.
     */
    override fun handleHttpMessageNotReadable(
        e: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        return ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
    }

    /**
     * Handles validation exceptions in [it.polito.wa2project.wa2projectcatalogservice.controllers.WalletController].
     */
    @ExceptionHandler(ConstraintViolationException::class)
    protected fun handleConstraintValidationException(e: ConstraintViolationException): ResponseEntity<String> {
        val errorsString = if (e.message != null) {
            val errorsList = e.message!!.split(",") // I checked that it's not null, i can use !! safely
            val cleanedErrors = errorsList.map { it.substringAfter(": ") }

            cleanedErrors.joinToString()
        } else {
            e.message
        }

        return ResponseEntity(errorsString, HttpStatus.BAD_REQUEST)
    }

    /**
     * Handles SQLExceptions when trying to insert an user with an email that already exists.
     */
    @ExceptionHandler(SQLException::class)
    protected fun handleSQLException(e: SQLException): ResponseEntity<String> {
        return ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(NotFoundException::class)
    protected fun handleNotFoundException(e: NotFoundException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.NOT_FOUND)

    @ExceptionHandler(HttpClientErrorException::class)
    protected fun handleRestCallsExceptions(e: HttpClientErrorException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.BAD_REQUEST)

}// RestExceptionHandler
