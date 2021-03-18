package ipvc.estg.smartcity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {

    @Query("SELECT * FROM notas ORDER BY id ASC")
    fun getAlphabetizedWords(): Flow<List<Word>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: Word)

    @Query("DELETE FROM notas")
    suspend fun deleteAll()

    @Query("DELETE FROM notas where id == :id")
    suspend fun deleteNota(id: Int)

}
