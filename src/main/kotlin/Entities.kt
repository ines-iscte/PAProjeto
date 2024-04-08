import java.io.File

class Attribute(
    var name: String,
    var value: String
)

class Entity(
    val name: String,
    var text: String = "",
    val attributes: MutableList <Attribute> = mutableListOf(),
    val parent: Entity? = null,
    val children: MutableList<Entity> = mutableListOf()

    ){
    init{
        if (parent != null) {
            parent.children.add(this)
        }

    }

    // Atributos

    fun addAttribute(attribute: Attribute){
        this.attributes.add(attribute)
    }

    fun removeAttribute(attribute: Attribute){
//        attributes.forEach(){
//            if(it == attribute)
//                attributes.remove(it)
//        }
        attributes.removeIf { it == attribute }
    }

    fun changeAttribute(attribute: Attribute, new_name: String? = null, new_value: String? = null) {
        attributes.forEach {
            if (it == attribute) {
                if (new_name != null) {
                    it.name = new_name
                }
                if (new_value != null) {
                    it.value = new_value
                }
            }
        }
    }

    fun get_attribute(): MutableList<Attribute>{
        return this.attributes
    }

    // Texto
    fun get_entity_text(): String{
        return this.text
    }

    fun change_entity_text(new_text: String){
        text=new_text
    }

    fun remove_entity_text(){
        text=""
    }

    // Entidade mãe e entidades aninhadas

    fun get_Parent(): Entity?{
        return this.parent
    }

    fun get_Children() : List<Entity> {
        val list: MutableList<Entity> = mutableListOf()
        this.children.forEach {
            //if (it is Entity) {
            list.add(it)
            list.addAll(it.get_Children())
            //}
        }
        return list
    }

    fun get_Parent_and_Children(): List<Entity>{

        val list = mutableListOf<Entity>()
        // Adiciona o pai
        this.get_Parent()?.let { parent ->
            list.add(parent)
        }
        // Adiciona os filhos
        list.addAll(this.get_Children())

        return list
    }
}

class Document(
    val name: String,
    val entities: MutableList <Entity> = mutableListOf()
) {

    fun addEntity(entity: Entity) {
        entities.add(entity)
    }

    fun removeEntity(entity: Entity) {
//        entities.forEach() {
//            if (it == entity)
//                entities.remove(it)
//        }
        entities.removeIf { it == entity }
    }

    fun prettyPrint(entity: Entity, indent: String = ""): String {
        val stringBuilder = StringBuilder()

        stringBuilder.append("$indent<${entity.name}")
        if (entity.attributes.isNotEmpty()) {
            entity.attributes.forEach { attribute ->
                stringBuilder.append(" ${attribute.name}=\"${attribute.value}\"")
            }
        }

        //stringBuilder.append(">")
        // Tirar esta identação se for para ter tag no fim
        if (entity.children.isEmpty() && entity.text.isEmpty()) {
            stringBuilder.append("/>")
        } else {
            stringBuilder.append(">")

            if (entity.text.isNotEmpty())
                stringBuilder.append("${entity.text}")

            if (entity.children.isNotEmpty()) {
                stringBuilder.appendLine()
                entity.children.forEach { child ->
                    stringBuilder.append(prettyPrint(child, "$indent    "))
                    stringBuilder.appendLine()
                }
                stringBuilder.append("$indent")
            }

            // WTF porque é que "componente" não tem tag de fecho
            // Que estupidez i swear
            if (entity.children.isNotEmpty() || entity.text.isNotEmpty()) {
                stringBuilder.append("</${entity.name}>")
            }
        }
        return stringBuilder.toString()
    }

    fun prettyPrintToFile(entity: Entity, indent: String = "", outputFile: File) {
        val xmlString = prettyPrint(entity, indent)
        outputFile.writeText(xmlString)
    }

}

//    fun prettyPrintDocument(): String {

//        var file: String
//
//        file = """<?xml version="1.0" encoding="UTF-8"?>"""
//        entities.forEach {
//            //if (it.children.isEmpty()) {
//            file.plus("<" + it.name + ">")
//            if (it.text != "")
//                file.plus(it.text)
//            if (!it.attributes.isEmpty())
//                it.attributes.forEach {
//                    file.plus(it.name + "=" + it.value)
//                }
//
//            file.plus("</" + it.name + ">")
//            //}
//            file.plus("<" + it.name + ">")
//
//        }
//    return file
//    }
//
//
//}


//data class InnerEntity(
//    override val name: String,
//    override val text: String = "",
//    override val attributes: MutableList <Attribute> = mutableListOf(),
//    val parentEntity: Entity
//): Entity(name, text, attributes)



