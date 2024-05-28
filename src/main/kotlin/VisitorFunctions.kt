/**
 * @author Ana Mercês Soares dos Reis Moreira - nº99352
 * @author Inês Colaço Ascenso - nº99286
 */

/**
 * NOTE: The following functions are another version of the exercises using visitors.
 * These functions are related to the class Document.
 */
// Functions using Visitors (Point 5.)

// Relating Entities, using Documents
// The functions must be related to a Document, so that the entities available in it are searched, when using the Visitor method.

// Point 3.
/**
 * Searches for the parent of an entity.
 *
 * Looks for the parent of the given [child_entity] from the root of the document's hierarchy of entities.
 *
 * @param[child_entity] The child entity for which to find the parent.
 * @return The parent entity of the [child_entity]. If not found, returns null.
 */
fun Document.getParentVis(child_entity: Entity): Entity?{
    var result: Entity? = null

    this.accept { entity ->
        if (entity == child_entity.getParent()) {
            result = entity
        }
        true
    }
    return result
}

// Point 3.
/**
 * Searches for the children of an entity.
 *
 * Starts looking for the children at the beginning of the document's structure and saves all the children
 * whose parent is the [parent_entity].
 *
 * @param[parent_entity] The parent entity for which to find its children.
 * @return The list of children of the [parent_entity].
 */
fun Document.getChildrenVis(parent_entity: Entity) : List<Entity> {
    val list = mutableListOf<Entity>()

    this.accept { entity ->
        if (entity.getParent() == parent_entity){
            list.add(entity)
        }
        true
    }
    return list
}

// Point 7.
/**
 * Looks for the parent and children of an entity.
 *
 * Starts at the root of the document and seeks the parent of the [main_entity] and the entity's children.
 * As it goes on the hierarchy, it saves the entities in a list.
 *
 * @param[main_entity] The entity for which to find its parent and children.
 * @return List with the parent and children of the entity given.
 */
fun Document.getParentAndChildrenVis(main_entity: Entity): List<Entity>{
    val list = mutableListOf<Entity>()

    this.accept { entity ->
        if (entity in main_entity.getChildren() || entity == main_entity.getParent()) {
            list.add(entity)
        }
        true
    }
    return list
}

// Relating Documents

// Point 6.
/**
 * Adds a global attribute to a document.
 *
 * Creates an attribute with the [attribute_name] and the [attribute_value].
 * Looks for the entities which name is the same as the [entity_name].
 * As it goes on the hierarchy, it adds the recently created attribute as a global attribute to the entities
 * which verify the previous condition.
 *
 * @param[entity_name] Name of the entities to which the attribute will be added.
 * @param[attribute_name] Name of the attribute to be added.
 * @param[attribute_value] Value of the attribute ti be added.
 */
fun Document.addGlobalAttributeVis(entity_name: String, attribute_name: String, attribute_value: String) {
    val att = Attribute(attribute_name, attribute_value)

    this.accept {
        if (entity_name == it.getName()) {
            if (attributeExists(it, att) != null) {
                throw IllegalStateException("Entity already has this attribute.")
            } else {
                it.addAttribute(att)
            }
        }
        true
    }
}

// Point 7.
/**
 * Renames entities to the document globally.
 *
 * It goes through the hierarchy of the document and looks for the entities which name is [old_name].
 * Renames them with [new_name].
 *
 * @param[old_name] Current name of the entities - to be renamed.
 * @param[new_name] Future name of the entities - to replace the current.
 */
fun Document.renameGlobalEntityVis(old_name: String, new_name:String){
    require(new_name.split(" ").size == 1) {
        "New name must contain only one word"
    }
    this.accept { entity ->
        if (old_name == entity.getName()) {
            if (entityExists(new_name, entity.getEntityText(), entity.getAttributes(), entity.getParent(), entity.getChildren()) != null) {
                throw IllegalStateException("Document already has this entity.")
            } else {
                entity.setName(new_name)
            }
        }
        true
    }
}

// Point 8.
/**
 * Renames global attributes of the document.
 *
 * Starting at the root, goes through the hierarchy of entities of the document and renames the global attributes
 * of the entities which name is [entity_name].
 * Changes the name of the global attributes which current name is [old_attribute_name] to [new_attribute_name].
 *
 * @param[entity_name] Name of the entities where the attributes are.
 * @param[old_attribute_name] Current name of the attributes - to be renamed.
 * @param[new_attribute_name] Future name of the attributes - to replace the current.
 */
fun Document.renameGlobalAttributesVis(entity_name: String, old_attribute_name: String, new_attribute_name: String){
    require(new_attribute_name.split(" ").size == 1) {
        "New name must contain only one word"
    }

    this.accept { entity ->
        if (entity_name == entity.getName()) {
            entity.getAttributes().forEach{
                if (it.getAttributeName() == old_attribute_name)
                    entity.changeAttribute(attribute = it, new_name = new_attribute_name)
            }
        }
        true
    }
}

// Point 9.
/**
 * Removes the entity from the document globally.
 *
 * Goes through the hierarchy of the document, starting at the root, and looks for the entities which name is
 * [entity_name]. Removes from the document's entities list those which fulfill the previous condition.
 *
 * @param[entity_name] Name of the entities to be removed.
 */
fun Document.removeGlobalEntitiesVis(entity_name: String) {
    val list = mutableListOf<Entity>()

    this.accept { entity ->
        if (entity.getName() == entity_name) {
            list.add(entity)
        }
        true
    }
    list.forEach { removeEntity(it) }
}

// Point 10.
/**
 * Removes attributes of the document globally.
 *
 * Starts at the beginning of the hierarchy of the document and looks for the entities which name is [entity_name].
 * Filters the attributes which name is [attribute_name] and when found all the entities and respective attributes,
 * removes all those attributes globally from the document.
 *
 * @param[entity_name] Name of the entities which contain the attributes.
 * @param[attribute_name] Name of the attributes to be removed.
 */
fun Document.removeGlobalAttributesVis(entity_name: String, attribute_name: String) {
    this.accept { entity ->
        if (entity_name == entity.getName()) {
            val attributesToRemove = entity.getAttributes().filter { it.getAttributeName() == attribute_name }
            attributesToRemove.forEach { attribute ->
                entity.getAttributes().remove(attribute)
            }
        }
        true
    }
}