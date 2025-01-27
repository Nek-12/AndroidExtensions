package com.nek12.androidutils.room

import android.annotation.SuppressLint
import androidx.room.CoroutinesRoom
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.RawQuery
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.flow.Flow

/**
 * A generic dao class that provides CRUD methods for you for free.
 * Extend this class to add your own methods.
 * Provides insert, update, delete, and get queries for you.
 * @param db Just add this parameter to your DAO and pass it along to the RoomDao base constructor.
 *   This parameter is an implementation detail and is handled by Room codegen.
 *   example
 *   ```
 *   abstract class MyDao(db: RoomDatabase) : RoomDao<MyEntity>(db, MyEntity.TABLE_NAME)
 *   ```
 * @see RoomEntity
 * @see RoomDataSource
 **/
@Dao
@Suppress("MethodOverloading", "TooManyFunctions")
@Deprecated(Deprecation)
abstract class RoomDao<I : Any, T : RoomEntity<I>>(
    private val db: RoomDatabase,
    private val tableName: String,
) {

    /**
     * Add or replace an entity, depending on the id
     * @return The rowId of a newly-inserted entity (id if you use autogenerated numeric values for PKs, internal id otherwise)
     */
    @Upsert
    abstract suspend fun save(entity: T): Long

    /**
     * Add or replace [entities], depending on the id
     */
    @Upsert
    abstract suspend fun save(vararg entities: T)

    /**
     * Add or replace [entities], depending on the id
     */
    @Upsert
    abstract suspend fun save(entities: List<T>)

    /**
     * Add an [entity] or fail if it already exists.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract suspend fun add(entity: T): Long

    /**
     * Add [entities] or fail if any of them already exists.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract suspend fun add(vararg entities: T)

    /**
     * Add [entities] or fail if any of them already exists.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract suspend fun add(entities: List<T>)

    /**
     * @return The number of items that were updated
     */
    @Update
    abstract suspend fun update(entity: T): Int

    /**
     * @return The number of items that were updated
     */
    @Update
    abstract suspend fun update(entities: List<T>): Int

    /**
     * @return The number of items that were updated
     */
    @Update
    abstract suspend fun update(vararg entities: T): Int

    /**
     * @return The number of items that were deleted
     */
    @Delete
    abstract suspend fun delete(entity: T): Int

    /**
     * @return The number of items that were deleted
     */
    @Delete
    abstract suspend fun delete(vararg entities: T): Int

    /**
     * @return The number of items that were deleted
     */
    @Delete
    abstract suspend fun delete(entities: List<T>): Int

    /**
     * @return The number of items that were deleted
     */
    @JvmName("deleteById")
    suspend fun delete(ids: List<I>): Int {
        val idsQuery = buildSqlIdList(ids)
        val query = SimpleSQLiteQuery("DELETE FROM `$tableName` WHERE `id` IN ($idsQuery);")
        return delete(query)
    }

    /**
     * @return The number of items that were deleted
     */
    suspend fun delete(vararg ids: I): Int = delete(ids.toList())

    /**
     * @return The number of items that were deleted
     */
    suspend fun deleteAll(): Int {
        val query = SimpleSQLiteQuery("DELETE FROM `$tableName`;")
        return delete(query)
    }

    /**
     * Get an entity synchronously
     * @return null if no value found
     */
    suspend fun getSync(id: I): T? = getSync(listOf(id)).firstOrNull()

    /**
     * Get entities synchronously
     * @return empty list if no values found
     */
    suspend fun getSync(vararg ids: I): List<T> = getSync(ids.toList())

    /**
     * Get entities synchronously
     * @return empty list if no values found
     */
    suspend fun getAllSync(): List<T> {
        val query = SimpleSQLiteQuery("SELECT * FROM `$tableName`;")
        return getSync(query) ?: emptyList()
    }

    /**
     * Get entities synchronously
     * @return empty list if no values found
     */
    suspend fun getSync(ids: List<I>): List<T> = getSync(buildSqlIdQuery(ids)) ?: emptyList()

    /**
     * Use [kotlinx.coroutines.flow.distinctUntilChanged] to prevent
     * duplicate emissions when unrelated entities are changed
     * Re-emits values when any of the [referencedTables] change
     */
    @SuppressLint("RestrictedApi")
    fun get(id: I, vararg referencedTables: String): Flow<T?> = CoroutinesRoom.createFlow(
        db,
        false,
        arrayOf(tableName) + referencedTables
    ) {
        getBlocking(buildSqlIdQuery(listOf(id)))?.firstOrNull()
    }

    /**
     * Use [kotlinx.coroutines.flow.distinctUntilChanged] to prevent
     * duplicate emissions when unrelated entities are changed
     * Does not support setting referencedTables
     */
    fun get(referencedTables: List<String> = emptyList(), vararg ids: I): Flow<List<T>> =
        get(ids.toList(), referencedTables = referencedTables.toTypedArray())

    /**
     * Use [kotlinx.coroutines.flow.distinctUntilChanged] to prevent
     * duplicate emissions when unrelated entities are changed
     * Re-emits values when any of the [referencedTables] change.
     * ReferencedTables includes [tableName] already.
     */
    @SuppressLint("RestrictedApi")
    fun get(ids: List<I>, vararg referencedTables: String): Flow<List<T>> =
        CoroutinesRoom.createFlow(db, true, tableNames = arrayOf(tableName) + referencedTables) {
            getBlocking(buildSqlIdQuery(ids)) ?: emptyList()
        }

    /**
     * Use [kotlinx.coroutines.flow.distinctUntilChanged] to prevent
     * duplicate emissions when unrelated entities are changed.
     * Re-emits values when any of the [referencedTables] change
     */
    @SuppressLint("RestrictedApi")
    fun getAll(vararg referencedTables: String): Flow<List<T>> =
        CoroutinesRoom.createFlow(db, false, arrayOf(tableName) + referencedTables) {
            getBlocking(SimpleSQLiteQuery("SELECT * FROM `$tableName`;")) ?: emptyList()
        }

    @RawQuery
    @Transaction
    protected abstract suspend fun delete(query: SupportSQLiteQuery): Int

    @RawQuery
    protected abstract suspend fun getSync(query: SupportSQLiteQuery): List<T>?

    @RawQuery
    internal abstract fun getBlocking(query: SupportSQLiteQuery): List<T>?

    private fun buildSqlIdList(ids: List<I>): String = buildString {
        ids.forEachIndexed { i, id ->
            if (i != 0) {
                append(",")
            }
            append("'$id'")
        }
    }

    private fun buildSqlIdQuery(ids: List<I>): SimpleSQLiteQuery {
        val idsQ = buildSqlIdList(ids)
        return SimpleSQLiteQuery("SELECT * FROM $tableName WHERE `id` IN ($idsQ);")
    }
}
//
// private fun UUID.toByteBlob(): ByteArray {
//     val bb: ByteBuffer = ByteBuffer.wrap(ByteArray(16))
//     bb.putLong(mostSignificantBits)
//     bb.putLong(leastSignificantBits)
//     return bb.array()
// }
//
// private fun ByteArray.toUUID(): UUID {
//     val bb: ByteBuffer = ByteBuffer.wrap(this)
//     val mostSignificantBits = bb.long
//     val leastSignificantBits = bb.long
//     return UUID(mostSignificantBits, leastSignificantBits)
// }
