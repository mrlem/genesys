package domain.model

sealed interface OutputPolicy {

    object Suffixed : OutputPolicy

    data class Designated(val filename: String) : OutputPolicy

}