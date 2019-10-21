package livrokotlin.com.br.listadecompras

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.db.parseList
import org.jetbrains.anko.db.rowParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.startActivity
import java.text.NumberFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val produtoAdapter = ProdutoAdapter(this)
        list_view.adapter = produtoAdapter

        list_view.setOnItemLongClickListener { adapterView, view, position, id ->

            val item = produtoAdapter.getItem(position)
            produtoAdapter.remove(item)

            true
        }

        btn_adicionar.setOnClickListener {
            startActivity<CadastroActivity>()
        }
    }

    override fun onResume() {
        super.onResume()

        val adapter = list_view.adapter as ProdutoAdapter

        database.use {

            select("produto").exec {
                //Criando	o	parser	que	montará	o	objeto	produto
                val parser = rowParser { id: Int, nome: String,
                                         quantidade: Int,
                                         valor: Double,
                                         foto: ByteArray? ->
                    //Colunas	do	banco	de	dados
                    //Montagem	do	objeto	Produto	com	as	colunas	do	banco
                    Produto(id, nome, quantidade, valor, foto?.toBitmap())
                }
                //criando	a	lista	de	produtos	com	dados	do	banco
                var listaProdutos = parseList(parser)
                //limpando	os	dados	da	lista	e	carregando	as	novas	informaçõe                s
                adapter.clear()
                adapter.addAll(listaProdutos)
                //efetuando	a	multiplicação	e	soma	da	quantidade	e	valor
                val soma = listaProdutos.sumByDouble { it.valor * it.quantidade }
                //formatando	em	formato	moeda
                val f = NumberFormat.getCurrencyInstance(Locale("pt", "br"))
                lbl_total.text = "TOTAL:	${f.format(soma)}"
            }

        }
    }
}
