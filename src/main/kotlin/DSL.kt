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

fun entity(name: String, text: String = "", build: Entity.() -> Unit ): Entity =
    Entity(name = name, text = text).apply{
        build(this)
    }

fun Entity.attribute(name: String, value: String) = this.addAttribute(Attribute(name, value))

// .() substitui o this na função, para chamar o parent
fun Entity.entity(name: String, text: String="", build: Entity.() -> Unit ) =
    Entity(name = name, text = text, parent = this).apply {
        build(this)
    }

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