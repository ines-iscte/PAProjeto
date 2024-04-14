import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

class Tests {

    val peso = Entity(name="peso")
    val modelo = Entity(name="modelo", text="Automático")
    val plastico = Attribute("Material", "Plastico")
    val papel = Attribute("Material", "Papel")
    val garrafa =  Entity(name="garrafa", attributes= mutableListOf(plastico))
    val garrafa2 =  Entity(name="garrafa", attributes= mutableListOf(papel))
    val doc = Document(name="doc")

    val doc_plano = Document(name="doc_plano")

    // A usar no teste de Pretty Print (Ponto 4.)
    val plano = Entity(name="plano")
    val curso1 = Entity(name="curso", text="Mestrado em Engenharia Informática", parent = plano)
    val codigo1 = Attribute(name="codigo", value="M4310")
    val fuc1 = Entity(name="fuc", attributes= mutableListOf(codigo1), parent = plano)
    val nome1 = Entity(name="nome", text="Programação Avançada", parent=fuc1)
    val ects1 = Entity(name="ects", text="6.0", parent=fuc1)
    val avaliacao1 = Entity(name="avaliacao", parent=fuc1)
    val componente_nome1 = Attribute(name="nome", value="Quizzes")
    val componente_nome2 = Attribute(name="peso", value="20%")
    val componente1 = Entity(name="componente", attributes= mutableListOf(componente_nome1, componente_nome2), parent=avaliacao1)
    val componente_nome3 = Attribute(name="nome", value="Projeto")
    val componente_nome4 = Attribute(name="peso", value="80%")
    val componente2 = Entity(name="componente", attributes= mutableListOf(componente_nome3, componente_nome4), parent=avaliacao1)

    val codigo2 = Attribute(name="codigo", value="03782")
    val fuc2 = Entity(name="fuc", attributes= mutableListOf(codigo2), parent = plano)
    val nome2 = Entity(name="nome", text="Dissertação", parent=fuc2)
    val ects2 = Entity(name="ects", text="42.0", parent=fuc2)
    val avaliacao2 = Entity(name="avaliacao", parent=fuc2)
    val componente_nome5 = Attribute(name="nome", value="Dissertação")
    val componente_nome6 = Attribute(name="peso", value="60%")
    val componente3 = Entity(name="componente", attributes= mutableListOf(componente_nome5, componente_nome6), parent=avaliacao2)
    val componente_nome7 = Attribute(name="nome", value="Apresentação")
    val componente_nome8 = Attribute(name="peso", value="20%")
    val componente4 = Entity(name="componente", attributes= mutableListOf(componente_nome7, componente_nome8), parent=avaliacao2)
    val componente_nome9 = Attribute(name="nome", value="Discussão")
    val componente_nome10 = Attribute(name="peso", value="20%")
    val componente5 = Entity(name="componente", attributes= mutableListOf(componente_nome9, componente_nome10), parent=avaliacao2)

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
    fun test_all_children_list_of_entity(){
        val objeto = Entity(name="objeto")
        val copo =  Entity(name="copo", text="De vinho", attributes= mutableListOf(plastico, papel), parent=objeto)
        val copito = Entity(name="copito", text="De vinho branco", parent=copo)
        assertEquals(listOf(copo, copito), objeto.get_all_Children())
    }

    // Ponto 3.
    @Test
    fun test_children_list_of_entity(){
        val objeto = Entity(name="objeto")
        val copo =  Entity(name="copo", text="De vinho", attributes= mutableListOf(plastico, papel), parent=objeto)
        val copito = Entity(name="copito", text="De vinho branco", parent=objeto)
        assertEquals(listOf(copo, copito), objeto.get_Children())
    }

    // Ponto 3.
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

    // Ponto 5.
    @Test
    fun test_children_list_of_entity_vis(){
        val objeto = Entity(name="objeto")
        val copo =  Entity(name="copo", text="De vinho", attributes= mutableListOf(plastico, papel), parent=objeto)
        val copito = Entity(name="copito", text="De vinho branco", parent=objeto)
        val doc11 = Document(name="doc11")
        doc11.addEntity(objeto)
        doc11.addEntity(copo)
        doc11.addEntity(copito)
        assertEquals(listOf(copo, copito), doc11.get_Children_vis(objeto))
    }

    // Ponto 5.
    @Test
    fun test_parent_of_entity_vis(){
        val objeto = Entity(name="objeto")
        val copo =  Entity(name="copo", text="De vinho", attributes= mutableListOf(plastico, papel), parent=objeto)
        val copito = Entity(name="copito", text="De vinho branco", parent=copo)
        val doc11 = Document(name="doc11")
        doc11.addEntity(objeto)
        doc11.addEntity(copo)
        doc11.addEntity(copito)
        assertEquals(copo, doc11.get_Parent_vis(copito))
    }

