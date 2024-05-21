import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

class Tests {

    // Entity without children or attributes
    val modelo = Entity(name ="modelo")

    // Test Document
    val doc = Document(child = modelo,  encoding="?xml version=\"1.0\" encoding=\"UTF-8\"?")

    // Entities with attributes
    val plastico = Attribute("Material", "Plastico")
    val papel = Attribute("Material", "Papel")
    val garrafa =  Entity(name ="garrafa", attributes = mutableListOf(plastico))
    val garrafa2 =  Entity(name ="garrafa", attributes = mutableListOf(papel))

    // Entities with children
    val objeto = Entity(name ="objeto")
    val copo =  Entity(name ="copo", attributes = mutableListOf(plastico, papel), parent =objeto)
    val copito = Entity(name ="copito", parent =copo)

    init{
        modelo.change_entity_text("Automático")
    }

    // Document relating to the example given
    val plano = Entity(name ="plano")

    val curso1 = Entity(name ="curso", text ="Mestrado em Engenharia Informática", parent = plano)
    val codigo1 = Attribute(name ="codigo", value ="M4310")
    val fuc1 = Entity(name ="fuc", attributes = mutableListOf(codigo1), parent = plano)
    val nome1 = Entity(name ="nome", text ="Programação Avançada", parent =fuc1)
    val ects1 = Entity(name ="ects", text ="6.0", parent =fuc1)
    val avaliacao1 = Entity(name ="avaliacao", parent =fuc1)
    val componente_nome1 = Attribute(name ="nome", value ="Quizzes")
    val componente_nome2 = Attribute(name ="peso", value ="20%")
    val componente1 = Entity(name ="componente", attributes = mutableListOf(componente_nome1, componente_nome2), parent =avaliacao1)
    val componente_nome3 = Attribute(name ="nome", value ="Projeto")
    val componente_nome4 = Attribute(name ="peso", value ="80%")
    val componente2 = Entity(name ="componente", attributes = mutableListOf(componente_nome3, componente_nome4), parent =avaliacao1)

    val codigo2 = Attribute(name ="codigo", value ="03782")
    val fuc2 = Entity(name ="fuc", attributes = mutableListOf(codigo2), parent = plano)
    val nome2 = Entity(name ="nome", text ="Dissertação", parent =fuc2)
    val ects2 = Entity(name ="ects", text ="42.0", parent =fuc2)
    val avaliacao2 = Entity(name ="avaliacao", parent =fuc2)
    val componente_nome5 = Attribute(name ="nome", value ="Dissertação")
    val componente_nome6 = Attribute(name ="peso", value ="60%")
    val componente3 = Entity(name ="componente", attributes = mutableListOf(componente_nome5, componente_nome6), parent =avaliacao2)
    val componente_nome7 = Attribute(name ="nome", value ="Apresentação")
    val componente_nome8 = Attribute(name ="peso", value ="20%")
    val componente4 = Entity(name ="componente", attributes = mutableListOf(componente_nome7, componente_nome8), parent =avaliacao2)
    val componente_nome9 = Attribute(name ="nome", value ="Discussão")
    val componente_nome10 = Attribute(name ="peso", value ="20%")
    val componente5 = Entity(name ="componente", attributes = mutableListOf(componente_nome9, componente_nome10), parent =avaliacao2)

    val doc_plano = Document(child = plano,  encoding="?xml version=\"1.0\" encoding=\"UTF-8\"?")

    // Attribute tests
    @Test
    fun test_create_attribute(){
        assertEquals("Material", plastico.get_attribute_name())
        assertEquals("Papel", papel.get_attribute_value())
    }

    // Point 2.
    @Test
    fun test_add_attribute_to_entity(){
        val reciclado = Attribute("Reciclado", "1.40€")
        modelo.add_attribute(reciclado)
        val normal = Attribute("Normal", "1.20€")
        modelo.add_attribute(normal)
        assertEquals(listOf(reciclado, normal), modelo.get_attributes())
    }

    // Point 2.
    @Test
    fun test_remove_attribute_to_entity(){
        val reciclado = Attribute("Reciclado", "1.40€")
        modelo.add_attribute(reciclado)
        val normal = Attribute("Normal", "1.20€")
        modelo.add_attribute(normal)
        modelo.remove_attribute(reciclado)
        assertEquals(listOf(normal), modelo.get_attributes())
    }

