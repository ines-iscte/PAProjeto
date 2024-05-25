import kotlin.reflect.*
import kotlin.reflect.full.*

@Target(AnnotationTarget.CLASS,AnnotationTarget.PROPERTY)
annotation class ObjectName(val objectName: String)

@Target(AnnotationTarget.CLASS,AnnotationTarget.PROPERTY)
annotation class Exclude

@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
annotation class IsEntity

@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
annotation class IsAttribute

@Target(AnnotationTarget.PROPERTY)
annotation class IsList

@Target(AnnotationTarget.PROPERTY)
annotation class XmlString(val transformer: KClass<out StringTransformer>)

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

interface StringTransformer {
    fun transform(value: Any): Any
}

class AddPercentage : StringTransformer {
    override fun transform(value: Any): Any {
        return "$value%"
    }
}

class AddPoint : StringTransformer {
    override fun transform(value: Any): Any {
        return "$value."
    }
}

interface EntityAdapter {
    fun adapt(entity: Entity)
}

class ChangeNameAdapter : EntityAdapter {
    override fun adapt(entity: Entity) {
        entity.setName("comp")
    }
}

class FUCAdapter : EntityAdapter {
    override fun adapt(entity: Entity) {
        entity.addAttribute(Attribute("Modo", "Presencial"))
    }
}

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

fun transformString(property: KProperty<*>, value: String): String {
    val adapterClass = property.findAnnotation<XmlString>()!!.transformer
    val adapterInstance = adapterClass.java.getDeclaredConstructor().newInstance()
    val newValue = adapterClass.memberFunctions.filter { it.name == "transform" && it.parameters.size == 2 }[0].call(adapterInstance, value)

    return newValue.toString()
}

fun transformEntity(entity: Entity, clazz: KClass<*>) {
        val adapterClass = clazz.findAnnotation<XmlAdapter>()!!.adapter
        val adapterInstance = adapterClass.java.getDeclaredConstructor().newInstance()
        adapterClass.memberFunctions.filter { it.name == "adapt" && it.parameters.size == 2 }[0].call(adapterInstance, entity)
}

