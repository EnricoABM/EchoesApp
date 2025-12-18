package com.nohana.projetoiot.view.nfc.config

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nohana.projetoiot.R
import com.nohana.projetoiot.model.ListeningPoint

/**
 * Adapter para exibir uma lista de [ListeningPoint] em um [RecyclerView].
 *
 * Cada item da lista permite ao usuário selecionar uma doença específica para o ponto de escuta
 * correspondente através de um [android.widget.Spinner]. As seleções do usuário são armazenadas
 * e podem ser recuperadas.
 */
class ListeningPointAdapter : RecyclerView.Adapter<ListeningPointViewHolder>() {
    /**
     * Mapa para armazenar a doença selecionada para cada ponto de escuta.
     * A chave é o nome da posição ([ListeningPoint.positionName]) e o valor é o nome da doença selecionada.
     */
    val selectedDiseases = mutableMapOf<String, String>()

    /**
     * Lista interna de [ListeningPoint] a serem exibidos pelo adapter.
     */
    private val list: MutableList<ListeningPoint> = mutableListOf()

    /**
     * Chamado quando o RecyclerView precisa de um novo [ListeningPointViewHolder] do tipo informado
     * para representar um item.
     *
     * Este novo ViewHolder deve ser construído com uma nova View que pode representar os itens
     * do tipo informado. Você pode criar Views manualmente ou inflá-las de um arquivo de layout XML.
     *
     * @param parent O ViewGroup no qual a nova View será adicionada depois de ser vinculada a
     *               uma posição do adapter.
     * @param viewType O tipo de view da nova View.
     * @return Um novo [ListeningPointViewHolder] que contém uma View do tipo de view informado.
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListeningPointViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_posicao, parent, false)
        return ListeningPointViewHolder(view)
    }

    /**
     * Chamado pelo RecyclerView para exibir os dados na posição especificada. Este método deve
     * atualizar o conteúdo do [ListeningPointViewHolder.itemView] para refletir o item na
     * posição informada.
     *
     * @param holder O [ListeningPointViewHolder] que deve ser atualizado para representar o
     *               conteúdo do item na posição informada no conjunto de dados.
     * @param position A posição do item dentro do conjunto de dados do adapter.
     */
    override fun onBindViewHolder(
        holder: ListeningPointViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val listeningPoint = list[position]
        holder.position.text = listeningPoint.positionName

        val diseases = listeningPoint.getDiseases().map { it.name }

        // Pré-seleciona a doença ativa ou a primeira da lista no Spinner
        val initialSelectedDisease = listeningPoint.getActiveDisease()?.name
            ?: diseases.firstOrNull() // Garante que não haja NPE se diseases estiver vazia
        if (initialSelectedDisease != null) {
            selectedDiseases[listeningPoint.positionName] = initialSelectedDisease
        }


        val spinnerAdapter = ArrayAdapter(
            holder.diseases.context,
            android.R.layout.simple_spinner_item,
            diseases
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        holder.diseases.adapter = spinnerAdapter

        // Define a seleção inicial do Spinner
        val initialSpinnerPosition = diseases.indexOf(initialSelectedDisease)
        if (initialSpinnerPosition >= 0) {
            holder.diseases.setSelection(initialSpinnerPosition)
        }

        holder.diseases.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                pos: Int,
                id: Long
            ) {
                // Atualiza o mapa de doenças selecionadas quando uma nova doença é escolhida
                if (diseases.isNotEmpty()) { // Garante que a lista não está vazia
                    selectedDiseases[listeningPoint.positionName] = diseases[pos]
                    Log.d("ListeningPointAdapter", "Doenças selecionadas: $selectedDiseases")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Não é necessário tratar este caso aqui
            }
        }
    }

    /**
     * Retorna o número total de itens neste conjunto de dados mantido pelo adapter.
     *
     * @return O número total de itens neste adapter.
     */
    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * Atualiza a lista de [ListeningPoint] exibida pelo adapter.
     *
     * Limpa a lista atual, adiciona todos os itens da nova lista e notifica o adapter
     * sobre a mudança nos dados para que a UI possa ser atualizada. O mapa de doenças
     * selecionadas também é limpo.
     *
     * @param newList A nova lista de [ListeningPoint] a ser exibida.
     */
    fun updateList(newList: List<ListeningPoint>) {
        this.list.clear()
        this.list.addAll(newList)
        selectedDiseases.clear() // Limpa seleções anteriores ao atualizar a lista
        notifyDataSetChanged() // Notifica que todos os itens mudaram, embora possa ser otimizado
    }
}
