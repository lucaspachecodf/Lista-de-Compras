package livrokotlin.com.br.listadecompras

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class ListaComprasDatabase(context: Context) :
    ManagedSQLiteOpenHelper(ctx = context, name = "listaCompras.db", version = 1) {

    //Singleton da classe
    companion object {
        private var instance: ListaComprasDatabase? = null

        @Synchronized
        fun getInstance(ctx: Context): ListaComprasDatabase {
            if (instance == null) {
                instance = ListaComprasDatabase(ctx.applicationContext)
            }

            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {

        db.createTable(
            "produto", true,
            "id" to INTEGER + PRIMARY_KEY + UNIQUE,
            "nome" to TEXT,
            "quantidade" to INTEGER,
            "valor" to REAL,
            "foto" to BLOB
        )
    }

    override fun onUpgrade(p0: SQLiteDatabase?, oudVersion: Int, newVersion: Int) {

    }
}

//	Acesso à propriedade pelo contexto
val Context.database: ListaComprasDatabase
    get() = ListaComprasDatabase.getInstance(getApplicationContext())