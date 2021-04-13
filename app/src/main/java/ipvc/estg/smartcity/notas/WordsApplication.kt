package ipvc.estg.smartcity.notas

import android.app.Application
import ipvc.estg.smartcity.db.WordRepository
import ipvc.estg.smartcity.db.WordRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class WordsApplication : Application() {
        // Using by lazy so the database and the repository are only created when they're needed
        // rather than when the application starts

        val applicationScope = CoroutineScope(SupervisorJob())

        val database by lazy { WordRoomDatabase.getDatabase(this, applicationScope) }
        val repository by lazy { WordRepository(database.wordDao()) }
}

