@file:Suppress("unused")

package com.nek12.androidutils.room

/**
 * A repository class that uses the generated [RoomDao] from the library.
 * Example
 * ```
 * class EntryRepo(
 *     private val dao: EntryDao
 * ): RoomRepo<Entry>(dao) {
 *     fun getAll() = dao.getAll()
 * }
 *
 * ```
 *
 * @see RoomDao
 * @see RoomEntity
 */
open class RoomRepo<T : RoomEntity>(private val dao: RoomDao<T>) {
    open suspend fun add(entity: T): Long = dao.add(entity)
    open suspend fun add(vararg entities: T) = dao.add(*entities)
    open suspend fun add(entities: List<T>) = dao.add(entities)
    open suspend fun update(entity: T) = dao.update(entity)
    open suspend fun update(entities: List<T>) = dao.update(entities)
    open suspend fun update(vararg entities: T) = dao.update(*entities)
    open suspend fun delete(entity: T) = dao.delete(entity)
    open suspend fun delete(vararg entities: T) = dao.delete(*entities)
    open suspend fun delete(entities: List<T>) = dao.delete(entities)
    open suspend fun deleteById(id: Long) = dao.deleteById(id)
    open suspend fun deleteById(ids: List<Long>) = dao.deleteById(ids)
    open suspend fun deleteById(vararg ids: Long) = dao.deleteById(*ids)
    open suspend fun deleteAll() = dao.deleteAll()
    open suspend fun getSync(id: Long): T? = dao.getSync(id)
    open suspend fun getSync(vararg ids: Long): List<T> = dao.getSync(*ids)
    open suspend fun getSync(ids: List<Long>): List<T> = dao.getSync(ids)
    open suspend fun getAllSync(): List<T> = dao.getAllSync()
}
