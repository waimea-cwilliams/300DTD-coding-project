/**
 * ------------------------------------------------------------------------
 * Win the NBA Championship
 * Level 3 programming project
 *
 * by Coen Williams
 *
 * This project is a road to an NBA championship.
 * The user will have to play multiple different teams in order to win a championship.
 * Follow the task list so you can complete the game.
 * Good luck!!

 * ------------------------------------------------------------------------
 */


import com.formdev.flatlaf.FlatDarkLaf
import java.awt.*
import java.awt.event.*
import javax.swing.*

//=============================================================================================

/**
 * Team class representing a match with possible directions to move.
 */
class Match(val name: String, val x: Int, val y: Int) {
    var north: Match? = null
    var east: Match? = null
    var south: Match? = null
    var west: Match? = null
}

/**
 * GUI class that handles the UI and responds to key events.
 */
class GUI : JFrame(), KeyListener {

    private val matchs = arrayOf(
        Match("Start", 40, 180),
        Match("Timberwolves", 170, 180),
        Match("Boston", 300, 180),
        Match("Hawks", 430, 180),
        Match("Pelicans", 560, 180),
        Match("Nets", 40, 310),
        Match("Knicks", 170, 310),
        Match("Magic", 300, 310),
        Match("Cavaliers", 430, 310),
        Match("Pacers", 560, 310),
        Match("Lakers", 40, 440),
        Match("Mavericks", 170, 440),
        Match("Heat", 300, 440),
        Match("Bulls", 430, 440),
        Match("Clippers", 560, 440),
        Match("Grizzlies", 40, 570),
        Match("76ers", 170, 570),
        Match("Hornets", 300, 570),
        Match("Bucks", 430, 570),
        Match("Rockets", 560, 570),
        Match("Warriors", 40, 700),
        Match("Nuggets", 170, 700),
        Match("Pistons", 300, 700),
        Match("Blazers", 430, 700),
        Match("NBA Finals", 560, 700)
    )

    private var playBoston = false
    private var beatLakers = false
    private var playClippers = false
    private var beatWarriors = false

    private var currentMatch = matchs[0]
    private var previousMatch: Match? = null

    private lateinit var instructionsLabel: JLabel
    private lateinit var instructionsList: JList<String>
    private lateinit var instructionsListModel: DefaultListModel<String>

    private lateinit var taskBarLabel: JLabel
    private lateinit var taskList: JList<String>
    private lateinit var taskListModel: DefaultListModel<String>

    private lateinit var userLabel: JLabel


    init {
        setupWindow()
        buildUI()
        setupTable()

        isVisible = true
        updateUserPosition()

        addKeyListener(this)
        isFocusable = true
    }


    /**
     * Configure the main window.
     */
    private fun setupWindow() {
        title = "Win an NBA Championship"
        contentPane.preferredSize = Dimension(700, 900)
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        isResizable = false
        layout = null
        pack()
    }

    /**
     * Populate the UI with components.
     */
    private fun buildUI() {
        val baseFont = Font(Font.SANS_SERIF, Font.PLAIN, 14)

        instructionsLabel = JLabel("INSTRUCTIONS:", SwingConstants.CENTER)
        instructionsLabel.bounds = Rectangle(70,35,200,20)
        instructionsLabel.font = Font(Font.SANS_SERIF, Font.BOLD, 20)
        add(instructionsLabel)

        instructionsListModel = DefaultListModel<String>().apply {
            addElement("* Use Arrow Keys to Navigate")
            addElement("* Complete Task bar to Win")
            addElement("* Your character is the")
            addElement("  basketball Icon")
            addElement("* Have Fun!!")

        }
        instructionsList = JList(instructionsListModel)
        instructionsList.bounds = Rectangle(50, 60, 250, 100)
        instructionsList.font = baseFont
        add(instructionsList)

        taskBarLabel = JLabel("TASK BAR:", SwingConstants.CENTER)
        taskBarLabel.bounds = Rectangle(420,35,200,20)
        taskBarLabel.font = Font(Font.SANS_SERIF, Font.BOLD, 20)
        add(taskBarLabel)

        taskListModel = DefaultListModel<String>().apply {
            addElement("* Play the Boston Celtics")
            addElement("* Beat the Los Angeles Lakers")
            addElement("* Play the Los Angeles Clippers")
            addElement("* Beat the Golden State Warriors")
            addElement("* Make it to the NBA Finals")
        }

        taskList = JList(taskListModel)
        taskList.bounds = Rectangle(400, 60, 250, 100)
        taskList.font = baseFont
        add(taskList)

        userLabel = JLabel("\uD83C\uDFC0", SwingConstants.CENTER)
        userLabel.font = Font(Font.SANS_SERIF, Font.PLAIN, 30)
        add(userLabel)

        for (match in matchs) {
            val label = JLabel(match.name, SwingConstants.CENTER)
            label.bounds = Rectangle(match.x, match.y, 100, 50)
            label.font = baseFont
            add(label)
        }
    }

