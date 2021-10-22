@file:Suppress("unused")

package com.nek12.androidutils.databinding.recyclerview

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding

internal const val CAST_MESSAGE = """
    Could not bind your Item because you provided wrong layout|type argument pair
"""

internal const val TAG = "GenericAdapter"

/**
 * A lambda that is going to be called when the ViewHolder is rebound.
 * You can put here all the logic that you usually put into [Item.bind]
 */
typealias Binder<T, VB> = (BindPayload<T, VB>) -> Unit

/**
 * This is the heart of the library. Use [Item]s to populate your generic recyclerview.
 * Items serve the purpose of viewHolders, adapter inner logic and encapsulate your business
 * logic. If you want a simple list with just one item type and zero custom binding logic, save
 * yourself some time and use [SimpleAdapter] and [GenericItem].
 *
 * If you provide a wrong [layout] <-> [VB] argument pair or do not implement the [data] variable
 * in your xml, your app will crash at runtime. This is the downside of using this library,
 * probably.
 *
 * If you have a single view type but want to have custom binding logic, use [SingleTypeAdapter]
 * and [GenericItem] together. If you have more than one type, however, you have to implement a
 * sealed class and use [GenericAdapter].
 *
 * ### [data]
 * Whatever data that you want to be passed to your xml. **Remember that your XML
 * databinding parameter must have a variable named exactly "data" and it must have the type of
 * your [T] type parameter!** For more info see [BaseHolder]
 *
 * It can be whatever class you choose, though I recommend mapping your
 * entities explicitly to data classes to allow saving dynamic state and even two-way binding,
 * and to skip on implementing [equals] and [hashCode] yourself.
 *
 * You can also make [Item] a dataclass if you want. This way your [data] field will be compared,
 * which is convenient.
 *
 * ### [id]
 * A unique (hopefully) identifier for the Item that you are trying to display. Your
 * recyclerview performance depends on how you override this parameter.
 *
 * You can use different [T] and [VB] types to implement multiple item type lists as follows:
 * Example:
 * ```
 * sealed class MainMenuItem<T, VB : ViewDataBinding> : Item<T, VB>() {
 *    data class Entry(
 *       override val data: MenuEntryEntity,
 *    ) : MainMenuItem<MenuEntryEntity, ItemMainMenuEntryBinding>() {
 *        override val layout: Int get() = R.layout.item_main_menu
 *        override val id: Long get() = data.id
 *        //data field is used in equals() and hashcode, no need to override
 *
 *        override fun bind(binding: ItemMainMenuEntryBinding, bindingPos: Int) {
 *            //use data, layout and binding together
 *        }
 *    }
 *
 *    data class Header(
 *       override val data: Unit, //don't forget to use something else in equals()
 *    ) : MainMenuItem<Unit, ItemMainMenuHeaderBinding>() { /* blah */  }
 * }
 * ```
 */
abstract class Item<T, VB : ViewDataBinding> {
    abstract val data: T
    abstract val id: Long
    abstract override fun equals(other: Any?): Boolean
    abstract override fun hashCode(): Int

    @get:LayoutRes
    abstract val layout: Int

    open fun bind(binding: VB, bindingPos: Int) {}

    internal fun tryBind(binding: ViewDataBinding, bindingPos: Int) = bind(
        requireNotNull(cast(binding)) { CAST_MESSAGE },
        bindingPos
    )

    @Suppress("UNCHECKED_CAST")
    private fun cast(viewDataBinding: ViewDataBinding): VB? = viewDataBinding as? VB
}

/**
 * This is the item that has no data, just a layout, like a header, a separator or a footer.
 * The performance of this Item is degraded down to practically zero difference from not using
 * any optimizaations. [equals] always returns false, and [layout] is used instead during [id]
 * comparison, though these items are usually incredibly lightweight.
 * @param alwaysRebound Whether this item should be rebound on **every** diffing pass. If false,
 * the item is **never** rebound.
 */
data class BlankItem<VB : ViewDataBinding>(
    override val layout: Int,
    val alwaysRebound: Boolean = false
) : Item<Unit, VB>() {
    override val data: Unit get() = Unit
    override val id get() = layout.toLong()
    override fun equals(other: Any?): Boolean = !alwaysRebound
    override fun hashCode(): Int = 31 * layout + 29 * if (alwaysRebound) 1 else 0
}

/**
 * An item that does not require for you to override the [Item] class. You can use this
 * class directly instead of Item, though that will cause problems with complicated lists. It
 * also breaks encapsulation somewhat and costs more resources.
 * Most useful for use with [SimpleAdapter] and [SingleTypeAdapter]
 */
data class GenericItem<T, VB : ViewDataBinding>(
    override val data: T,
    override val id: Long,
    override val layout: Int,
    val binder: Binder<T, VB>? = null,
) : Item<T, VB>() {
    override fun bind(binding: VB, bindingPos: Int) {
        binder?.invoke(BindPayload(this, binding, bindingPos))
    }
}

/**
 * You get this payload if you're using [Binder].
 * Here's the data you might need when binding.
 */
data class BindPayload<T, VB : ViewDataBinding>(
    val item: Item<T, VB>,
    val binding: VB,
    val bindingPos: Int,
) {
    val data: T get() = item.data
}
