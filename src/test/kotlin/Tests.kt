import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Tests {

    val peso = Entity(name="peso")
    val modelo = Entity(name="modelo", text="Automático")
    val plastico = Attribute("Material", "Plástico")
    val papel = Attribute("Material", "Papel")
    val garrafa =  Entity(name="garrafa", attributes= mutableListOf(plastico))
    val doc = Document(name="doc")

    // Testes a Atributos
    @Test
    fun test_create_attribute(){
        assertEquals("Material", plastico.name)
        assertEquals("Papel", papel.value)
    }

    // Ponto 2.
    @Test
    fun test_add_attribute_to_entity(){
        val altura = Attribute("Altura", "1.60 metros")
        modelo.addAttribute(altura)
        val largura = Attribute("Largura", "1.20 metros")
        modelo.addAttribute(largura)
        assertEquals(listOf(altura, largura), modelo.attributes)
    }

    // Ponto 2.
    @Test
    fun test_remove_attribute_to_entity(){
        val altura = Attribute("Altura", "1.60 metros")
        modelo.addAttribute(altura)
        val largura = Attribute("Largura", "1.20 metros")
        modelo.addAttribute(largura)
        modelo.removeAttribute(altura)
        assertEquals(listOf(largura), modelo.attributes)
    }

    // Ponto 2.
    @Test
    fun test_change_attribute_to_entity(){
        val altura = Attribute("Altura", "1.60 metros")
        modelo.addAttribute(altura)
        val largura = Attribute("Largura", "1.20 metros")
        modelo.addAttribute(largura)
        modelo.changeAttribute(attribute=altura,new_value="1.50 metros")
        modelo.changeAttribute(attribute=largura, new_name="Comprimento", new_value="1.90 metros")

        val expectedAttributes = listOf(
            Attribute("Altura", "1.50 metros"),
            Attribute("Comprimento", "1.90 metros")
        )

        assertEquals(expectedAttributes, modelo.attributes)
    }

    // Testes a Entidades
    @Test
    fun test_create_entity() {
        assertEquals("modelo", modelo.name)
        assertEquals("peso", peso.name)
        assertEquals("Automático", modelo.text)
        assertEquals(mutableListOf(plastico), garrafa.attributes)
    }

    // Ponto 3.
    @Test
    fun test_children_list_of_entity(){
        val objeto = Entity(name="objeto")
        val copo =  Entity(name="copo", text="De vinho", attributes= mutableListOf(plastico, papel), parent=objeto)
        val copito = Entity(name="copito", text="De vinho branco", parent=copo)
        assertEquals(listOf(copo, copito), objeto.get_Children())
    }

    // Ponto 3
    @Test
    fun test_parent_of_entity(){
        val objeto = Entity(name="objeto")
        val copo =  Entity(name="copo", text="De vinho", attributes= mutableListOf(plastico, papel), parent=objeto)
        val copito = Entity(name="copito", text="De vinho branco", parent=copo)
        assertEquals(copo, copito.get_Parent())
    }

    // Ponto 3.
    @Test
    fun test_parent_and_children(){
        val objeto = Entity(name="objeto")
        val copo =  Entity(name="copo", text="De vinho", attributes= mutableListOf(plastico, papel), parent=objeto)
        val copito = Entity(name="copito", text="De vinho branco", parent=copo)
        assertEquals(listOf(objeto, copito), copo.get_Parent_and_Children())
    }

    // Testes a Documentos
    @Test
    fun test_create_document(){
        assertEquals("doc", doc.name)
    }

    // Ponto 1.
    @Test
    fun test_add_entity_to_document(){
        doc.addEntity(peso)
        doc.addEntity(modelo)
        doc.addEntity(garrafa)
        assertEquals(listOf(peso, modelo, garrafa), doc.entities)
    }

    // Ponto 1.
    @Test
    fun test_remove_entity_to_document(){
        doc.addEntity(peso)
        doc.addEntity(modelo)
        doc.addEntity(garrafa)
        doc.removeEntity(modelo)
        assertEquals(listOf(peso, garrafa), doc.entities)
    }
}