    // Ponto 5.
    @Test
    fun test_parent_and_children_vis(){
        val objeto = Entity(name="objeto")
        val copo =  Entity(name="copo", text="De vinho", attributes= mutableListOf(plastico, papel), parent=objeto)
        val copito = Entity(name="copito", text="De vinho branco", parent=copo)
        val doc11 = Document(name="doc11")
        doc11.addEntity(objeto)
        doc11.addEntity(copo)
        doc11.addEntity(copito)
        assertEquals(listOf(objeto, copito), doc11.get_Parent_and_Children_vis(copo))
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

    // Ponto 4.
    @Test
    fun test_pretty_print(){
//        doc.addEntity(peso)
//        val modelo = Entity(name="modelo", text="Automatico", parent=peso)
//        val garrafa =  Entity(name="garrafa", attributes= mutableListOf(plastico), parent=peso)
//        doc.addEntity(modelo)
//        doc.addEntity(garrafa)

        doc_plano.addEntity(plano)
        doc_plano.addEntity(curso1)
        doc_plano.addEntity(fuc1)
        doc_plano.addEntity(nome1)
        doc_plano.addEntity(ects1)
        doc_plano.addEntity(avaliacao1)
        doc_plano.addEntity(componente1)
        doc_plano.addEntity(componente2)
        doc_plano.addEntity(fuc2)
        doc_plano.addEntity(nome2)
        doc_plano.addEntity(ects2)
        doc_plano.addEntity(avaliacao2)
        doc_plano.addEntity(componente3)
        doc_plano.addEntity(componente4)
        doc_plano.addEntity(componente5)

        val expected_output = """
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

        assertEquals(expected_output, doc_plano.prettyPrint(doc_plano.entities[0]))
    }

    // Ponto 4.
    @Test
    fun test_pretty_print_to_file(){
        doc_plano.addEntity(plano)
        doc_plano.addEntity(curso1)
        doc_plano.addEntity(fuc1)
        doc_plano.addEntity(nome1)
        doc_plano.addEntity(ects1)
        doc_plano.addEntity(avaliacao1)
        doc_plano.addEntity(componente1)
        doc_plano.addEntity(componente2)
        doc_plano.addEntity(fuc2)
        doc_plano.addEntity(nome2)
        doc_plano.addEntity(ects2)
        doc_plano.addEntity(avaliacao2)
        doc_plano.addEntity(componente3)
        doc_plano.addEntity(componente4)
        doc_plano.addEntity(componente5)

        val expected_output = """
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

        // Chame a função para escrever a representação formatada no arquivo
        doc_plano.prettyPrintToFile(doc_plano.entities[0], outputFile = testFile)

        // Verifique se o arquivo foi criado
        assert(testFile.exists())

        // Verifique se o conteúdo do arquivo é o mesmo que a representação formatada da entidade
        val actualXml = testFile.readText()
        assertEquals(expected_output, actualXml)

        // Limpeza: Exclua o arquivo após o teste
        testFile.delete()
    }
        //assertEquals(expected_output, doc_plano.prettyPrint(doc_plano.entities[0]))

    // Ponto 6.
    @Test
    fun test_add_global_attribute(){
        doc.addEntity(garrafa)
        doc.addEntity(garrafa2)

        doc.add_global_attribute("garrafa", "Material", "Cartao")
        val expectedAttributes = mutableListOf(Attribute("Material", "Plastico"), Attribute("Material", "Cartao"))
        assertEquals(expectedAttributes, garrafa.attributes)
    }

    // Ponto 6.
    @Test
    fun test_add_global_attribute_vis(){
        doc.addEntity(garrafa)
        doc.addEntity(garrafa2)

        doc.add_global_attribute_vis("garrafa", "Material", "Cartao")
        val expectedAttributes = mutableListOf(Attribute("Material", "Plastico"), Attribute("Material", "Cartao"))
        assertEquals(expectedAttributes, garrafa.attributes)
    }

    // Ponto 7.
    @Test
    fun test_rename_global_entity(){
        doc.addEntity(modelo)
        doc.rename_global_entity("modelo", "model")
        assertEquals("model", modelo.name)
    }

    // Com visitante
    @Test
    fun test_rename_global_entity_vis(){
        doc.addEntity(modelo)
        doc.rename_global_entity_vis("modelo", "modelos")
        assertEquals("modelos", modelo.name)
    }

    // Ponto 8
    @Test
    fun test_rename_global_attributes(){
        doc.addEntity(garrafa)
        doc.rename_global_attributes("garrafa", "Material", "Materia")
        assertEquals("Materia", garrafa.attributes.get(0).name)
    }

    // Com visitante
    @Test
    fun test_rename_global_attributes_vis(){
        doc.addEntity(garrafa)
        doc.rename_global_attributes_vis("garrafa", "Material", "Materiais")
        assertEquals("Materiais", garrafa.attributes.get(0).name)
    }

    // Ponto 9.
    @Test
    fun test_remove_global_entities(){
        val garrafinha = Entity("garrafinha", parent=garrafa)
        doc.addEntity(garrafa)
        doc.addEntity(modelo)
        doc.remove_global_entities("garrafa")
        assertEquals(listOf(modelo), doc.entities)
    }

    //Com visitante
    @Test
    fun test_remove_global_entities_vis(){
        val garrafinha = Entity("garrafinha", parent=garrafa)
        doc.addEntity(garrafa)
        doc.addEntity(modelo)
        doc.remove_global_entities_vis("garrafa")
        assertEquals(listOf(modelo), doc.entities)
    }

    // Ponto 10.
    @Test
    fun test_remove_global_attributes(){
        val pretos = Attribute("Materiais", "Pretos")
        garrafa.addAttribute(pretos)
        doc.addEntity(garrafa)
        doc.addEntity(modelo)
        doc.remove_global_attributes("garrafa", "Material")
        assertEquals(listOf(pretos), garrafa.attributes)
    }

    //Com visitante
    @Test
    fun test_remove_global_attributes_vis(){
        val pretos = Attribute("Materiais", "Pretos")
        garrafa.addAttribute(pretos)
        doc.addEntity(garrafa)
        doc.addEntity(modelo)
        doc.remove_global_attributes_vis("garrafa", "Material")
        assertEquals(listOf(pretos), garrafa.attributes)
    }

    // Testes auxiliares
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



}