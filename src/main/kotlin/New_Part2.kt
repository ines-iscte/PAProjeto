import kotlin.reflect.*
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

@Target(AnnotationTarget.CLASS,AnnotationTarget.PROPERTY)
annotation class DbName(val dbName: String)

@Target(AnnotationTarget.CLASS,AnnotationTarget.PROPERTY)
annotation class Exclude

@Target(AnnotationTarget.CLASS,AnnotationTarget.PROPERTY)
annotation class Header

@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
annotation class IsEntity

@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
annotation class IsAttribute

@Target(AnnotationTarget.PROPERTY)
annotation class IsList

@IsEntity
@DbName("fuc")
data class FUC(

    @Header
    @IsAttribute
    @DbName("codigo")
    val codigo: String,

    @IsEntity
    @DbName("nome")
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

    @IsAttribute
    @DbName("peso")
    val peso: Int,
)

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
                if (property.hasAnnotation<IsAttribute>()) {
                    val propertyValue = property.call(obj)
                    auxEntity.add_attribute(
                        Attribute(name = propertyName, value = propertyValue.toString()
                        )
                    )

                } else if (property.hasAnnotation<IsList>()) {
                    val list = property.call(obj) as? List<*>
                    list?.let {
                        val auxEntity2 = Entity(name = property.name, parent = auxEntity)
                        it.forEach { element ->
                            translate(obj = element!!, parentEntity = auxEntity2)
                        }
                    }

                } else {
                    val propertyValue = property.call(obj)
                    if (propertyValue != null || !property.returnType.isMarkedNullable) {
                        val textValue = propertyValue?.toString() ?: ""
                        Entity(name = propertyName, text = textValue, parent = auxEntity)
                    }
                }
            }
        }
    }
    return doc
}



//<componente nome="Quizzes" peso="20"/>




//
//    if (entity.get_children().isEmpty() && entity.get_entity_text().isEmpty()) {
//        stringBuilder.append("/>")
//    } else {
//        stringBuilder.append(">")
//
//        if (entity.get_entity_text().isNotEmpty())
//            stringBuilder.append(entity.get_entity_text())
//
//        // In case pretty_print is True, it will get the children structure too
//        if (pretty_print){
//            if (entity.get_children().isNotEmpty()) {
//                stringBuilder.appendLine()
//                entity.get_children().forEach { child ->
//                    stringBuilder.append(get_entity_xml(entity=child, pretty_print=true ,indent="$indent    "))
//                    stringBuilder.appendLine()
//                }
//                stringBuilder.append(indent)
//            }
//        }
//        if (entity.get_children().isNotEmpty() || entity.get_entity_text().isNotEmpty()) {
//            stringBuilder.append("</${entity.get_name()}>")
//        }
//    }
//    return stringBuilder.toString()




//fun get_entity_xml(entity: Entity, pretty_print: Boolean = false, encoding: String? = null, indent: String = ""): String{
//    val stringBuilder = StringBuilder()
//
//    stringBuilder.append("$indent<${entity.get_name()}")
//    if (entity.get_attributes().isNotEmpty()) {
//        entity.get_attributes().forEach { attribute ->
//            stringBuilder.append(" ${attribute.get_attribute_name()}=\"${attribute.get_attribute_value()}\"")
//        }
//    }
//
//    if (entity.get_children().isEmpty() && entity.get_entity_text().isEmpty()) {
//        stringBuilder.append("/>")
//    } else {
//        stringBuilder.append(">")
//
//        if (entity.get_entity_text().isNotEmpty())
//            stringBuilder.append(entity.get_entity_text())
//
//        // In case pretty_print is True, it will get the children structure too
//        if (pretty_print){
//            if (entity.get_children().isNotEmpty()) {
//                stringBuilder.appendLine()
//                entity.get_children().forEach { child ->
//                    stringBuilder.append(get_entity_xml(entity=child, pretty_print=true ,indent="$indent    "))
//                    stringBuilder.appendLine()
//                }
//                stringBuilder.append(indent)
//            }
//        }
//        if (entity.get_children().isNotEmpty() || entity.get_entity_text().isNotEmpty()) {
//            stringBuilder.append("</${entity.get_name()}>")
//        }
//    }
//    return stringBuilder.toString()



