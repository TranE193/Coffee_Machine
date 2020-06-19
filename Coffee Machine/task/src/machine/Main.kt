package machine

fun main() {
    val coffeeMachine = CoffeeMachine()

    while (coffeeMachine.currentState != CoffeeMachineState.OFF) {
        print("Write action (buy, fill, take, remaining, exit): ")
        val action = getInputValue()
        println()
        when (action) {
            "buy" -> {
                coffeeMachine.currentState = CoffeeMachineState.BUY
                print("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu: ")
                val input = getInputValue()
                if (input.toIntOrNull() != null && input.toInt() in 1..3) {
                    val type = CoffeeType.values()[input.toInt() - 1]
                    coffeeMachine.buy(type)
                }
            }
            "fill" -> {
                coffeeMachine.currentState = CoffeeMachineState.FILL
                coffeeMachine.fill(getFillComponents())
            }
            "take" -> coffeeMachine.take()
            "remaining" -> coffeeMachine.remain()
            "exit" -> coffeeMachine.currentState = CoffeeMachineState.OFF
        }
        println()
    }
}

fun getInputValue(): String = readLine()!!

fun getFillComponents(): Components {
    val components = Components()
    print("Write how many ml of water do you want to add: ")
    components.water += getInputValue().toInt()
    print("Write how many ml of milk do you want to add: ")
    components.milk += getInputValue().toInt()
    print("Write how many grams of coffee beans do you want to add: ")
    components.coffeeBeans += getInputValue().toInt()
    print("Write how many disposable cups of coffee do you want to add: ")
    components.cups += getInputValue().toInt()
    return components
}

class CoffeeMachine {
    var currentState = CoffeeMachineState.READY
    private var components = Components(400, 540, 120, 9)
    private var money = 550

    fun buy(coffeeType: CoffeeType) = when {
        components.water < coffeeType.components.water -> println("Sorry, not enough water!")
        components.milk < coffeeType.components.milk -> println("Sorry, not enough milk!")
        components.coffeeBeans < coffeeType.components.coffeeBeans -> println("Sorry, not enough coffee beans!")
        components.cups < coffeeType.components.cups -> println("Sorry, not enough disposable cups!")
        else -> {
            println("I have enough resources, making you a coffee!")
            components -= coffeeType.components
            money += coffeeType.cost
        }
    }

    fun fill(components: Components) {
        this.components += components
    }

    fun take() {
        println("I gave you \$${money}")
        money = 0
    }

    fun remain() {
        println("${components.water} of water")
        println("${components.milk} of milk")
        println("${components.coffeeBeans} of coffee beans")
        println("${components.cups} of disposable cups")
        println("$money of money")
    }
}

enum class CoffeeType(val components: Components, val cost: Int) {
    ESPRESSO(Components(250, 0, 16, 1), 4),
    LATTE(Components(350, 75, 20, 1), 7),
    CAPPUCCINO(Components(200, 100, 12, 1), 6)
}

class Components(var water: Int = 0, var milk: Int = 0, var coffeeBeans: Int = 0, var cups: Int = 0) {
    operator fun plus(increment: Components) =
            Components(this.water + increment.water,
                    this.milk + increment.milk,
                    this.coffeeBeans + increment.coffeeBeans,
                    this.cups + increment.cups)

    operator fun minus(decrement: Components) =
            Components(this.water - decrement.water,
                    this.milk - decrement.milk,
                    this.coffeeBeans - decrement.coffeeBeans,
                    this.cups - decrement.cups)
}

enum class CoffeeMachineState {
    READY,
    BUY,
    FILL,
    OFF
}