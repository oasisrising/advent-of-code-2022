package advent.colors

// from https://github.com/kotlin-hands-on/advent-of-code-kotlin-template


/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println(color: Int) = println(colors[color % colors.size] + this + reset)
fun Any?.print(color: Int): String = colors[color % colors.size] + this + reset

const val dark_red = "\u001b[31m"
const val dark_green = "\u001b[32m"
const val dark_yellow = "\u001b[33m"
const val dark_blue = "\u001b[34m"
const val dark_magenta = "\u001b[35m"
const val dark_cyan = "\u001b[36m"
const val bright_red = "\u001b[91m"
const val bright_green = "\u001b[92m"
const val bright_yellow = "\u001b[93m"
const val bright_blue = "\u001b[94m"
const val bright_magenta = "\u001b[95m"
const val bright_cyan = "\u001b[96m"
const val reset = "\u001b[0m"

val colors = arrayOf(
    dark_magenta,
    bright_magenta,
    dark_red,
    bright_red,
    dark_yellow,
    bright_yellow,
    dark_green,
    bright_green,
    dark_blue,
    bright_blue,
    dark_cyan,
    bright_cyan
)

