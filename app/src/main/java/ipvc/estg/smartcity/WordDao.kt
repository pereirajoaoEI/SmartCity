package ipvc.estg.smartcity

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {

    @Query("SELECT * FROM notas ORDER BY id DESC")
    fun getAlphabetizedWords(): Flow<List<Word>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: Word)

    @Query("DELETE FROM notas")
    suspend fun deleteAll()

    @Query("DELETE FROM notas where id == :id")
    suspend fun deleteNota(id: Int)

    @Update
    suspend fun updateNota(word: Word)

}
