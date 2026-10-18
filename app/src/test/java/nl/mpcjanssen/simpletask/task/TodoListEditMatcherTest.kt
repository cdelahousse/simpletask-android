package nl.mpcjanssen.simpletask.task

import junit.framework.TestCase

class TodoListEditMatcherTest : TestCase() {
    fun testFindsSameTaskObject() {
        val original = Task("Call plumber @home +house")
        val tasks = listOf(Task("Other task"), original)

        assertEquals(1, TodoListEditMatcher.findEditableTaskIndex(tasks, original))
    }

    fun testFindsReloadedTaskByUuid() {
        val original = Task("Call plumber @home +house uuid:abc-123")
        val reloaded = Task("Call plumber @home +house uuid:abc-123")
        val tasks = listOf(Task("Other task"), reloaded)

        assertEquals(1, TodoListEditMatcher.findEditableTaskIndex(tasks, original))
    }

    fun testFindsReloadedTaskByTextWhenNoUuid() {
        val original = Task("Call plumber @home +house")
        val reloaded = Task("Call plumber @home +house")
        val tasks = listOf(Task("Other task"), reloaded)

        assertEquals(1, TodoListEditMatcher.findEditableTaskIndex(tasks, original))
    }

    fun testReturnsMinusOneWhenTaskCannotBeMatched() {
        val original = Task("Call plumber @home +house")
        val tasks = listOf(Task("Other task"), Task("Different task"))

        assertEquals(-1, TodoListEditMatcher.findEditableTaskIndex(tasks, original))
    }
}
