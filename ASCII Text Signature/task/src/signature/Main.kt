package signature

import java.io.File
import java.util.*
import kotlin.collections.ArrayList

fun main() {
    val scan = Scanner(System.`in`)
    val romanFontFile: MutableList<String> = ArrayList()
    File("/Users/mac/fonts/roman.txt").forEachLine { romanFontFile.add(it) }
    val mediumFontFile: MutableList<String> = ArrayList()
    File("/Users/mac/fonts/medium.txt").forEachLine { mediumFontFile.add(it) }

    val romanSymbols: MutableList<Symbol> = initializeFont(romanFontFile)
    val mediumSymbols: MutableList<Symbol> = initializeFont(mediumFontFile)

    print("Enter name and surname:")
    val name = scan.nextLine()

    print("Enter person's status:")
    val status = scan.nextLine()

    var nameInRoman: MutableList<String> = MutableList(10) { "" }
    name.forEach { char ->
        romanSymbols.find { it.value == char }?.lines?.forEachIndexed { i: Int, l: String ->
            nameInRoman[i] += l
        }
    }
    var statusInMedium: MutableList<String> = MutableList(3) { "" }
    status.forEach { char ->
        mediumSymbols.find { it.value == char }?.lines?.forEachIndexed { i: Int, l: String ->
            statusInMedium[i] += l
        }
    }

    val nameLength = nameInRoman.first().length
    val statusLength = statusInMedium.first().length
    if (nameLength > statusLength) {
        val leftPadding = (nameLength - statusLength) / 2
        statusInMedium = statusInMedium.map { it.padStart(leftPadding + statusLength).padEnd(nameLength) }.toMutableList()
    } else {
        val leftPadding = (statusLength - nameLength) / 2
        nameInRoman = nameInRoman.map { it.padStart(leftPadding + nameLength).padEnd(statusLength) }.toMutableList()
    }

    println("8".repeat(nameInRoman.first().length + 8))
    nameInRoman.forEach { println("88  $it  88") }
    statusInMedium.forEach { println("88  $it  88") }
    println("8".repeat(nameInRoman.first().length + 8))
}

fun initializeFont(fontFile: MutableList<String>): MutableList<Symbol> {
    val symbols: MutableList<Symbol> = ArrayList()
    val (fontHeight, fontLength) = fontFile.first().split(' ').map { it.toInt() }

    for (i in 0 until fontLength) {
        val value = fontFile[i * (fontHeight + 1) + 1].first()
        val width = fontFile[i * (fontHeight + 1) + 1].split(' ')[1].toInt()
        val lines: MutableList<String> = ArrayList(fontHeight)

        for (j in 1..fontHeight) {
            lines.add(fontFile[i * (fontHeight + 1) + j + 1])
        }
        symbols.add(Symbol(value, lines, width))
    }
    val space = Symbol(' ', MutableList(fontHeight) { " ".repeat(symbols.first().width) }, symbols.first().width)
    symbols.add(space)
    return symbols
}

class Symbol(val value: Char, val lines: MutableList<String>, val width: Int)