    // Point 2.
    @Test
    fun test_change_attribute_to_entity(){
        val reciclado = Attribute("Reciclado", "1.40€")
        modelo.add_attribute(reciclado)
        val normal = Attribute("Normal", "1.20€")
        modelo.add_attribute(normal)
        modelo.change_attribute(attribute=reciclado, new_value="1.50€")
        modelo.change_attribute(attribute=normal, new_name="Semi-normal", new_value="1.70€")

        val expectedAttributes = listOf(
            Attribute("Reciclado", "1.50€"),
            Attribute("Semi-normal", "1.70€")
        )

        assertEquals(expectedAttributes, modelo.get_attributes())
    }


    // Entity Tests
    @Test
    fun test_create_entity() {
        assertEquals("modelo", modelo.get_name())
        assertEquals("Automático", modelo.get_entity_text())
        assertEquals(mutableListOf(plastico), garrafa.get_attributes())
    }

    // Point 3.
    @Test
    fun test_all_children_list_of_entity(){
        assertEquals(listOf(copo, copito), objeto.get_all_children())
    }

    // Point 3.
    @Test
    fun test_children_list_of_entity(){
        assertEquals(listOf(copo), objeto.get_children())
    }

    // Point 3.
    @Test
    fun test_parent_of_entity(){
        assertEquals(copo, copito.get_parent())
    }

    // Point 3.
    @Test
    fun test_parent_and_children(){
        assertEquals(listOf(objeto, copito), copo.get_parent_and_children())
    }

    // Points 3 and 5.
    @Test
    fun test_children_list_of_entity_vis(){
        doc.add_entity_to_document(objeto)
        doc.add_entity_to_document(copo)
        doc.add_entity_to_document(copito)
        assertEquals(listOf(copo), doc.get_children_vis(objeto))
    }

    // Points 3 and 5.
    @Test
    fun test_parent_of_entity_vis(){
        doc.add_entity_to_document(objeto)
        doc.add_entity_to_document(copo)
        doc.add_entity_to_document(copito)
        assertEquals(copo, doc.get_parent_vis(copito))
    }

    // Points 3 and 5.
    @Test
    fun test_parent_and_children_vis(){
        doc.add_entity_to_document(objeto)
        doc.add_entity_to_document(copo)
        doc.add_entity_to_document(copito)
        assertEquals(listOf(objeto, copito), doc.get_parent_and_children_vis(copo))
    }


    // Document tests
    @Test
    fun test_create_document(){
        assertEquals("modelo", doc.get_child().get_name())
    }

    // Point 1.
    @Test
    fun test_add_entity_to_document(){
        doc.add_entity_to_document(objeto)
        doc.add_entity_to_document(copo)
        doc.add_entity_to_document(copito)
        assertEquals(listOf(objeto, copo, copito), doc.get_entities())
    }

    // Point 1.
    @Test
    fun test_remove_entity_to_document(){
        doc.add_entity_to_document(objeto)
        doc.add_entity_to_document(copo)
        doc.add_entity_to_document(copito)
        doc.remove_entity(copito)
        assertEquals(listOf(objeto, copo), doc.get_entities())
    }

    // Point 4.
    @Test
    fun test_pretty_print(){

        val expected_output = """
        <?xml version="1.0" encoding="UTF-8"?>
        <plano>
            <curso>Mestrado em Engenharia Informática</curso>
            <fuc codigo="M4310">
                <nome>Programação Avançada</nome>
                <ects>6.0</ects>
                <avaliacao>
                    <componente nome="Quizzes" peso="20%"/>
                    <componente nome="Projeto" peso="80%"/>
                </avaliacao>
            </fuc>
            <fuc codigo="03782">
                <nome>Dissertação</nome>
                <ects>42.0</ects>
                <avaliacao>
                    <componente nome="Dissertação" peso="60%"/>
                    <componente nome="Apresentação" peso="20%"/>
                    <componente nome="Discussão" peso="20%"/>
                </avaliacao>
            </fuc>
        </plano>
    """.trimIndent()

        assertEquals(expected_output, doc_plano.pretty_print())

    }

    // Point 4.
    @Test
    fun test_pretty_print_to_file(){

        val expected_output = """
        <?xml version="1.0" encoding="UTF-8"?>
        <plano>
            <curso>Mestrado em Engenharia Informática</curso>
            <fuc codigo="M4310">
                <nome>Programação Avançada</nome>
                <ects>6.0</ects>
                <avaliacao>
                    <componente nome="Quizzes" peso="20%"/>
                    <componente nome="Projeto" peso="80%"/>
                </avaliacao>
            </fuc>
            <fuc codigo="03782">
                <nome>Dissertação</nome>
                <ects>42.0</ects>
                <avaliacao>
                    <componente nome="Dissertação" peso="60%"/>
                    <componente nome="Apresentação" peso="20%"/>
                    <componente nome="Discussão" peso="20%"/>
                </avaliacao>
            </fuc>
        </plano>
    """.trimIndent()

        val filePath = "output.xml"
        val testFile = File(filePath)

        doc_plano.pretty_print_to_file(outputFile = testFile)

        assert(testFile.exists())

        val actualXml = testFile.readText()
        assertEquals(expected_output, actualXml)

        testFile.delete()
    }

