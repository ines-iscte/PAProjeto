import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

class Tests {

    // Relating Part 1 - XML Modelling

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
        modelo.changeEntityText("Automático")
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
        assertEquals("Material", plastico.getAttributeName())
        assertEquals("Papel", papel.getAttributeValue())
    }

    // Point 2.
    @Test
    fun testAddAttributeToEntity(){
        val reciclado = Attribute("Reciclado", "1.40€")
        modelo.addAttribute(reciclado)
        val normal = Attribute("Normal", "1.20€")
        modelo.addAttribute(normal)
        assertEquals(listOf(reciclado, normal), modelo.getAttributes())
    }

    // Point 2.
    @Test
    fun testRemoveAttributeToEntity(){
        val reciclado = Attribute("Reciclado", "1.40€")
        modelo.addAttribute(reciclado)
        val normal = Attribute("Normal", "1.20€")
        modelo.addAttribute(normal)
        modelo.removeAttribute(reciclado)
        assertEquals(listOf(normal), modelo.getAttributes())
    }

    // Point 2.
    @Test
    fun testChangeAttributeToEntity(){
        val reciclado = Attribute("Reciclado", "1.40€")
        modelo.addAttribute(reciclado)
        val normal = Attribute("Normal", "1.20€")
        modelo.addAttribute(normal)
        modelo.changeAttribute(attribute=reciclado, new_value="1.50€")
        modelo.changeAttribute(attribute=normal, new_name="Semi-normal", new_value="1.70€")

        val expectedAttributes = listOf(
            Attribute("Reciclado", "1.50€"),
            Attribute("Semi-normal", "1.70€")
        )

        assertEquals(expectedAttributes, modelo.getAttributes())
    }


    // Entity Tests
    @Test
    fun testCreateEntity() {
        assertEquals("modelo", modelo.getName())
        assertEquals("Automático", modelo.getEntityText())
        assertEquals(mutableListOf(plastico), garrafa.getAttributes())
    }

    // Point 3.
    @Test
    fun testAllChildrenListOfEntity(){
        assertEquals(listOf(copo, copito), objeto.getAllChildren())
    }

    // Point 3.
    @Test
    fun testChildrenListOfEntity(){
        assertEquals(listOf(copo), objeto.getChildren())
    }

    // Point 3.
    @Test
    fun testParentOfEntity(){
        assertEquals(copo, copito.getParent())
    }

    // Point 3.
    @Test
    fun testParentAndChildren(){
        assertEquals(listOf(objeto, copito), copo.getParentAndChildren())
    }

    // Points 3 and 5.
    @Test
    fun testChildrenListOfEntityVis(){
        doc.addEntity(objeto)
        doc.addEntity(copo)
        doc.addEntity(copito)
        assertEquals(listOf(copo), doc.getChildrenVis(objeto))
    }

    // Points 3 and 5.
    @Test
    fun testParentOfEntityVis(){
        doc.addEntity(objeto)
        doc.addEntity(copo)
        doc.addEntity(copito)
        assertEquals(copo, doc.getParentVis(copito))
    }

    // Points 3 and 5.
    @Test
    fun testParentAndChildrenVis(){
        doc.addEntity(objeto)
        doc.addEntity(copo)
        doc.addEntity(copito)
        assertEquals(listOf(objeto, copito), doc.getParentAndChildrenVis(copo))
    }


    // Document tests
    @Test
    fun testCreateDocument(){
        assertEquals("modelo", doc.getChild().getName())
    }

    // Point 1.
    @Test
    fun testAddEntityToDocument(){
        doc.addEntity(objeto)
        doc.addEntity(copo)
        doc.addEntity(copito)
        assertEquals(listOf(objeto, copo, copito), doc.getEntities())
    }

    // Point 1.
    @Test
    fun testRemoveEntityToDocument(){
        doc.addEntity(objeto)
        doc.addEntity(copo)
        doc.addEntity(copito)
        doc.removeEntity(copito)
        assertEquals(listOf(objeto, copo), doc.getEntities())
    }

    // Point 4.
    @Test
    fun testPrettyPrint(){

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

        assertEquals(expected_output, doc_plano.prettyPrint())

    }

    // Point 4.
    @Test
    fun testPrettyPrintToFile(){

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

        doc_plano.prettyPrintToFile(outputFile = testFile)

        assert(testFile.exists())

        val actualXml = testFile.readText()
        assertEquals(expected_output, actualXml)

        testFile.delete()
    }

    // Point 6.
    @Test
    fun testAddGlobalAttribute(){
        doc.addEntity(garrafa)
        doc.addEntity(garrafa2)

        doc.addGlobalAttribute("garrafa", "Material", "Cartao")
        val expectedAttributes = mutableListOf(Attribute("Material", "Plastico"), Attribute("Material", "Cartao"))
        assertEquals(expectedAttributes, garrafa.getAttributes())
    }

    // Points 6 and 5.
    @Test
    fun testAddGlobalAttributeVis(){
        doc.addEntity(garrafa)
        doc.addEntity(garrafa2)

        doc.addGlobalAttributeVis("garrafa", "Material", "Cortiça")
        val expectedAttributes = mutableListOf(Attribute("Material", "Plastico"), Attribute("Material", "Cortiça"))
        assertEquals(expectedAttributes, garrafa.getAttributes())
    }

    // Point 7.
    @Test
    fun testRenameGlobalEntity(){
        doc.addEntity(objeto)
        doc.renameGlobalEntity("objeto", "objeta")
        assertEquals("objeta", objeto.getName())
    }

    // Points 7 and 5.
    @Test
    fun testRenameGlobalEntityVis(){
        doc.addEntity(objeto)
        doc.renameGlobalEntityVis("objeto", "objetos")
        assertEquals("objetos", objeto.getName())
    }

    // Point 8.
    @Test
    fun testRenameGlobalAttributes(){
        doc.addEntity(garrafa)
        doc.renameGlobalAttributes("garrafa", "Material", "Materia")
        assertEquals("Materia", garrafa.getAttributes().get(0).getAttributeName())
    }

    // Points 8 and 5.
    @Test
    fun testRenameGlobalAttributesVis(){
        doc.addEntity(garrafa)
        doc.renameGlobalAttributesVis("garrafa", "Material", "Materia")
        assertEquals("Materia", garrafa.getAttributes().get(0).getAttributeName())
    }

    // Point 9.
    @Test
    fun testRemoveGlobalEntities(){
        doc.addEntity(objeto)
        doc.addEntity(copo)
        doc.addEntity(copito)
        doc.removeGlobalEntities("copito")
        assertEquals(listOf(objeto, copo), doc.getEntities())
    }

    // Points 9 and 5.
    @Test
    fun testRemoveGlobalEntitiesVis(){
        doc.addEntity(objeto)
        doc.addEntity(copo)
        doc.addEntity(copito)
        doc.removeGlobalEntitiesVis("copito")
        assertEquals(listOf(objeto, copo), doc.getEntities())
    }

    // Point 10.
    @Test
    fun testRemoveGlobalAttributes(){
        val escuro = Attribute("Cor", "Escuro")
        garrafa.addAttribute(escuro)
        garrafa2.addAttribute(escuro)
        doc.addEntity(garrafa)
        doc.addEntity(garrafa2)
        doc.removeGlobalAttributes("garrafa", "Material")
        assertEquals(listOf(escuro), garrafa.getAttributes())
        assertEquals(listOf(escuro), garrafa2.getAttributes())
    }

    // Points 10 and 5.
    @Test
    fun testRemoveGlobalAttributesVis(){
        val escuro = Attribute("Cor", "Escuro")
        garrafa.addAttribute(escuro)
        garrafa2.addAttribute(escuro)
        doc.addEntity(garrafa)
        doc.addEntity(garrafa2)
        doc.removeGlobalAttributesVis("garrafa", "Material")
        assertEquals(listOf(escuro), garrafa.getAttributes())
        assertEquals(listOf(escuro), garrafa2.getAttributes())
    }

    // Micro-XPath
    @Test
    fun testGetEntityXmlWithXPath(){
        val expected1=   """
            <componente nome="Quizzes" peso="20%"/>
            <componente nome="Projeto" peso="80%"/>
            <componente nome="Dissertação" peso="60%"/>
            <componente nome="Apresentação" peso="20%"/>
            <componente nome="Discussão" peso="20%"/>""".trimIndent()

        val expected2 = "<curso>Mestrado em Engenharia Informática</curso>"

        assertEquals(expected1, doc_plano.getEntityXmlWithXPath("fuc/avaliacao/componente"))
        assertEquals(expected2, doc_plano.getEntityXmlWithXPath("curso"))
    }

    @Test
    fun testGetEntityWithXPath(){
        assertEquals(listOf(componente1, componente2, componente3, componente4, componente5), doc_plano.getEntityWithXPath("fuc/avaliacao/componente"))
        assertEquals(listOf(curso1), doc_plano.getEntityWithXPath("curso"))
    }


    // Auxiliar tests
    @Test
    fun testGetAttributes(){
        assertEquals(mutableListOf(plastico), garrafa.getAttributes())
    }

    @Test
    fun testGetEntityText(){
        assertEquals("Automático", modelo.getEntityText())
    }

    @Test
    fun testChangeEntityText(){
        modelo.changeEntityText("Não automático")
        assertEquals("Não automático", modelo.getEntityText())
    }

    @Test
    fun testRemoveEntityText(){
        modelo.removeEntityText()
        assertEquals("", modelo.getEntityText())
    }

    @Test
    fun testGetEntityXml(){
        val expected1 = "<componente nome=\"Quizzes\" peso=\"20%\"/>"
        val expected2 = "<curso>Mestrado em Engenharia Informática</curso>"

        assertEquals(expected1, doc_plano.getEntityXml(componente1))
        assertEquals(expected2, doc_plano.getEntityXml(curso1))
    }

    // Tests relating Part 2 - Class Mapping
    @Test
    fun testComponentClass(){

        val c = ComponenteAvaliacao("Quizzes", 20)

        val output = "<utf-8>\n" + "<comp nome=\"Quizzes\" peso=\"20%\"/>"

        assertEquals(output, translate(c, encoding = "utf-8").prettyPrint())
    }


    @Test
    fun testFucClass(){

        val f = FUC("M4310", "Programação Avançada", 6.0, "la la...",
            listOf(
                ComponenteAvaliacao("Quizzes", 20),
                ComponenteAvaliacao("Projeto", 80)
            )
        )

        val output = "<utf-8>\n" +
                "<fuc codigo=\"M4310\" Modo=\"Presencial\">\n" +
                "    <avaliacao>\n" +
                "        <comp nome=\"Quizzes\" peso=\"20%\"/>\n" +
                "        <comp nome=\"Projeto\" peso=\"80%\"/>\n" +
                "    </avaliacao>\n" +
                "    <ects>6.0</ects>\n" +
                "    <nome>Programação Avançada.</nome>\n" +
                "</fuc>"

        assertEquals(output, translate(f, encoding = "utf-8").prettyPrint())
    }

    // Relating Part 3 - Internal DSL
    @Test
    fun testDslGet() {
        assertEquals(copito, copo.get("copito"))
    }

    @Test
    fun testDslBuild(){
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