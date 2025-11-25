package data.local.gedcom.converter

import org.folg.gedcom.model.EventFact

enum class EventType(val tag: String) {

    BIRTH("BIRT"),
    BURIAL("BURI"),
    CHRISTENING("CHR"),
    DEATH("DEAT"),
    OCCUPATION("OCCU"),
    SEX("SEX"),
}

operator fun List<EventFact>.get(type: EventType) = firstOrNull { it.tag == type.tag }