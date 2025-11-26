package domain.model

sealed interface RootPolicy {

    object FirstIndividual : RootPolicy

    object MostRecent : RootPolicy

    data class Designated(val name: String) : RootPolicy
}