import Result.*
import kotlin.random.Random

fun main(){
    val response = ApiResponse()
    val result = compose(response)
    if (result)
        println(response.body)
}

fun compose(apiResponse: ApiResponse): Boolean {
    return validApiResponse(apiResponse)
        .then(::validApiResponseBody)
        .otherwise(::error)
}


infix fun <T, R> Result<T>.then(f: (T) -> Result<R>) = when(this){
    is Success -> f(this.value)
    is Failure -> Failure(this.throws)
}

infix fun <T> Result<T>.otherwise(f: (Throwable) -> Unit): Boolean {
    return if (this is Failure) {
        f(this.throws)
        false
    } else
        true
}

fun error(throws: Throwable): Unit = throw Exception(throws)

fun validApiResponse(response: ApiResponse): Result<ApiResponse> =
    if (response.isSuccessful())
        Success(response)
    else
        Failure(Throwable("Api is no reachable"))

fun validApiResponseBody(response: ApiResponse): Result<ApiResponse> =
    if (response.body.isNullOrEmpty())
        Failure(Throwable("Body from response is empty"))
    else
        Success(response)

sealed class Result<T>{
    data class Success<T>(val value: T): Result<T>()
    data class Failure<T>(val throws: Throwable): Result<T>()
}

data class ApiResponse(val complete: Boolean = Random.nextInt(0, 10)> 11,
                       val body: String? = """{'data': 'The response body is not Null'}"""){

    fun isSuccessful(): Boolean = complete
}