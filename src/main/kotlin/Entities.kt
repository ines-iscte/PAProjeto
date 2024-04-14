import java.io.File

class Attribute(
    var name: String,
    var value: String
){
    override fun toString(): String {
        return "Attribute(name='$name', value='$value')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Attribute) return false

        if (name != other.name || value != other.value) return false

        return true
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

    // Ao contar com os filhos das entidades filhas
    fun get_all_Children() : List<Entity> {
        val list: MutableList<Entity> = mutableListOf()
        this.children.forEach {
            //if (it is Entity) {
            list.add(it)
            list.addAll(it.get_all_Children())
            //}
        }
        return list
    }

    // Sem contar com os filhos das entidades filhas
    fun get_Children() : List<Entity> {
        return this.children
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

    // Ponto 6
    fun add_global_attribute(entity_name: String, attribute_name: String, attribute_value: String) {
        // Procura ou cria o atributo
        val attribute = Attribute(attribute_name, attribute_value)

        // Adiciona o atributo a todas as entidades com o nome especificado
        this.entities.filter { it.name == entity_name }
            .forEach {
                it.addAttribute(attribute)
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

// Funções usando objetos Visitor (Ponto 5)

// Relativas a Entidades, usando Documento

// As funções têm de estar relacionada com um Documento, de modo a serem percorridas as entidades disponíveis no mesmo,
// ao ser usado o método de Visitante.

fun Document.get_Parent_vis(child_entity: Entity): Entity?{
    var result: Entity? = null
    this.accept { entity ->
        if (entity == child_entity.get_Parent()) {
            result = entity
        }
        true
    }
    return result
}

fun Document.get_Children_vis(parent_entity: Entity) : List<Entity> {
    val list = mutableListOf<Entity>()

    this.accept { entity ->
        if (entity.parent == parent_entity){
            list.add(entity)
        }

        true
    }
    return list
}

fun Document.get_Parent_and_Children_vis(main_entity: Entity): List<Entity>{

    val list = mutableListOf<Entity>()

    this.accept { entity ->
        if (entity in main_entity.get_Children() || entity == main_entity.get_Parent()) {
            list.add(entity)
        }
        true
    }
    return list
}

// Relativas a Documentos

fun Document.add_global_attribute_vis(entity_name: String, attribute_name: String, attribute_value: String) {

    val attribute = Attribute(attribute_name, attribute_value)

    this.accept {
        if (entity_name == it.name) {
            it.addAttribute(attribute)
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