    // Point 6.
    @Test
    fun test_add_global_attribute(){
        doc.add_entity_to_document(garrafa)
        doc.add_entity_to_document(garrafa2)

        doc.add_global_attribute("garrafa", "Material", "Cartao")
        val expectedAttributes = mutableListOf(Attribute("Material", "Plastico"), Attribute("Material", "Cartao"))
        assertEquals(expectedAttributes, garrafa.get_attributes())
    }

    // Points 6 and 5.
    @Test
    fun test_add_global_attribute_vis(){
        doc.add_entity_to_document(garrafa)
        doc.add_entity_to_document(garrafa2)

        doc.add_global_attribute_vis("garrafa", "Material", "Cortiça")
        val expectedAttributes = mutableListOf(Attribute("Material", "Plastico"), Attribute("Material", "Cortiça"))
        assertEquals(expectedAttributes, garrafa.get_attributes())
    }

    // Point 7.
    @Test
    fun test_rename_global_entity(){
        doc.add_entity_to_document(objeto)
        doc.rename_global_entity("objeto", "objeta")
        assertEquals("objeta", objeto.get_name())
    }

    // Points 7 and 5.
    @Test
    fun test_rename_global_entity_vis(){
        doc.add_entity_to_document(objeto)
        doc.rename_global_entity_vis("objeto", "objetos")
        assertEquals("objetos", objeto.get_name())
    }

    // Point 8.
    @Test
    fun test_rename_global_attributes(){
        doc.add_entity_to_document(garrafa)
        doc.rename_global_attributes("garrafa", "Material", "Materia")
        assertEquals("Materia", garrafa.get_attributes().get(0).get_attribute_name())
    }

    // Points 8 and 5.
    @Test
    fun test_rename_global_attributes_vis(){
        doc.add_entity_to_document(garrafa)
        doc.rename_global_attributes_vis("garrafa", "Material", "Materia")
        assertEquals("Materia", garrafa.get_attributes().get(0).get_attribute_name())
    }

    // Point 9.
    @Test
    fun test_remove_global_entities(){
        doc.add_entity_to_document(objeto)
        doc.add_entity_to_document(copo)
        doc.add_entity_to_document(copito)
        doc.remove_global_entities("copito")
        assertEquals(listOf(objeto, copo), doc.get_entities())
    }

    // Points 9 and 5.
    @Test
    fun test_remove_global_entities_vis(){
        doc.add_entity_to_document(objeto)
        doc.add_entity_to_document(copo)
        doc.add_entity_to_document(copito)
        doc.remove_global_entities_vis("copito")
        assertEquals(listOf(objeto, copo), doc.get_entities())
    }

    // Point 10.
    @Test
    fun test_remove_global_attributes(){
        val escuro = Attribute("Cor", "Escuro")
        garrafa.add_attribute(escuro)
        garrafa2.add_attribute(escuro)
        doc.add_entity_to_document(garrafa)
        doc.add_entity_to_document(garrafa2)
        doc.remove_global_attributes("garrafa", "Material")
        assertEquals(listOf(escuro), garrafa.get_attributes())
        assertEquals(listOf(escuro), garrafa2.get_attributes())
    }

    // Points 10 and 5.
    @Test
    fun test_remove_global_attributes_vis(){
        val escuro = Attribute("Cor", "Escuro")
        garrafa.add_attribute(escuro)
        garrafa2.add_attribute(escuro)
        doc.add_entity_to_document(garrafa)
        doc.add_entity_to_document(garrafa2)
        doc.remove_global_attributes_vis("garrafa", "Material")
        assertEquals(listOf(escuro), garrafa.get_attributes())
        assertEquals(listOf(escuro), garrafa2.get_attributes())
    }

    // Micro-XPath
    @Test
    fun test_get_entity_xml_with_x_path(){
        val expected1=   """
            <componente nome="Quizzes" peso="20%"/>
            <componente nome="Projeto" peso="80%"/>
            <componente nome="Dissertação" peso="60%"/>
            <componente nome="Apresentação" peso="20%"/>
            <componente nome="Discussão" peso="20%"/>""".trimIndent()

        val expected2 = "<curso>Mestrado em Engenharia Informática</curso>"

        assertEquals(expected1, doc_plano.get_entity_xml_with_x_path("fuc/avaliacao/componente"))
        assertEquals(expected2, doc_plano.get_entity_xml_with_x_path("curso"))
    }

