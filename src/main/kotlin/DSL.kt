operator fun Entity.get(n: String): Any? {
    val child = this.get_children().find { it.get_name() == n }
    if (child != null) {
        return child
    } else {
        val attribute = this.get_attributes().find { it.get_attribute_name() == n }
        if (attribute != null) {
            return attribute
        }
    }
    return null
}

fun entity(name: String, text: String = "", build: Entity.() -> Unit ): Entity =
    Entity(name = name, text = text).apply{
        build(this)
    }

fun Entity.attribute(name: String, value: String) = this.add_attribute(Attribute(name, value))

// .() substitui o this na função, para chamar o parent
fun Entity.entity(name: String, text: String="", build: Entity.() -> Unit ) =
    Entity(name = name, text = text, parent = this).apply {
        build(this)
    }

fun Entity.toTree(indent: String = ""): String {
    val builder = StringBuilder()
    if (this.get_entity_text() != "") {
        builder.append("$indent${this.get_name()} (Text = ${this.get_entity_text()})\n")
    } else {
        builder.append("$indent${this.get_name()}\n")
    }
    for (att in this.get_attributes())
        builder.append("$indent$att\n")
    for (child in this.get_children()) {
        builder.append(child.toTree("$indent  "))
    }
    return builder.toString()
}