    /**
     * Set up connections between the teams.
     */
    private fun setupTable() {
        val tableSize = 5

        for (i in matchs.indices) {
            val row = i / tableSize
            val col = i % tableSize

            if (row < tableSize - 1) matchs[i].south = matchs[i + tableSize]
            if (row > 0) matchs[i].north = matchs[i - tableSize]
            if (col > 0) matchs[i].west = matchs[i - 1]
            if (col < tableSize - 1) matchs[i].east = matchs[i + 1]
        }
    }

    override fun keyPressed(e: KeyEvent) {
        when (e.keyCode) {
            KeyEvent.VK_LEFT -> moveLeft()
            KeyEvent.VK_RIGHT -> moveRight()
            KeyEvent.VK_UP -> moveUp()
            KeyEvent.VK_DOWN -> moveDown()
        }
    }

    private fun moveLeft() {
        previousMatch = currentMatch
        currentMatch.west?.let {
            currentMatch = it
            updateUserPosition()
        }
    }

    private fun moveRight() {
        previousMatch = currentMatch
        currentMatch.east?.let {
            currentMatch = it
            updateUserPosition()
        }
    }

    private fun moveUp() {
        previousMatch = currentMatch
        currentMatch.north?.let {
            currentMatch = it
            updateUserPosition()
        }
    }

    private fun moveDown() {
        previousMatch = currentMatch
        currentMatch.south?.let {
            currentMatch = it
            updateUserPosition()
        }
    }

    /**
     * Update the player's position and check completed tasks.
     */
    private fun updateUserPosition() {
        userLabel.bounds = Rectangle(currentMatch.x, currentMatch.y, 100, 100)

        when (currentMatch.name) {
            "Boston" -> if (!playBoston) {
                playBoston = true
                showTaskCompletionMessage("You beat the Celtics!!", 0)
            }
            "Lakers" -> if (!beatLakers) {
                beatLakers = true
                showTaskCompletionMessage("You beat the Lakers", 1)
            }
            "Clippers" -> if (!playClippers) {
                playClippers = true
                showTaskCompletionMessage("You beat the Clippers", 2)
            }
            "Warriors" -> if (!beatWarriors) {
                beatWarriors = true
                showTaskCompletionMessage("You beat the Warriors", 3)
            }
            "NBA Finals" -> if (playBoston && beatLakers && playClippers && beatWarriors) {
                showFinalMessage()
            } else {
                rejectFinalAccess()
            }
        }
    }

    /**
     * Show a task completion message.
     */
    private fun showTaskCompletionMessage(message: String, taskIndex: Int) {
        JOptionPane.showMessageDialog(this, message, "Won", JOptionPane.INFORMATION_MESSAGE)
        taskListModel.set(taskIndex, "✓ ${taskListModel.get(taskIndex)}")
    }

    /**
     * Show the final message when all tasks are completed.
     */
    private fun showFinalMessage() {
        JOptionPane.showMessageDialog(this, "You're at the Finals!! Awesome sauce, you just won an NBA Championship", "Game Won", JOptionPane.INFORMATION_MESSAGE)
        taskListModel.set(4, "✓ Make it to the NBA Finals")
        newGame()
    }

    /**
     * Reject access to the NBA Finals if tasks aren't completed.
     */
    private fun rejectFinalAccess() {
        currentMatch = previousMatch ?: currentMatch
        JOptionPane.showMessageDialog(this, "Make sure you complete the tasks shown down below the map!!", "Cannot Finish", JOptionPane.WARNING_MESSAGE)
        updateUserPosition()
    }

    /**
     * Reset everything for a new game.
     */
    private fun newGame() {
        currentMatch = matchs[0]
        playBoston = false
        beatLakers = false
        playClippers = false
        beatWarriors = false

        taskListModel.set(0, "Play the Boston Celtics")
        taskListModel.set(1, "Beat the Los Angeles Lakers")
        taskListModel.set(2, "Play the Los Angeles Clippers")
        taskListModel.set(3, "Beat the Golden State Warriors")
        taskListModel.set(4, "Make it to the NBA Finals")
        updateUserPosition()
    }

    override fun keyReleased(e: KeyEvent) {}
    override fun keyTyped(e: KeyEvent) {}
}

/**
 * Launch the application.
 */
fun main() {
    FlatDarkLaf.setup()
    EventQueue.invokeLater { GUI() }
}
