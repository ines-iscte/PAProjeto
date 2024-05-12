import kotlin.reflect.*
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor

@Target(AnnotationTarget.CLASS,AnnotationTarget.PROPERTY)
annotation class DbName(val dbName: String)

@Target(AnnotationTarget.CLASS,AnnotationTarget.PROPERTY)
annotation class Exclude

@Target(AnnotationTarget.PROPERTY)
annotation class Header

@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
annotation class IsEntity

@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
annotation class IsAttribute

@Target(AnnotationTarget.PROPERTY)
annotation class IsList

@Target(AnnotationTarget.PROPERTY)
annotation class XmlString(val transformer: KClass<out StringTransformer>)

//@Target(AnnotationTarget.CLASS)
//annotation class XmlAdapter(val adapt: KClass<out EntityAdapter<*>>)

//@XmlAdapter(FUCAdapter::class)
@IsEntity
@DbName("fuc")
data class FUC(

    @Header
    @IsAttribute
    @DbName("codigo")
    val codigo: String,

    @IsEntity
    @DbName("nome")
    @XmlString(AddPoint::class)
    val nome: String,

    @IsEntity
    @DbName("ects")
    val ects: Double,

    @Exclude
    val observacoes: String,

    @IsEntity
    @IsList
    @DbName("avaliacao")
    val avaliacao: List<ComponenteAvaliacao>
)

@IsEntity
@DbName("componente")
data class ComponenteAvaliacao(

    @IsAttribute
    @DbName("nome")
    val nome: String,

    @XmlString(AddPercentage::class)
    @IsAttribute
    @DbName("peso")
    val peso: Int,
)

interface StringTransformer {
    fun transform(value: String): String
}

class AddPercentage : StringTransformer {
    override fun transform(value: String): String {
        return "$value%"
    }
}

class AddPoint : StringTransformer {
    override fun transform(value: String): String {
        return "$value."
    }
}

//interface EntityAdapter<T> {
//    fun adapt(entity: T): T
//}
//
//class FUCAdapter: EntityAdapter<FUC> {
//    override fun adapt(entity: FUC): FUC {
//        // Reordenar os atributos conforme desejado
//        val reorderedAttributes = LinkedHashMap<String, Any?>()
//
//        // Define a ordem desejada dos atributos
//        val attributeOrder = listOf("codigo", "nome", "ects", "observacoes", "avaliacao")
//
//        // Adiciona os atributos à entidade reordenada na ordem desejada
//        attributeOrder.forEach { attributeName ->
//            val property = entity::class.members.find { it.name == attributeName }
//            property?.let {
//                val value = property.call(entity)
//                reorderedAttributes[attributeName] = value
//            }
//        }
//
//        // Cria uma nova instância de FUC com os atributos reordenados
//        return FUC(
//            codigo = reorderedAttributes["codigo"] as String,
//            nome = reorderedAttributes["nome"] as String,
//            ects = reorderedAttributes["ects"] as Double,
//            observacoes = reorderedAttributes["observacoes"] as String,
//            avaliacao = reorderedAttributes["avaliacao"] as List<ComponenteAvaliacao>
//        )
//    }
//}


//class FUCAdapter1: EntityAdapter<Entity> {
//    override fun adapt(entity: Entity): Entity {
//        // Reordenar os atributos conforme desejado
//        val reorderedAttributes = LinkedHashMap<String, Any?>()
//
//        // Define a ordem desejada dos atributos
//        val attributeOrder = listOf("codigo", "nome", "ects", "observacoes", "avaliacao")
//
//        // Adiciona os atributos à entidade reordenada na ordem desejada
//        attributeOrder.forEach { attributeName ->
//            val property = entity::class.members.find { it.name == attributeName }
//            property?.let {
//                val value = property.call(entity)
//                reorderedAttributes[attributeName] = value
//            }
//        }
//
//        // Cria uma nova instância de FUC com os atributos reordenados
//        val fucEntity = Entity(name = "FUC")
//        reorderedAttributes.forEach { (attributeName, attributeValue) ->
//            fucEntity.add_attribute(Attribute(name = attributeName, value = attributeValue?.toString() ?: ""))
//        }
//        return fucEntity
//    }
//}

fun translate(obj: Any, parentEntity: Entity? = null): Document {
    val clazz: KClass<*> = obj::class
    var clazzName = clazz.simpleName

    if (clazz.hasAnnotation<DbName>())
        clazzName = clazz.findAnnotation<DbName>()?.dbName

    val auxEntity = Entity(name = clazzName.toString(), parent = parentEntity)
    val doc = Document(child = auxEntity, encoding = "utf-8")

    if (clazz.declaredMemberProperties.isNotEmpty()) {
        clazz.declaredMemberProperties.forEach { property ->
            var propertyName = property.name
            if (property.hasAnnotation<DbName>())
                propertyName = property.findAnnotation<DbName>()?.dbName.toString()

            if (!property.hasAnnotation<Exclude>()) {
                var propertyValue = property.call(obj)
                if (property.hasAnnotation<XmlString>()) {
                    propertyValue = transformValue(property, propertyValue)
                }
                if (property.hasAnnotation<IsAttribute>()) {
                    auxEntity.add_attribute(Attribute(name = propertyName.toString(), value = propertyValue.toString()))

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
//    val adapterClass = (obj::class.annotations.find { it is XmlAdapter } as? XmlAdapter)?.adapt
//    val adaptedEntity = adapterClass?.let { adapter ->
//        val adaptMethod = adapter.primaryConstructor?.call()
//        if (adaptMethod is EntityAdapter<*>) {
//            adaptMethod.adapt(obj)
//        } else {
//            obj
//        }
//    } ?: obj


    return doc
}

fun transformValue(property: KProperty<*>, value: Any?): String {
    return value?.let {
        (property.findAnnotation<XmlString>()?.transformer?.primaryConstructor?.call())?.transform(it.toString()) ?: it.toString()
    } ?: ""
}

//fun adaptEntityWithAdapter(clazz: KClass<*>, obj: Any): Any {
//    val adapterAnnotation = clazz.annotations.find { it is XmlAdapter } as? XmlAdapter
//    return adapterAnnotation?.let { adapter ->
//        val adapterClass = adapter.adapt
//        val adapterInstance = adapterClass.primaryConstructor?.call()
//        if (adapterInstance is EntityAdapter<*>) {
//            adapterInstance.adapt(obj)
//        } else {
//            obj
//        }
//    } ?: obj
//}
//




