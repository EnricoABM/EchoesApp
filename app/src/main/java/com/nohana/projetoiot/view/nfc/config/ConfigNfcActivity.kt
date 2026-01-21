package com.nohana.projetoiot.view.nfc.config

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nohana.projetoiot.R
import com.nohana.projetoiot.viewmodel.AnimalModelView

/**
 * Activity responsável pela configuração de pontos de escuta associados a um animal.
 *
 * Esta tela permite ao usuário selecionar um animal de um [Spinner] e, em seguida,
 * visualizar e potencialmente modificar os pontos de escuta e suas doenças associadas
 * em um [RecyclerView]. As alterações podem ser salvas através de um botão.
 * Utiliza [AnimalModelView] para interagir com os dados relacionados aos animais.
 */
class ConfigNfcActivity : AppCompatActivity() {
    // Views da interface do usuário
    private lateinit var recycler: RecyclerView
    private lateinit var saveButton: Button
    private lateinit var backButton: ImageButton
    private lateinit var animalImage: ImageView
    private lateinit var animalSpinner: Spinner

    // ViewModel para acessar os dados dos animais
    private lateinit var animalMV: AnimalModelView


    /**
     * Chamado quando a activity está sendo criada pela primeira vez.
     *
     * @param savedInstanceState Se a activity estiver sendo reiniciada após ter sido
     *                           anteriormente encerrada, este Bundle contém os dados que ela
     *                           forneceu mais recentemente em [onSaveInstanceState].
     *                           Caso contrário, é nulo.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc_config) // Define o layout da activity

        // Inicializando Componentes da UI
        saveButton = findViewById<Button>(R.id.tela_nfc_config_botao)
        recycler = findViewById<RecyclerView>(R.id.tela_nfc_config_recycler)
        animalSpinner = findViewById<Spinner>(R.id.tela_nfc_config_animal)
        backButton = findViewById<ImageButton>(R.id.tela_nfc_config_botao_voltar)
        animalImage = findViewById<ImageView>(R.id.tela_nfc_config_image)
        // Inicializando o ViewModel
        animalMV = ViewModelProvider(this)[AnimalModelView::class.java]


        // Configuração do Spinner de Animais
        val spinnerAdapter = object : ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            arrayListOf<String>() // Inicializa com uma lista vazia
        ) {
            /**
             * Controla se um item no spinner está habilitado ou desabilitado.
             * O primeiro item (placeholder "Animal") é desabilitado.
             * @param position A posição do item no adapter.
             * @return `true` se o item estiver habilitado, `false` caso contrário.
             */
            override fun isEnabled(position: Int): Boolean {
                return position != 0 // Desabilita o primeiro item (placeholder)
            }
        }
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        animalSpinner.adapter = spinnerAdapter

        // Configuração do RecyclerView para os pontos de escuta
        val recyclerViewAdapter = ListeningPointAdapter()
        recycler.adapter = recyclerViewAdapter
        recycler.layoutManager = LinearLayoutManager(this) // Define o layout como linear vertical

        // Observa mudanças na lista de animais do ViewModel
        animalMV.animalLiveData.observe(this) { list ->
            // Verifica se a lista não é nula para evitar NullPointerException
            if (list != null) {
                // Mapeia a lista de objetos Animal para uma lista de nomes (Strings)
                val names = list.map { it.name }.toMutableList()
                names.add(0, "Animal") // Adiciona um placeholder no início da lista
                spinnerAdapter.clear() // Limpa os itens antigos do adapter
                spinnerAdapter.addAll(names) // Adiciona os novos nomes ao adapter
            }
        }

        // Listener para seleção de itens no Spinner de animais
        animalSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            /**
             * Chamado quando um item no spinner é selecionado.
             * @param parent O AdapterView onde a seleção ocorreu.
             * @param view A view dentro do AdapterView que foi clicada.
             * @param position A posição do item selecionado no adapter.
             * @param id O ID da linha do item selecionado.
             */
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val name = parent.getItemAtPosition(position) as String
                // Se a posição selecionada não for o placeholder (posição 0)
                if (position != 0) {
                    loadListeningPoints(name) // Carrega os pontos de escuta para o animal selecionado
                }
            }

            /**
             * Chamado quando nada é selecionado no spinner.
             * @param p0 O AdapterView que não tem mais uma seleção.
             */
            override fun onNothingSelected(p0: AdapterView<*>?) {
                // Nenhuma ação necessária aqui neste caso
            }
        }

        // Configuração do listener de clique para o botão Salvar
        saveButton.setOnClickListener {
            save(recyclerViewAdapter) // Chama a função para salvar as configurações
            Toast.makeText(this, "Configurações salvas", Toast.LENGTH_SHORT).show()
            finish() // Fecha a activity atual e retorna para a anterior
        }

        // Configuração do listener de clique para o botão Voltar
        backButton.setOnClickListener {
            finish() // Fecha a activity atual e retorna para a anterior
        }
    }

    /**
     * Salva as configurações atuais dos pontos de escuta para o animal selecionado.
     * Recupera as doenças selecionadas do adapter do RecyclerView e o nome do animal
     * do Spinner, e então chama o ViewModel para atualizar os dados.
     *
     * @param adapter O [ListeningPointAdapter] do RecyclerView, usado para obter as
     *                doenças selecionadas.
     */
    fun save(adapter: ListeningPointAdapter) {
        val options = adapter.selectedDiseases
        val name = animalSpinner.selectedItem as String // Obtém o nome do animal selecionado
        animalMV.updateAnimal(name, options) // Chama o ViewModel para atualizar o animal
    }

    /**
     * Carrega e exibe os pontos de escuta para um animal específico.
     * Observa os dados do animal (obtidos pelo nome) do ViewModel e atualiza
     * o adapter do RecyclerView com os pontos de escuta correspondentes.
     *
     * @param name O nome do animal cujos pontos de escuta devem ser carregados.
     */
    fun loadListeningPoints(name: String) {
        animalMV.findAnimalByName(name).observe(this) { animal ->
            // Garante que o adapter seja do tipo esperado
            val adapter = recycler.adapter as ListeningPointAdapter
            animalImage.setImageResource(animal.imageUrl)
            adapter.updateList(animal.getListeningPoints()) // Atualiza o RecyclerView com os novos pontos
        }
    }
}
