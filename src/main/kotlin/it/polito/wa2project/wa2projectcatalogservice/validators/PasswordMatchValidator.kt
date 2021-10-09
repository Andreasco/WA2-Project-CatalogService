package it.polito.wa2project.wa2projectcatalogservice.validators

import org.springframework.beans.BeanWrapperImpl
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [PasswordValueMatchValidator::class])
@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class PasswordValueMatch(
    val message: String = "Password values don't match",
    val password: String,
    val confirmPassword: String,

    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)


class PasswordValueMatchValidator: ConstraintValidator<PasswordValueMatch, Any>{
    private var password:String = ""
    private var confirmPassword:String = ""


    override fun initialize(constraintAnnotation: PasswordValueMatch) {
        super.initialize(constraintAnnotation)
        this.password = constraintAnnotation.password
        this.confirmPassword = constraintAnnotation.confirmPassword
    }

    override fun isValid(value: Any, context: ConstraintValidatorContext?): Boolean {
        val password: String = BeanWrapperImpl(value).getPropertyValue(password) as String
        val passwordMatch: String = BeanWrapperImpl(value).getPropertyValue(confirmPassword) as String
        println(">> IsValid PasswordValueMatchValidator")
        println(">> password: $password")
        println(">> confirmPassword: $passwordMatch")
        println(">> password === passwordMatch: ${password == passwordMatch}")
        return password == passwordMatch
    }
}
