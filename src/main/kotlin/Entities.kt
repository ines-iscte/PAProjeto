import java.io.File

class Attribute(
    var name: String,
    var value: String
){
    override fun toString(): String {
        return "Attribute(name='$name', value='$value')"
    }
}

class Entity(
    var name: String,
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

    //Dunno
    fun accept(visitor: (Entity) -> Boolean){
        visitor(this)
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

    fun get_attributes(): MutableList<Attribute>{
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

//fun Document.add_global_attribute_vis(entity_name: String, attribute_name: String, attribute_value: String){
//    var att: Attribute? = null
//    this.entities.forEach(){
//        var att_list = it.get_attributes()
//        att_list.forEach(){
//            if (it.name==entity_name && it.value==attribute_value) {
//                att = it
//                return@forEach
//            }
//        }
//    }
//    if (att == null)
//        att = Attribute(name=attribute_name, value=attribute_value)
//
//    val attCopy = att
//
//    // Para preservar a variável att, que pode ser modificada dentro da expressão do visitor
//    this.accept {
//            if (entity_name == it.name && attCopy != null)
//                it.addAttribute(attCopy!!)
//            true
//    }
//}

fun Document.add_global_attribute_vis(entity_name: String, attribute_name: String, attribute_value: String) {
    var att: Attribute? = null

    // Procura por um Attribute com o mesmo nome e valor em cada entidade
    this.entities.forEach { entity ->
        val att_list = entity.get_attributes()
        val matchingAttribute = att_list.find { it.name == attribute_name && it.value == attribute_value }
        if (matchingAttribute != null) {
            att = matchingAttribute
            return@forEach
        }
    }

    // Se não foi encontrado um Attribute, cria um novo
    if (att == null) {
        att = Attribute(name = attribute_name, value = attribute_value)
    }

    // Adiciona o Attribute à lista de atributos de cada entidade
    this.accept {
        if (entity_name == it.name && att != null) {
            it.addAttribute(att!!) // Copia o Attribute para garantir instâncias separadas
        }
        true
    }
}

// Ponto 7
fun Document.rename_global_entity_vis(old_name: String, new_name:String){
    this.accept {
        if (old_name == it.name) {
            it.name=new_name
        }
        true
    }
}

// Ponto 8
fun Document.rename_global_attributes_vis(entity_name: String, old_attribute_name: String, new_attribute_name: String){
    this.accept {
        if (entity_name == it.name) {
            it.attributes.forEach(){
                if (it.name==old_attribute_name)
                    it.name=new_attribute_name
            }
        }
        true
    }
}

// Ponto 9
fun Document.remove_global_entities_vis(entity_name: String) {
    this.accept { entity ->
        if (entity.name == entity_name) {
            entities.remove(entity)
        }
        true
    }
}

// Ponto 10
//fun Document.remove_global_attributes_vis(entity_name: String, attribute_name: String){
//    this.accept { entity ->
//        if (entity_name == entity.name) {
//            entity.attributes.forEach(){
//                if (it.name==attribute_name)
//                    entity.attributes.remove(it)
//            }
//        }
//        true
//    }
//}

fun Document.remove_global_attributes_vis(entity_name: String, attribute_name: String) {
    this.accept { entity ->
        if (entity_name == entity.name) {
            val attributesToRemove = entity.attributes.filter { it.name == attribute_name }
            attributesToRemove.forEach { attribute ->
                entity.attributes.remove(attribute)
            }
        }
        true
    }
}

class Document(
    var name: String,
    val entities: MutableList <Entity> = mutableListOf()
) {

    // Dunno x2
    fun accept(visitor: (Entity) -> Boolean) {
        entities.forEach {
                it.accept(visitor)
            }
    }

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

    // Ponto 6.
    fun add_global_attribute(entity_name: String, attribute_name: String, attribute_value: String){
        var att: Attribute? = null
        this.entities.forEach(){
            var att_list = it.get_attributes()
            att_list.forEach(){
                if (it.name==entity_name && it.value==attribute_value) {
                    att = it
                    return@forEach
                }
            }
        }
        if (att == null)
            att = Attribute(name=attribute_name, value=attribute_value)

        val attCopy = att

        entities.forEach () {
            if (entity_name == it.name) {
                if (attCopy != null) {
                    it.addAttribute(attCopy)
                }
            }
        }
    }

    // Ponto 7
    fun rename_global_entity(old_name: String, new_name:String){
        this.entities.forEach(){
            if (old_name == it.name) {
                it.name=new_name
            }
        }
    }

    // Ponto 8
    fun rename_global_attributes(entity_name: String, old_attribute_name: String, new_attribute_name: String){
        entities.forEach(){
            if (entity_name == it.name) {
                it.attributes.forEach(){
                    if (it.name==old_attribute_name)
                        it.name=new_attribute_name
                }
            }
        }
    }

    // Ponto 9
    fun remove_global_entities(entity_name: String){
        val list: MutableList <Entity> = mutableListOf()
        entities.forEach(){
            if (entity_name == it.name) {
                list.add(it)
            }
        }
        entities.removeAll(list)
    }

    // Ponto 10
//    fun remove_global_attributes(entity_name: String, attribute_name: String) {
//        val list: MutableList <Entity> = mutableListOf()
//        entities.forEach(){
//            if (entity_name == it.name) {
//                it.attributes.forEach(){
//                    if (it.name == attribute_name)
//                        list.add(it)
//                }
//                it.attributes.removeAll(list)
//            }
//        }
//    }

    fun remove_global_attributes(entity_name: String, attribute_name: String) {
        entities.forEach { entity ->
            if (entity_name == entity.name) {
                val attributesToRemove = entity.attributes.filter { it.name == attribute_name }
                entity.attributes.removeAll(attributesToRemove)
            }
        }
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



