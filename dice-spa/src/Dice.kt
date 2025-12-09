import kotlin.random.Random

val sidesOptions = setOf(4, 6, 8, 10, 12, 20)

fun dieRoll(sides: Int): Int {
    require(sides in sidesOptions) { "Invalid number of die sides: $sides" }
    return Random.nextInt(sides) + 1
}

fun diceRoll(numDice: Int, sides: Int): List<Int> {
    require(numDice > 0) { "Invalid number of dice: $numDice" }
    return List(numDice) { dieRoll(sides) }
}
