/**
 * @author Ana Mercês Soares dos Reis Moreira - nº99352
 * @author Inês Colaço Ascenso - nº99286
 */

import kotlin.reflect.*
import kotlin.reflect.full.*

/**
 * Annotation that specifies a chosen name for the object, class or property.
 * @param[objectName] The costume name to be assigned to the annotated element.
 */
@Target(AnnotationTarget.CLASS,AnnotationTarget.PROPERTY)
annotation class ObjectName(val objectName: String)

/**
 * Annotation to exclude a class or property from a certain processing.
 */
@Target(AnnotationTarget.CLASS,AnnotationTarget.PROPERTY)
annotation class Exclude

/**
 * Annotation to mark a class or property as an Entity.
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
annotation class IsEntity

/**
 * Annotation to mark a class or property as an Attribute.
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
annotation class IsAttribute

/**
 * Annotation to indicate that a property is a list.
 */
@Target(AnnotationTarget.PROPERTY)
annotation class IsList

/**
 * Annotation to specify a transformer for XML string serialization; only applied to properties.
 * @param[transformer] The KClass of the transformer to be used for the annotated property.
 */
@Target(AnnotationTarget.PROPERTY)
annotation class XmlString(val transformer: KClass<out StringTransformer>)

/**
 * Annotation to specify an adapter for XML serialization; only applied to classes.
 * @param[adapter] The class of the adapter to be used.
 */
@Target(AnnotationTarget.CLASS)
annotation class XmlAdapter(val adapter: KClass<out EntityAdapter>)

@IsEntity
@XmlAdapter(ChangeNameAdapter::class)
@ObjectName("componente")
data class ComponenteAvaliacao(

    @IsAttribute
    @ObjectName("nome")
    val nome: String,

    @XmlString(AddPercentage::class)
    @IsAttribute
    @ObjectName("peso")
    val peso: Int,
)

@XmlAdapter(FUCAdapter::class)
@IsEntity
@ObjectName("fuc")
data class FUC(

    @IsAttribute
    @ObjectName("codigo")
    val codigo: String,

    @IsEntity
    @XmlString(AddPoint::class)
    @ObjectName("nome")
    val nome: String,

    @IsEntity
    @ObjectName("ects")
    val ects: Double,

    @Exclude
    val observacoes: String,

    @IsEntity
    @IsList
    @ObjectName("avaliacao")
    val avaliacao: List<ComponenteAvaliacao>
)

/**
 * Interface for transforming strings.
 */
interface StringTransformer {
    fun transform(value: Any): Any
}

/**
 * Transformer to add a percentage sign(%) to a value.
 */
class AddPercentage : StringTransformer {
    override fun transform(value: Any): Any {
        return "$value%"
    }
}

/**
 * Transformer to add a point(.) to a value.
 */
class AddPoint : StringTransformer {
    override fun transform(value: Any): Any {
        return "$value."
    }
}

/**
 * Interface for adapting entities.
 */
interface EntityAdapter {
    fun adapt(entity: Entity)
}

/**
 * Adapter to change the name of an entity.
 */
class ChangeNameAdapter : EntityAdapter {
    override fun adapt(entity: Entity) {
        entity.setName("comp")
    }
}

/**
 * Adapter for the entity FUC.
 */
class FUCAdapter : EntityAdapter {
    override fun adapt(entity: Entity) {
        entity.addAttribute(Attribute("Modo", "Presencial"))
    }
}

/**
 * Translates a given object to an XML document representation.
 * Applies the annotations if the conditions are met.
 *
 * @param[obj] The object to be translated.
 * @param[parentEntity] The entity's parent, if there is. By default, is null.
 * @param[encoding] The encoding for the XML document. By default, is an empty string.
 * @return The XML document representing the object.
 */

fun translate(obj: Any, parentEntity: Entity? = null, encoding: String = ""): Document {
    val clazz: KClass<*> = obj::class
    var clazzName = clazz.simpleName

    if (clazz.hasAnnotation<ObjectName>())
        clazzName = clazz.findAnnotation<ObjectName>()?.objectName

    val auxEntity = Entity(name = clazzName.toString(), parent = parentEntity)

    if (clazz.declaredMemberProperties.isNotEmpty()) {
        clazz.declaredMemberProperties.forEach { property ->
            var propertyName = property.name
            if (property.hasAnnotation<ObjectName>())
                propertyName = property.findAnnotation<ObjectName>()?.objectName.toString()

            if (!property.hasAnnotation<Exclude>()) {
                var propertyValue = property.call(obj)
                if (property.hasAnnotation<XmlString>()) {
                    propertyValue = transformString(property, propertyValue.toString())
                }
                if (property.hasAnnotation<IsAttribute>()) {
                    auxEntity.addAttribute(Attribute(name = propertyName, value = propertyValue.toString()))

                } else if (property.hasAnnotation<IsList>()) {
                    val list = property.call(obj) as? List<*>
                    list?.let {
                        val auxEntity2 = Entity(name = property.name, parent = auxEntity)
                        it.forEach { element ->
                            translate(obj = element!!, parentEntity = auxEntity2)
                        }
                    }

                } else {
                    if (propertyValue != null || !property.returnType.isMarkedNullable) {
                        val textValue = propertyValue?.toString() ?: ""
                        Entity(name = propertyName, text = textValue, parent = auxEntity)
                    }
                }
            }
        }
    }

    if (clazz.hasAnnotation<XmlAdapter>())
        transformEntity(auxEntity, obj::class)

    return Document(child = auxEntity, encoding = encoding)
}

/**
 * Transforms a string using the specified transformer for the property.
 *
 * @param[property] The property whose value will be transformed.
 * @param[value] The string value to be transformed.
 * @return The transformed string.
 */
fun transformString(property: KProperty<*>, value: String): String {
    val adapterClass = property.findAnnotation<XmlString>()!!.transformer
    val adapterInstance = adapterClass.java.getDeclaredConstructor().newInstance()
    val newValue = adapterClass.memberFunctions.filter { it.name == "transform" && it.parameters.size == 2 }[0].call(adapterInstance, value)

    return newValue.toString()
}

/**
 * Transforms an entity using the specified adapter for the class.
 *
 * @param[entity] The entity to be transformed.
 * @param[clazz] The class whose adapter will be used for the transformation.
 */
fun transformEntity(entity: Entity, clazz: KClass<*>) {
        val adapterClass = clazz.findAnnotation<XmlAdapter>()!!.adapter
        val adapterInstance = adapterClass.java.getDeclaredConstructor().newInstance()
        adapterClass.memberFunctions.filter { it.name == "adapt" && it.parameters.size == 2 }[0].call(adapterInstance, entity)
}

