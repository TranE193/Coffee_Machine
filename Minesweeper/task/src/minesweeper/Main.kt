package minesweeper

import java.util.*

fun main() {
    val scanner = Scanner(System.`in`)
    val width = 9
    val height = 9
    print("How many mines do you want on the field? ")
    val minesCount = scanner.nextInt()
    val battlefield = Battlefield(width, height, minesCount)
    battlefield.drawBattlefield()


    print("Set/unset mine marks or claim a cell as free:")
    val firstCell = Cell(scanner.nextInt() - 1, scanner.nextInt() - 1)
    val operation = scanner.next()

    battlefield.initializeBattlefield(firstCell)
    battlefield.explore(firstCell)
    battlefield.drawBattlefield()

    while (!battlefield.isGameEnded()) {
        print("Set/unset mine marks or claim a cell as free:")
        val exploringCell = Cell(scanner.nextInt() - 1, scanner.nextInt() - 1)
        val operation = scanner.next()

        if (operation == "free") {
            if (battlefield.cells.find { c -> c == exploringCell }!!.isMine) {
                println("You stepped on a mine and failed!")
                break
            } else {
                battlefield.explore(exploringCell)
            }
        }
        if (operation == "mine") {
            battlefield.cells.find { c -> c == exploringCell }!!.changeSelection()
        }
        battlefield.drawBattlefield()
    }
    if (battlefield.isGameEnded()) {
        println("Congratulations! You found all the mines!")
    }

}

open class Cell(val width: Int,
                val height: Int,
                var isExplored: Boolean = false,
                var isMine: Boolean = false,
                var isSelected: Boolean = false,
                var value: Int = 0) {
    override fun equals(other: Any?): Boolean =
            other is Cell && width == other.width && this.height == other.height

    override fun hashCode(): Int {
        var result = width
        result = 321 * result + height
        return result
    }

    fun changeSelection() {
        if (!isExplored)
            isSelected = !isSelected
        else println("This cell already explored!")
    }

}

class Battlefield(private val width: Int, private val height: Int, private val minesCount: Int) {
    val cells = mutableListOf<Cell>()

    fun drawBattlefield() {
        println(" │${(1..width).joinToString("")}│")
        println("-│${"-".repeat(width)}│")
        for (i in 0 until height) {
            print("${i + 1}|")
            for (j in 0 until width) {
                if (cells.size == 0) {
                    print('.')
                } else {
                    val cell = cells.find { c -> c == Cell(j, i) }!!

                    when {
                        cell.isSelected -> print('*')
                        cell.isExplored -> if (cell.value == 0) print('/') else print(cell.value.toString())
                        cell.isMine -> print('.')
                        else -> print('.')
                    }
                }

            }
            println('|')
        }
        println("-│${"-".repeat(width)}│")
    }

    private fun nearlyMines(cell: Cell): Int {
        var nearlyMinesCount = 0
        cells.filter { c -> c.isMine }.forEach { if (cell.width - it.width in -1..1 && cell.height - it.height in -1..1) nearlyMinesCount++ }
        return nearlyMinesCount
    }

    fun initializeBattlefield(firstCell: Cell) {
        for (i in 0 until width) {
            for (j in 0 until height) {
                cells.add(Cell(i, j))
            }
        }

        val rnd = Random()
        while (cells.count { c -> c.isMine } != minesCount) {
            val mineIndex = rnd.nextInt(cells.size)
            if (!cells[mineIndex].isMine && cells[mineIndex] != firstCell)
                cells[mineIndex].isMine = true
        }

        cells.forEach { c ->
            if (!c.isMine) {
                c.value = nearlyMines(c)
            }
        }
    }

    fun explore(cell: Cell) {
        val selectedCell = cells[cells.indexOf(cell)]
        selectedCell.isExplored = true
        if (selectedCell.value == 0) {
            openEmptyCells(selectedCell)
        }
    }

    private fun openEmptyCells(cell: Cell) {
        cells.forEach {
            if (cell.width - it.width in -1..1 && cell.height - it.height in -1..1 && !it.isExplored && !it.isMine) {
                it.isExplored = true;
                if (it.value == 0)
                    it.isSelected = false
                openEmptyCells(it)
            }
        }
    }

    fun isGameEnded(): Boolean {
        val selectedCells = cells.filter { c -> c.isSelected }
        return (selectedCells.all { c -> c.isMine } && selectedCells.count() == minesCount)
                || cells.filter { c -> !c.isExplored }.all { c -> c.isMine }
    }
}
