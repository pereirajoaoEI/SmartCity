package ipvc.estg.smartcity.dao

import androidx.room.*
import ipvc.estg.smartcity.entities.Word
import kotlinx.coroutines.flow.Flow
//teste
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
