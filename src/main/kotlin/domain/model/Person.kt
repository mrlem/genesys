package domain.model

data class Person(
    val id: String,
    val name: Name = Name(),
    val parents: List<Person> = emptyList(),
    val sex: Sex? = null,
    val occupation: String? = null,
    val birth: Date? = null,
    val death: Date? = null,
)

data class Date(
    val year: Int,
    val exact: Boolean,
) : Comparable<Date> {

    override fun compareTo(other: Date): Int {
        return year.compareTo(other.year)
    }

}

enum class Sex {
    MALE,
    FEMALE,
}