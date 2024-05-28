/**
 * @author Ana Mercês Soares dos Reis Moreira - nº99352
 * @author Inês Colaço Ascenso - nº99286
 */

/**
 * Operator function that allows access to an entity's child an attribute of the entity by name.
 *
 * @param[n] The name of the child or the attribute, depending on if the first one is null or not.
 * @return The child or attribute or null if there is no such child or attribute.
 */
operator fun Entity.get(n: String): Any? {
    val child = this.getChildren().find { it.getName() == n }
    if (child != null) {
        return child
    } else {
        val attribute = this.getAttributes().find { it.getAttributeName() == n }
        if (attribute != null) {
            return attribute
        }
    }
    return null
}

/**
 * Creates a new entity and applies the specified builder function to do it.
 * By introducing the specific parameters: name and text, it builds the new object Entity.
 * @param[name] The name of the new entity.
 * @param[text] The text of the new entity. By default, it will be empty.
 * @param[build] The builder function to apply to the new entity.
 */

fun entity(name: String, text: String = "", build: Entity.() -> Unit ): Entity =
    Entity(name = name, text = text).apply{
        build(this)
    }

/**
 * Adds an attribute to the entity.
 * @param[name] The name of the attribute to add.
 * @param[value] The value of the attribute to add.
 */
fun Entity.attribute(name: String, value: String) = this.addAttribute(Attribute(name, value))

/**
 * Creates a child of the Entity with a certain name and text.
 * Applies the specified builder function to it.
 *
 * @param[name] The name of the new child.
 * @param[text] The text of the new child. By default, it will be empty.
 * @param[build] The builder function to apply to the new child.
 * @return The child entity just created.
 */
fun Entity.entity(name: String, text: String="", build: Entity.() -> Unit ) =
    Entity(name = name, text = text, parent = this).apply {
        build(this)
    }

/**
 * Converts entity, its children and features to  tree-structured String representation.
 * The indentation is supposed to be clear and easy to read its content.
 * @param[indent] The indentation String to use for each level of the tree. By default, it will be empty.
 * @return The String representation of the entity and its children in tree structure.
 */

fun Entity.toTree(indent: String = ""): String {
    val builder = StringBuilder()
    if (this.getEntityText() != "") {
        builder.append("$indent${this.getName()} (Text = ${this.getEntityText()})\n")
    } else {
        builder.append("$indent${this.getName()}\n")
    }
    for (att in this.getAttributes())
        builder.append("$indent$att\n")
    for (child in this.getChildren()) {
        builder.append(child.toTree("$indent  "))
    }
    return builder.toString()
}