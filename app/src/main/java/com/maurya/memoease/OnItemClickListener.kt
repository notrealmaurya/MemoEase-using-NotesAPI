package com.maurya.memoease


interface OnItemClickListener {
    fun onItemClickListener(position: Int,isComplete:Boolean)
    fun onItemCheckedChange(position: Int, isChecked: Boolean, isCompleteList: Boolean)

}