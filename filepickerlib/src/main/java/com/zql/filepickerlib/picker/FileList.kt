package com.zql.filepickerlib.picker

import android.util.Log

class FileList<E>: ArrayList<E> {
    constructor(initialCapacity: Int) : super(initialCapacity)
    constructor() : super()
    constructor(c: FileList< E>) : super(c)

    override fun add(element: E): Boolean {
//        Log.d("lzqtest", "MyList.add: 5 ${element.toString()}")
        return super.add(element)
    }

    override fun add(index: Int, element: E) {
//        Log.d("lzqtest", "MyList.add: 12 ${element.toString()}")
        super.add(index, element)
    }

    override fun addAll(elements: Collection<E>): Boolean {
        return super.addAll(elements)
    }

    override fun addAll(index: Int, elements: Collection<E>): Boolean {
        return super.addAll(index, elements)
    }

    override fun clear() {
        super.clear()
    }

    override fun iterator(): MutableIterator<E> {
        return super.iterator()
    }

    override fun remove(element: E): Boolean {
//        Log.d("lzqtest", "MyList.remove: 33 $element")
        return super.remove(element)
    }

    override fun removeAll(elements: Collection<E>): Boolean {
        return super.removeAll(elements)
    }
}