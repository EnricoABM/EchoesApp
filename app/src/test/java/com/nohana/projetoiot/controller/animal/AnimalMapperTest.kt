package com.nohana.projetoiot.controller.animal

import android.content.Context
import android.content.res.Resources
import com.nohana.projetoiot.database.entities.AnimalEntity
import com.nohana.projetoiot.database.relationships.AnimalAndListeningPoint
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class AnimalMapperTest {

    // Criamos "falsos" objetos para o teste
    @Mock
    lateinit var mockContext: Context
    @Mock
    lateinit var mockResources: Resources

    private lateinit var mapper: AnimalMapper

    @Before
    fun setup() {
        // Inicializa os mocks
        MockitoAnnotations.openMocks(this)

        // Configuramos o mock para que context.resources retorne nosso mockResources
        `when`(mockContext.resources).thenReturn(mockResources)
        `when`(mockContext.packageName).thenReturn("com.nohana.projetoiot")

        mapper = AnimalMapper(mockContext)
    }

    @Test
    fun `entityToModel deve converter corretamente entidades para modelo de dominio`() {
        // 1. GIVEN (Dado que temos uma entidade vinda do banco)
        val animalName = "Cachorro"
        val imageResourceName = "img_cachorro"
        val expectedResId = 12345 // ID falso simulado

        val animalEntity = AnimalEntity(id = 1, name = animalName, imageUrl = imageResourceName)
        val relation = AnimalAndListeningPoint(
            animal = animalEntity,
            listeningPoints = emptyList() // Simplificando para o primeiro teste
        )

        // Simulamos o comportamento do getIdentifier
        `when`(mockResources.getIdentifier(imageResourceName, "drawable", "com.nohana.projetoiot"))
            .thenReturn(expectedResId)

        // 2. WHEN (Quando chamamos o metodo de conversao)
        val result = mapper.entityToModel(relation)

        // 3. THEN (Entao o resultado deve ser o esperado)
        assertEquals(1, result.id)
        assertEquals(animalName, result.name)
        assertEquals(expectedResId, result.imageUrl)
    }
}