    @Test
    fun test_get_entity_with_x_path(){
        assertEquals(listOf(componente1, componente2, componente3, componente4, componente5), doc_plano.get_entity_with_x_path("fuc/avaliacao/componente"))
        assertEquals(listOf(curso1), doc_plano.get_entity_with_x_path("curso"))
    }


    // Auxiliar tests
    @Test
    fun test_get_attributes(){
        assertEquals(mutableListOf(plastico), garrafa.get_attributes())
    }

    @Test
    fun test_get_entity_text(){
        assertEquals("Automático", modelo.get_entity_text())
    }

    @Test
    fun test_change_entity_text(){
        modelo.change_entity_text("Não automático")
        assertEquals("Não automático", modelo.get_entity_text())
    }

    @Test
    fun test_remove_entity_text(){
        modelo.remove_entity_text()
        assertEquals("", modelo.get_entity_text())
    }

    @Test
    fun test_get_entity_xml(){
        val expected1 = "<componente nome=\"Quizzes\" peso=\"20%\"/>"
        val expected2 = "<curso>Mestrado em Engenharia Informática</curso>"

        assertEquals(expected1, doc_plano.get_entity_xml(componente1))
        assertEquals(expected2, doc_plano.get_entity_xml(curso1))
    }

    // Tests relating Part 2
//
//    @Test
//    fun test_component_class(){
//
//        val c = ComponenteAvaliacao("Quizzes", 20)
//        val output = "<utf-8>\n" + "<componente nome=\"Quizzes\" peso=\"20\"/>"
//        assertEquals(output, translate(c, encoding = "utf-8").pretty_print())
//    }
//
//    @Test
//    fun test_fuc_class(){
//
//        val f = FUC("M4310", "Programação Avançada", 6.0, "la la...",
//            listOf(
//                ComponenteAvaliacao("Quizzes", 20),
//                ComponenteAvaliacao("Projeto", 80)
//            )
//        )
//
//        val output = "<utf-8>\n" +
//                "<fuc codigo=\"M4310\">\n" +
//                "    <avaliacao>\n" +
//                "        <componente nome=\"Quizzes\" peso=\"20\"/>\n" +
//                "        <componente nome=\"Projeto\" peso=\"80\"/>\n" +
//                "    </avaliacao>\n" +
//                "    <ects>6.0</ects>\n" +
//                "    <nome>Programação Avançada</nome>\n" +
//                "</fuc>"
//        assertEquals(output, translate(f, encoding = "utf-8").pretty_print())
//    }

    @Test
    fun test_component_class(){

        val c = ComponenteAvaliacao("Quizzes", 20)

        val output = "<utf-8>\n" + "<componente nome=\"Quizzes\" peso=\"20%\"/>"

        assertEquals(output, translate(c, encoding = "utf-8").pretty_print())
    }


    @Test
    fun test_fuc_class(){

        val f = FUC("M4310", "Programação Avançada", 6.0, "la la...",
            listOf(
                ComponenteAvaliacao("Quizzes", 20),
                ComponenteAvaliacao("Projeto", 80)
            )
        )

        val output = "<utf-8>\n" +
                "<ufc codigo=\"M4310\">\n" +
                "    <avaliacao>\n" +
                "        <componente nome=\"Quizzes\" peso=\"20%\"/>\n" +
                "        <componente nome=\"Projeto\" peso=\"80%\"/>\n" +
                "    </avaliacao>\n" +
                "    <ects>6.0</ects>\n" +
                "    <nome>Programação Avançada.</nome>\n" +
                "</ufc>"

        assertEquals(output, translate(f, encoding = "utf-8").pretty_print())
    }

    // Relating Part 3 - Internal DS
    @Test
    fun test_dsl_get() {
        assertEquals(copito, copo.get("copito"))
    }

    @Test
    fun test_dsl_build(){
        val aux = entity("C") {
            attribute("Att", "1")
            attribute("Att", "2")
            entity("D", text = "Entity D") {
                attribute("Att", "3")
            }
        }
        val result= "C\n" +
                "Attribute(name='Att', value='1')\n" +
                "Attribute(name='Att', value='2')\n" +
                "  D (Text = Entity D)\n" +
                "  Attribute(name='Att', value='3')\n"

        assertEquals(result, aux.toTree())
    }
}