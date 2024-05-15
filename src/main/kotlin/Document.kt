/**
 * @author Ana Mercês Soares dos Reis Moreira - nº99352
 * @author Inês Colaço Ascenso - nº99286
 */

import java.io.File

/**
 * Class that represents a document with a name and a list of entities.
 * @property[entities] List of entities contained in the document.
 */
class Document(
    private var child: Entity,
    private val entities: MutableList <Entity> = mutableListOf(),
    private val encoding: String
) {

    init{
        entities.addAll(child.get_children())
    }

    /**
     * Accepts a visitor function to process this entity and its children.
     *
     * Applies the provided [visitor] function to this entity and recursively to all of its children.
     * The visitor function should return true to continue visiting other entities, or false to stop the process.
     * @param visitor The visitor function to be applied to this entity and its children.
     */
    fun accept(visitor: (Entity) -> Boolean) {
        entities.forEach {
            it.accept(visitor)
        }
    }

    fun get_child(): Entity {
        return child
    }

    fun get_entities(): List<Entity> {
        return entities
    }

    internal fun attribute_exists(entity: Entity, attribute: Attribute): Attribute? {
        return entity.get_attributes().find { it.get_attribute_name() == attribute.get_attribute_name() && it.get_attribute_value() == attribute.get_attribute_value() }
    }

    internal fun entity_exists(name: String, text: String, attributes: MutableList<Attribute>, parent: Entity?, children: List<Entity>): Entity? {
        return get_entities().find { it.get_name() == name && it.get_entity_text() == text && it.get_attributes() == attributes
                && it.get_parent() == parent && it.get_children() == children}
    }

    // Point 1.
    /**
     * Adds an entity to the document.
     * Adds to the list of entities the [entity].
     * @param[entity] The entity to be added to the document.
     */
    fun add_entity_to_document(entity: Entity) {
        if (entity_exists(entity.get_name(), entity.get_entity_text(), entity.get_attributes(), entity.get_parent(), entity.get_children()) != null) {
            throw IllegalStateException("Document already has this entity.")
        } else {
            entities.add(entity)
        }

    }

    // Point 1.
    /**
     * Removes an entity from the document.
     * It goes to the list of entities of the document and removes the one given as [entity].
     * @param[entity] The entity to be removed from the document.
     */
    fun remove_entity(entity: Entity) {
        entities.removeIf { it == entity }
    }

    // Point 4.
    /**
     * Does the pretty print by calling an auxiliary function.
     * @return Call another function that deals with the construction of the structure of the xml File.
     */
//    fun pretty_print(entity: Entity): String {
//        return get_entity_xml(entity = entity, pretty_print = true, encoding)
//    }

    fun pretty_print(): String {
        return get_entity_xml(entity = child, pretty_print = true, encoding)
    }

    // Point 4.
    /**
     * Does the pretty print and write it down in a given File.
     * @param[outputFile] File where the pretty print should be written
     * @return Call another function that deals with the construction of the structure of the xml File and
     * write it in the File.
     */
//    fun pretty_print_to_file(entity: Entity, outputFile: File) {
//        val xmlString = pretty_print(entity)
//        outputFile.writeText(xmlString)
//    }

    fun pretty_print_to_file(outputFile: File) {
        val xmlString = pretty_print()
        outputFile.writeText(xmlString)
    }

    // Point 6.
    /**
     * Adds attributes to the document globally.
     *
     * Creates a new attribute with the [attribute_name] and the [attribute_value].
     * Filters from the document's list of entities those which have the name [entity_name].
     * The attribute just created is added to those entities.
     *
     * @param[entity_name] Name of the entities to which an attribute will be added.
     * @param[attribute_name] Name of the attribute to be added.
     * @param[attribute_value] Value of the attribute to be added.
     */
    fun add_global_attribute(entity_name: String, attribute_name: String, attribute_value: String) {
//        val attribute = Attribute(attribute_name, attribute_value)
//
//        this.entities.filter { it.name == entity_name }
//            .forEach {
//                if (it.attributes.filter{ attribute.name == attribute_name && attribute.value == attribute_value }.size == 0){
//                    it.add_attribute(attribute)
//                } else {
//
//                    print("The entity already has this attribute")
//                }
//
//            }

        require(attribute_name.split(" ").size == 1) {
            "New name must contain only one word"
        }

        val att = Attribute(attribute_name, attribute_value)

        this.entities.filter { it.get_name() == entity_name }
            .forEach {
                if (attribute_exists(it, att) != null) {
                    throw IllegalStateException("Entity already has this attribute.")
                } else {
                    it.add_attribute(att)
                }

            }
    }

    // Point 7.
    /**
     * Renames entities to the document globally.
     *
     * It searches from the document's list of entities those which name is [old_name].
     * Renames those entities with [new_name].
     *
     * @param[old_name] Current name of the entities - to be renamed.
     * @param[new_name] Future name of the entities - to replace the current.
     */
    fun rename_global_entity(old_name: String, new_name:String){
        require(new_name.split(" ").size == 1) {
                "New name must contain only one word"
        }

        this.entities.filter { old_name == it.get_name() }
            .forEach { entity ->
                if (entity_exists(new_name, entity.get_entity_text(), entity.get_attributes(), entity.get_parent(), entity.get_children()) != null) {
                    throw IllegalStateException("Document already has this entity.")
                } else {
                    entity.set_name(new_name)
                }
            }
    }

    // Point 8.
    /**
     * Renames attributes of the document globally.
     *
     * Checks the document's list of entities and verifies if there are any entities with the name [entity_name].
     * If so, goes through the list of attributes of each one of those entities and verifies if there are any attributes
     * with the same name as the [old_attribute_name].
     * If so, renames the current name of that attribute to [new_attribute_name].
     *
     * @param[entity_name] Name of the entities where the attributes are.
     * @param[old_attribute_name] Current name of the attribute - to be renamed.
     * @param[new_attribute_name] Future name of the attribute - to replace the current.
     */
    fun rename_global_attributes(entity_name: String, old_attribute_name: String, new_attribute_name: String){
        require(new_attribute_name.split(" ").size == 1) {
                "New name must contain only one word"
        }
        entities.forEach{ entity ->
            if (entity_name == entity.get_name()) {
                entity.get_attributes().forEach{
                    if (it.get_attribute_name() == old_attribute_name)
                        entity.change_attribute(attribute = it, new_name = new_attribute_name)
                }
//                it.attributes.forEach{
//                    if (it.get_attribute_name()==old_attribute_name)
//                        it.set_attribute_name(new_attribute_name)
//                }

            }
        }
    }

    // Point 9.
    /**
     * Removes the entity from the document globally.
     *
     * Goes through the document's list of entities and verifies if there are any entities with the same name as [entity_name].
     * If so, adds them to the list recently created.
     * At the end, removes the list of unwanted entities from the document's list of entities.
     *
     * @param[entity_name] Name of the entity to be removed.
     */
    fun remove_global_entities(entity_name: String){
        val list: MutableList <Entity> = mutableListOf()

        this.entities.filter { it.get_name() == entity_name }
            .forEach {
                list.add(it)
            }
        entities.removeAll(list)
    }

    // Point 10.
    /**
     * Removes attributes of the document globally.
     *
     * Iterates over the document's list of entities and removes the attributes which name is [attribute_name]
     * and belong to the entities which name is [entity_name].
     *
     * @param[entity_name] Name of the entities which contain the attributes.
     * @param[attribute_name] Name of the attributes to be removed.
     */
    fun remove_global_attributes(entity_name: String, attribute_name: String) {
        entities.forEach { entity ->
            if (entity_name == entity.get_name()) {
                val attributes_to_remove = entity.get_attributes().filter { it.get_attribute_name() == attribute_name }
                entity.get_attributes().removeAll(attributes_to_remove)
            }
        }
    }

    /**
     * Creates a String that contains the elements of each entity (name, text, attributes).
     *
     * If it's called by the "pretty_print" function, the Boolean variable "pretty_print" will be True
     * and so the String of the entity's children will also be added recursively to the structure.
     *
     * @param[entity] The entity where the pretty print should start. If there is no pretty print, it's
     * the entity String.
     * @param[pretty_print] Defines if it's a String or a pretty print structure.
     * @return Structure of the resulting pretty print.
     */
    fun get_entity_xml(entity: Entity, pretty_print: Boolean = false, encoding: String? = null, indent: String = ""): String{
        val stringBuilder = StringBuilder()

        if(encoding != null) {
            stringBuilder.append("<${encoding}>")
            stringBuilder.appendLine()
        }

        stringBuilder.append("$indent<${entity.get_name()}")
        if (entity.get_attributes().isNotEmpty()) {
            entity.get_attributes().forEach { attribute ->
                stringBuilder.append(" ${attribute.get_attribute_name()}=\"${attribute.get_attribute_value()}\"")
            }
        }

        if (entity.get_children().isEmpty() && entity.get_entity_text().isEmpty()) {
            stringBuilder.append("/>")
        } else {
            stringBuilder.append(">")

            if (entity.get_entity_text().isNotEmpty())
                stringBuilder.append(entity.get_entity_text())

            // In case pretty_print is True, it will get the children structure too
            if (pretty_print){
                if (entity.get_children().isNotEmpty()) {
                    stringBuilder.appendLine()
                    entity.get_children().forEach { child ->
                        stringBuilder.append(get_entity_xml(entity=child, pretty_print=true ,indent="$indent    "))
                        stringBuilder.appendLine()
                    }
                    stringBuilder.append(indent)
                }
            }
            if (entity.get_children().isNotEmpty() || entity.get_entity_text().isNotEmpty()) {
                stringBuilder.append("</${entity.get_name()}>")
            }
        }
        return stringBuilder.toString()
    }

    /**
     * For each split in the XPath String ("/"), it searches for the entity name where is equal to the first
     * sub-string of the split (entity most to the left of the split character) - "first_entity".
     *
     * If it finds entities that exist in the [entities_to_explore] list and the entities have the same name as
     * the first sub-string, there are 2 possible scenarios:
     *
     *      1st - the path ends up there, which means the "other_entities" variable is empty and adds the entity to
     * "foundEntities" list.
     *
     *      2nd - there are more entities in the path and so calls the function to its children.
     *
     * At the end of the path, if the entity exists, it is added to the list of foundEntities.
     *
     * @param[x_path] Micro-XPath with the possible path of entities.
     * @param[entities_to_explore] Document's list of entities.
     * @return List of the found entities.
     */
    private fun aux_get_entity_with_x_path(x_path: String, entities_to_explore: List<Entity>): List<Entity> {
    val foundEntities = mutableListOf<Entity>()

    val parts = x_path.split("/")
    val first_entity = parts.first()
    val other_entities = parts.drop(1).joinToString("/")

    val matchingEntities = entities_to_explore.filter { it.get_name() == first_entity }

    matchingEntities.forEach { entity ->
        if (other_entities.isEmpty()) {
            foundEntities.add(entity)
        } else {
            val childEntities = aux_get_entity_with_x_path(other_entities, entity.get_children())
            foundEntities.addAll(childEntities)
        }
    }

    return foundEntities
}

    // Micro-XPath
    /**
     * Given a path, joins all the entities that the auxiliary function "aux_get_entity_with_x_path"
     * relates to the [x_path].
     *
     * @param[x_path] Micro-XPath with the possible path of entities.
     * @return String with the final path of the found entities.
     */
    fun get_entity_xml_with_x_path(x_path: String): String {
        val stringBuilder = StringBuilder()
        val returned_entities = aux_get_entity_with_x_path(x_path, entities)

        if (returned_entities.isNotEmpty()) {
            returned_entities.forEachIndexed { index, entity ->
                stringBuilder.append(get_entity_xml(entity))
                if (index != returned_entities.size - 1)
                    stringBuilder.append("\n")
            }
        }
        return stringBuilder.toString()
    }

    fun get_entity_with_x_path(x_path: String): List<Entity> {
        val returned_entities = aux_get_entity_with_x_path(x_path, entities)
        return returned_entities
    }
}