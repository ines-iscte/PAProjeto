/**
 * @author Ana Mercês Soares dos Reis Moreira - nº99352
 * @author Inês Colaço Ascenso - nº99286
 */

import java.io.File

/**
 * Class that represents a document with its child, the list of its entities and an encoding.
 * @property[child] The entity that belongs to the document.
 * @property[entities] List of entities contained in the document.
 * @property[encoding] The String that goes on the top of the XML File - document.
 *
 */
class Document(
    private var child: Entity,
    private val entities: MutableList <Entity> = mutableListOf(),
    private val encoding: String
) {

    init{
        entities.addAll(child.getChildren())
    }

    /**
     * Accepts a visitor function to process the entity and its children.
     *
     * Applies the provided [visitor] function to the entity and recursively to all of its children.
     * The visitor function should return True to continue visiting other entities, or False to stop the process.
     * @param visitor The visitor function to be applied to the entity and its children.
     */
    fun accept(visitor: (Entity) -> Boolean) {
        entities.forEach {
            it.accept(visitor)
        }
    }

    /**
     * Getter of the child (entity) of the document.
     * @return Entity which belong to the document.
     */
    fun getChild(): Entity {
        return child
    }

    /**
     * Getter of the list of entities of the document.
     * @return List of entities that belong to the document.
     */
    fun getEntities(): List<Entity> {
        return entities
    }

    /**
     * Verifies if the [entity] already has the [attribute].
     * There can't be two equal attributes with the same name and value.
     * [entity] The entity to verify if it has already the attribute given.
     * [attribute] The object of comparison.
     */
    internal fun attributeExists(entity: Entity, attribute: Attribute): Attribute? {
        return entity.getAttributes().find { it.getAttributeName() == attribute.getAttributeName() && it.getAttributeValue() == attribute.getAttributeValue() }
    }

    /**
     * Verifies if an entity exists in the document.
     * There can only be one entity with its specific parameters in the document.
     * [name] The name of the entity to check.
     * [text] The text of the entity to check.
     * [atributes] The list of attribute which belong to the entity.
     * [parent] The entity's parent.
     * [children] The list of entities, whose parent is the entity to check.
     */
    internal fun entityExists(name: String, text: String, attributes: MutableList<Attribute>, parent: Entity?, children: List<Entity>): Entity? {
        return getEntities().find { it.getName() == name && it.getEntityText() == text && it.getAttributes() == attributes
                && it.getParent() == parent && it.getChildren() == children}
    }

    // Point 1.
    /**
     * Adds an entity to the document.
     * Adds to the list of entities the [entity].
     * @param[entity] The entity to be added to the document.
     */
    fun addEntity(entity: Entity) {
        if (entityExists(entity.getName(), entity.getEntityText(), entity.getAttributes(), entity.getParent(), entity.getChildren()) != null) {
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
    fun removeEntity(entity: Entity) {
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

    fun prettyPrint(): String {
        return getEntityXml(entity = child, pretty_print = true, encoding)
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

    fun prettyPrintToFile(outputFile: File) {
        val xmlString = prettyPrint()
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
    fun addGlobalAttribute(entity_name: String, attribute_name: String, attribute_value: String) {
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

        this.entities.filter { it.getName() == entity_name }
            .forEach {
                if (attributeExists(it, att) != null) {
                    throw IllegalStateException("Entity already has this attribute.")
                } else {
                    it.addAttribute(att)
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
    fun renameGlobalEntity(old_name: String, new_name:String){
        require(new_name.split(" ").size == 1) {
                "New name must contain only one word"
        }

        this.entities.filter { old_name == it.getName() }
            .forEach { entity ->
                if (entityExists(new_name, entity.getEntityText(), entity.getAttributes(), entity.getParent(), entity.getChildren()) != null) {
                    throw IllegalStateException("Document already has this entity.")
                } else {
                    entity.setName(new_name)
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
    fun renameGlobalAttributes(entity_name: String, old_attribute_name: String, new_attribute_name: String){
        require(new_attribute_name.split(" ").size == 1) {
                "New name must contain only one word"
        }
        entities.forEach{ entity ->
            if (entity_name == entity.getName()) {
                entity.getAttributes().forEach{
                    if (it.getAttributeName() == old_attribute_name)
                        entity.changeAttribute(attribute = it, new_name = new_attribute_name)
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
    fun removeGlobalEntities(entity_name: String){
        val list: MutableList <Entity> = mutableListOf()

        this.entities.filter { it.getName() == entity_name }
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
    fun removeGlobalAttributes(entity_name: String, attribute_name: String) {
        entities.forEach { entity ->
            if (entity_name == entity.getName()) {
                val attributes_to_remove = entity.getAttributes().filter { it.getAttributeName() == attribute_name }
                entity.getAttributes().removeAll(attributes_to_remove)
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
    fun getEntityXml(entity: Entity, pretty_print: Boolean = false, encoding: String? = null, indent: String = ""): String{
        val stringBuilder = StringBuilder()

        if(encoding != null) {
            stringBuilder.append("<${encoding}>")
            stringBuilder.appendLine()
        }

        stringBuilder.append("$indent<${entity.getName()}")
        if (entity.getAttributes().isNotEmpty()) {
            entity.getAttributes().forEach { attribute ->
                stringBuilder.append(" ${attribute.getAttributeName()}=\"${attribute.getAttributeValue()}\"")
            }
        }

        if (entity.getChildren().isEmpty() && entity.getEntityText().isEmpty()) {
            stringBuilder.append("/>")
        } else {
            stringBuilder.append(">")

            if (entity.getEntityText().isNotEmpty())
                stringBuilder.append(entity.getEntityText())

            // In case pretty_print is True, it will get the children structure too
            if (pretty_print){
                if (entity.getChildren().isNotEmpty()) {
                    stringBuilder.appendLine()
                    entity.getChildren().forEach { child ->
                        stringBuilder.append(getEntityXml(entity=child, pretty_print=true ,indent="$indent    "))
                        stringBuilder.appendLine()
                    }
                    stringBuilder.append(indent)
                }
            }
            if (entity.getChildren().isNotEmpty() || entity.getEntityText().isNotEmpty()) {
                stringBuilder.append("</${entity.getName()}>")
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
    private fun auxGetEntityWithXPath(x_path: String, entities_to_explore: List<Entity>): List<Entity> {
    val foundEntities = mutableListOf<Entity>()

    val parts = x_path.split("/")
    val first_entity = parts.first()
    val other_entities = parts.drop(1).joinToString("/")

    val matchingEntities = entities_to_explore.filter { it.getName() == first_entity }

    matchingEntities.forEach { entity ->
        if (other_entities.isEmpty()) {
            foundEntities.add(entity)
        } else {
            val childEntities = auxGetEntityWithXPath(other_entities, entity.getChildren())
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
    fun getEntityXmlWithXPath(x_path: String): String {
        val stringBuilder = StringBuilder()
        val returned_entities = auxGetEntityWithXPath(x_path, entities)

        if (returned_entities.isNotEmpty()) {
            returned_entities.forEachIndexed { index, entity ->
                stringBuilder.append(getEntityXml(entity))
                if (index != returned_entities.size - 1)
                    stringBuilder.append("\n")
            }
        }
        return stringBuilder.toString()
    }

    fun getEntityWithXPath(x_path: String): List<Entity> {
        val returned_entities = auxGetEntityWithXPath(x_path, entities)
        return returned_entities
    }
}