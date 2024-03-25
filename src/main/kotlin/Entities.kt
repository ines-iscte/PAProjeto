data class Attribute(
    var name: String,
    var value: String
)

class Entity(
    val name: String,
    val text: String = "",
    val attributes: MutableList <Attribute> = mutableListOf(),
    val parent: Entity? = null,
    val children: MutableList<Entity> = mutableListOf()

    ){
    init{
        if (parent != null) {
            parent?.children.add(this)
        }

    }

    // Atributos

    fun addAttribute(attribute: Attribute){
        this.attributes.add(attribute)
    }

    fun removeAttribute(attribute: Attribute){
        attributes.forEach(){
            if(it == attribute)
                attributes.remove(it)
        }
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

    // Entidade mãe e entidades aninhadas

    fun get_Parent(): Entity?{
        return this.parent
    }

    fun get_Children() : List<Entity> {
        var list: MutableList<Entity> = mutableListOf()
        this.children.forEach {
            if (it is Entity) {
                list.add(it)
                list.addAll(it.get_Children())
            }
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
){

    fun addEntity(entity: Entity){
        entities.add(entity)
    }

    fun removeEntity(entity: Entity){
        entities.forEach(){
            if(it == entity)
                entities.remove(it)
        }
    }
}


//data class InnerEntity(
//    override val name: String,
//    override val text: String = "",
//    override val attributes: MutableList <Attribute> = mutableListOf(),
//    val parentEntity: Entity
//): Entity(name, text, attributes)



