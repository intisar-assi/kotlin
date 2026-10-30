package com.example.kotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    val taskManager = TaskManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun main() {
        println("==== Task Manager ====")
        val menu = listOf(
            "1. Add Task ",
            "2. Show all Tasks ",
            "3. Search by ID",
            "4. Mark Task As Done ",
            "5. Delete Task",
            "6. Summary ",
            "0. Exit "
        )
        menu.forEach { println(it) }
        println("Enter your choice :")
        val choice = readlnOrNull()?.toIntOrNull()

        if (choice == 1) {
            addTask()
        } else if (choice == 2) {
            showAllTasks()
        } else if (choice == 3) {
            searchById()
        } else if (choice == 4) {
            markTaskAsDone()
        } else if (choice == 5) {
            deleteTask()
        } else if (choice == 6) {
            summary()
        } else if (choice == 0) {
            println("Exiting...")
        }
    }

    fun addTask() {
    }

    fun showAllTasks() {
        taskManager.showAllTasks()
    }

    fun searchById(id: Int = 1) {
        val task = taskManager.searchById(id)
        if (task != null) {
            task.display()
        } else {
            println("Task not found!")
        }
    }

    fun markTaskAsDone(id: Int = 1) {
        taskManager.markTaskAsDone(id)
    }

    fun deleteTask(id: Int = 1) {
        taskManager.deleteTask(id)
    }

    fun summary() {
        taskManager.getSummary()
    }
}

abstract class Task(
    val id: Int,
    var title: String,
    var category: String,
    var priority: Int,
    var isDone: Boolean = false
) {
    abstract fun typeLabel(): String

    fun display() {
        println(
            "ID: $id | Title: $title | " +
                    "Category: $category | Priority: $priority | " +
                    "Done: $isDone | Type: ${typeLabel()}"
        )
    }
}

class WorkTask(
    id: Int,
    title: String,
    category: String,
    priority: Int,
    val deadlineDay: Int
) : Task(id, title, category, priority) {
    init {
        require(deadlineDay in 1..31) {
            "Deadline day must be between 1 and 31"
        }
    }

    override fun typeLabel(): String {
        return "Work"
    }
}

class PersonalTask(
    id: Int,
    title: String,
    category: String,
    priority: Int
) : Task(id, title, category, priority) {

    override fun typeLabel(): String {
        return "Personal"
    }
}

class TaskManager {
    val tasks = mutableListOf<Task>()
    val usedIds = mutableSetOf<Int>()
    val priorityCounts = IntArray(3)
    val categoryCounts = mutableMapOf<String, Int>()

    fun addTask(task: Task) {
        tasks.add(task)
        usedIds.add(task.id)
        println("Task added successfully!")
    }

    fun showAllTasks() {
        if (tasks.isEmpty()) {
            println("No tasks available to show")
            return
        }

        for (task in tasks) {
            task.display()
        }
    }

    fun searchById(id: Int): Task? {
        for (task in tasks) {
            if (task.id == id) {
                return task
            }
        }
        return null
    }

    fun findById(id: Int): Task? = searchById(id)

    fun markTaskAsDone(id: Int) {
        val task = findById(id)

        if (task == null) {
            println("Task not found!")
        } else if (task.isDone) {
            println("Task is already done!")
        } else {
            task.isDone = true
            println("Task marked as done!")
        }
    }

    fun deleteTask(id: Int) {
        val task = findById(id)

        if (task == null) {
            println("Task not found!")
            return
        }

        tasks.remove(task)
        usedIds.remove(task.id)
        println("Task deleted successfully!")
    }

    fun getSummary(): Summary {
        var done = 0
        var pending = 0
        var low = 0
        var medium = 0
        var high = 0

        for (task in tasks) {
            if (task.isDone) {
                done++
            } else {
                pending++
            }

            if (task.priority == 1) {
                low++
            } else if (task.priority == 2) {
                medium++
            } else if (task.priority == 3) {
                high++
            }
        }

        println("Total Tasks: ${tasks.size}")
        println("Done Tasks: $done")
        println("Pending Tasks: $pending")
        println("Low Priority: $low")
        println("Medium Priority: $medium")
        println("High Priority: $high")

        return Summary(
            total = tasks.size,
            done = done,
            pending = pending
        )
    }
}

data class Summary(
    val total: Int,
    val done: Int,
    val pending: Int
)
