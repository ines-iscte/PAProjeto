/**
 * @author Ana Mercês Soares dos Reis Moreira - nº99352
 * @author Inês Colaço Ascenso - nº99286
 */

/**
 * Class that represents an attribute with a name and a value.
 * @property[name] Name of the attribute.
 * @property[value] Value of the attribute.
 */
class Attribute(
    private var name: String?,
    private var value: String?
){
    /**
     * Initializes an instance of Attribute.
     *
     * If the name given has more than one word, the attribute is not created
     * @param[name] The name given to the attribute
     */
    init {
        require(name?.split(" ")?.size == 1) {
            "Name must contain only one word"
        }
    }

    /**
     * Returns a String representation of the attribute.
     * @return A String representation of the attribute.
     */
    override fun toString(): String {
        return "Attribute(name='$name', value='$value')"
    }

    fun get_attribute_name(): String? {
        return name
    }

    fun get_attribute_value(): String? {
        return value
    }

    fun set_attribute_name(name: String) {
        this.name = name
    }

    fun set_attribute_value(value: String) {
        this.value = value
    }


    /**
     * Verifies if the attribute in question is equal to another attribute.
     *
     * @param[other] The other object of the comparison.
     * @return True: if the other object is also an Attribute and has the same name and value as the first one.
     * False: otherwise.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Attribute) return false

        if (name != other.name || value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + value.hashCode()
        return result
    }

}