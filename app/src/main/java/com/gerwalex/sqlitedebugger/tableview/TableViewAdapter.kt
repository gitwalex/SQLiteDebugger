/*
 * MIT License
 *
 * Copyright (c) 2021 Evren Coşkun
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.gerwalex.sqlitedebugger.tableview

import android.database.Cursor
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.evrencoskun.tableview.adapter.AbstractTableAdapter
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.evrencoskun.tableview.sort.SortState
import com.gerwalex.sqlitedebugger.R
import com.gerwalex.sqlitedebugger.tableview.holder.CellViewHolder
import com.gerwalex.sqlitedebugger.tableview.holder.ColumnHeaderViewHolder
import com.gerwalex.sqlitedebugger.tableview.holder.RowHeaderViewHolder
import com.gerwalex.sqlitedebugger.tableview.model.Cell
import com.gerwalex.sqlitedebugger.tableview.model.ColumnHeader
import com.gerwalex.sqlitedebugger.tableview.model.RowHeader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by evrencoskun on 11/06/2017.
 *
 *
 * This is a sample of custom TableView Adapter.
 */
class TableViewAdapter : AbstractTableAdapter<ColumnHeader, RowHeader, Cell>() {

    override fun getCellItemViewType(column: Int): Int {
        // The unique ID for this type of cell item
        // If you have different items for Cell View by X (Column) position,
        // then you should fill this method to be able create different
        // type of CellViewHolder on "onCreateCellViewHolder"
        // Default view type
        return 0
    }

    override fun getColumnHeaderItemViewType(position: Int): Int {
        // The unique ID for this type of column header item
        // If you have different items for Cell View by X (Column) position,
        // then you should fill this method to be able create different
        // type of CellViewHolder on "onCreateCellViewHolder"
        return 0
    }

    override fun getRowHeaderItemViewType(position: Int): Int {
        // The unique ID for this type of row header item
        // If you have different items for Row Header View by Y (Row) position,
        // then you should fill this method to be able create different
        // type of RowHeaderViewHolder on "onCreateRowHeaderViewHolder"
        return 0
    }

    /**
     * That is where you set Cell View Model data to your custom Cell ViewHolder. This method is
     * Called by Cell RecyclerView of the TableView to display the data at the specified position.
     * This method gives you everything you need about a cell item.
     *
     * @param holder         : This is one of your cell ViewHolders that was created on
     * ```onCreateCellViewHolder``` method. In this example we have created
     * "CellViewHolder" holder.
     * @param cellItemModel  : This is the cell view model located on this X and Y position. In this
     * example, the model class is "Cell".
     * @param columnPosition : This is the X (Column) position of the cell item.
     * @param rowPosition    : This is the Y (Row) position of the cell item.
     * @see .onCreateCellViewHolder
     */
    override fun onBindCellViewHolder(
        holder: AbstractViewHolder, cellItemModel: Cell?,
        columnPosition: Int, rowPosition: Int,
    ) {
        val viewHolder = holder as CellViewHolder
        viewHolder.setCell(cellItemModel)
    }

    /**
     * That is where you set Column Header View Model data to your custom Column Header ViewHolder.
     * This method is Called by ColumnHeader RecyclerView of the TableView to display the data at
     * the specified position. This method gives you everything you need about a column header
     * item.
     *
     * @param holder                : This is one of your column header ViewHolders that was created
     * on ```onCreateColumnHeaderViewHolder``` method. In this example
     * we have created "ColumnHeaderViewHolder" holder.
     * @param columnHeaderItemModel : This is the column header view model located on this X
     * position. In this example, the model class is "ColumnHeader".
     * @param columnPosition        : This is the X (Column) position of the column header item.
     * @see .onCreateColumnHeaderViewHolder
     */
    override fun onBindColumnHeaderViewHolder(
        holder: AbstractViewHolder,
        columnHeaderItemModel: ColumnHeader?, columnPosition: Int,
    ) {
        // Get the holder to update cell item text
        val columnHeaderViewHolder = holder as ColumnHeaderViewHolder
        columnHeaderViewHolder.setColumnHeader(columnHeaderItemModel)
    }

