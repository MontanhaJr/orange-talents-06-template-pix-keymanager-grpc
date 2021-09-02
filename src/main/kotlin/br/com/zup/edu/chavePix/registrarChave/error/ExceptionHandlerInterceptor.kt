package br.com.zup.edu.chavePix.registrarChave.error

import com.google.rpc.BadRequest
import io.grpc.BindableService
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.protobuf.StatusProto
import io.grpc.stub.StreamObserver
import io.micronaut.aop.InterceptorBean
import io.micronaut.aop.MethodInterceptor
import io.micronaut.aop.MethodInvocationContext
import org.slf4j.LoggerFactory
import java.lang.IllegalArgumentException
import javax.inject.Singleton
import javax.validation.ConstraintViolationException

@Singleton
@InterceptorBean(ErrorHandler::class)
class ExceptionHandlerInterceptor : MethodInterceptor<BindableService, Any?> {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun intercept(context: MethodInvocationContext<BindableService, Any?>?): Any? {
        try {
            logger.info("Intercepted")
            return context?.proceed()
        } catch (e: Exception) {
            e.printStackTrace()

            val statusError = when (e) {
                is IllegalArgumentException -> Status.INVALID_ARGUMENT.withDescription(e.message).asRuntimeException()
                is IllegalStateException -> Status.FAILED_PRECONDITION.withDescription(e.message).asRuntimeException()
                is ChavePixJaCadastradaException -> Status.ALREADY_EXISTS.withDescription(e.message).asRuntimeException()
                is ConstraintViolationException -> handleConstraintViolationException(e)
                else -> Status.UNKNOWN.withDescription("Erro Inesperado").asRuntimeException()
            }

            val responseObserver = context!!.parameterValues[1] as StreamObserver<*>
            responseObserver.onError(statusError)
            return null
        }
    }

    private fun handleConstraintViolationException(e: ConstraintViolationException): StatusRuntimeException {

        val details = BadRequest.newBuilder()
            .addAllFieldViolations(e.constraintViolations.map {
                BadRequest.FieldViolation.newBuilder()
                    .setField(it.propertyPath.last().name)
                    .setDescription(it.message)
                    .build()
            })
            .build()

        val statusProto = com.google.rpc.Status.newBuilder()
            .setCode(Status.INVALID_ARGUMENT.code.value())
            .setMessage("Parâmetros de entrada inválidos")
            .addDetails(com.google.protobuf.Any.pack(details))
            .build()

        return StatusProto.toStatusRuntimeException(statusProto)
    }
}