    /**
     * That is where you set Row Header View Model data to your custom Row Header ViewHolder. This
     * method is Called by RowHeader RecyclerView of the TableView to display the data at the
     * specified position. This method gives you everything you need about a row header item.
     *
     * @param holder             : This is one of your row header ViewHolders that was created on
     * ```onCreateRowHeaderViewHolder``` method. In this example we have
     * created "RowHeaderViewHolder" holder.
     * @param rowHeaderItemModel : This is the row header view model located on this Y position. In
     * this example, the model class is "RowHeader".
     * @param rowPosition        : This is the Y (row) position of the row header item.
     * @see .onCreateRowHeaderViewHolder
     */
    override fun onBindRowHeaderViewHolder(
        holder: AbstractViewHolder, rowHeaderItemModel: RowHeader?,
        rowPosition: Int,
    ) {
        // Get the holder to update row header item text
        val rowHeaderViewHolder = holder as RowHeaderViewHolder
        rowHeaderViewHolder.row_header_textview.text = rowHeaderItemModel!!.data.toString()
    }

    /**
     * This is where you create your custom Cell ViewHolder. This method is called when Cell
     * RecyclerView of the TableView needs a new RecyclerView.ViewHolder of the given type to
     * represent an item.
     *
     * @param viewType : This value comes from "getCellItemViewType" method to support different
     * type of viewHolder as a Cell item.
     * @see .getCellItemViewType
     */
    override fun onCreateCellViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        // For cells that display a text
        val layout = inflater.inflate(R.layout.table_view_cell_layout, parent, false)
        // Create a Cell ViewHolder
        return CellViewHolder(layout)
    }

    /**
     * This is where you create your custom Column Header ViewHolder. This method is called when
     * Column Header RecyclerView of the TableView needs a new RecyclerView.ViewHolder of the given
     * type to represent an item.
     *
     * @param viewType : This value comes from "getColumnHeaderItemViewType" method to support
     * different type of viewHolder as a Column Header item.
     * @see .getColumnHeaderItemViewType
     */
    override fun onCreateColumnHeaderViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        // TODO: check
        //Log.e(LOG_TAG, " onCreateColumnHeaderViewHolder has been called");
        // Get Column Header xml Layout
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.table_view_column_header_layout, parent, false)
        // Create a ColumnHeader ViewHolder
        return ColumnHeaderViewHolder(layout, tableView)
    }

    suspend fun swap(c: Cursor) {
        val colList: MutableList<ColumnHeader> = ArrayList()
        val rowList: MutableList<RowHeader> = ArrayList()
        val cellLists: MutableList<List<Cell>> = ArrayList()
        withContext(Dispatchers.IO) {
            for (index in 0 until c.columnCount) {
                colList.add(ColumnHeader(index.toString(), c.getColumnName(index)))
            }
            if (c.moveToFirst()) {
                do {
                    rowList.add(RowHeader(c.getString(0), c.getString(0)))
                    val cellList: MutableList<Cell> = ArrayList()
                    for (index in 0 until c.columnCount) {
                        cellList.add(Cell(index.toString(), c.getString(index)))
                    }
                    cellLists.add(cellList)
                } while (c.moveToNext())
            }
        }
        withContext(Dispatchers.Main) {
            setAllItems(colList, rowList, cellLists)
        }
    }

    override fun onCreateCornerView(parent: ViewGroup): View {
        // Get Corner xml layout
        val corner = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.table_view_corner_layout, parent, false)
        corner.setOnClickListener { view: View? ->
            val sortState = this@TableViewAdapter
                .tableView
                .rowHeaderSortingStatus
            if (sortState != SortState.ASCENDING) {
                Log.d("TableViewAdapter", "Order Ascending")
                this@TableViewAdapter
                    .tableView
                    .sortRowHeader(SortState.ASCENDING)
            } else {
                Log.d("TableViewAdapter", "Order Descending")
                this@TableViewAdapter
                    .tableView
                    .sortRowHeader(SortState.DESCENDING)
            }
        }
        return corner
    }

    /**
     * This is where you create your custom Row Header ViewHolder. This method is called when
     * Row Header RecyclerView of the TableView needs a new RecyclerView.ViewHolder of the given
     * type to represent an item.
     *
     * @param viewType : This value comes from "getRowHeaderItemViewType" method to support
     * different type of viewHolder as a row Header item.
     * @see .getRowHeaderItemViewType
     */
    override fun onCreateRowHeaderViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        // Get Row Header xml Layout
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.table_view_row_header_layout, parent, false)
        // Create a Row Header ViewHolder
        return RowHeaderViewHolder(layout)
    }
}