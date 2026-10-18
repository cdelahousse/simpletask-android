package nl.mpcjanssen.simpletask.task

import junit.framework.TestCase

class TodoListEditMatcherTest : TestCase() {
    fun testFindsSameTaskObject() {
        val original = Task("Call plumber @home +house")
        val tasks = listOf(Task("Other task"), original)

        assertEquals(1, TodoListEditMatcher.findEditableTaskIndex(tasks, original))
    }

    fun testFindsReloadedTaskByUuid() {
        val original = Task("Call plumber @home +house uuid:0f8fad5b-d9cb-469f-a165-70867728950e")
        val reloaded = Task("Call the plumber again @home +house uuid:0f8fad5b-d9cb-469f-a165-70867728950e")
        val tasks = listOf(Task("Other task uuid:7c9e6679-7425-40de-944b-e07fc1f90ae7"), reloaded)

        assertEquals(1, TodoListEditMatcher.findEditableTaskIndex(tasks, original))
    }

    fun testFindsReloadedTaskByTextWhenNoUuid() {
        val original = Task("Call plumber @home +house")
        val reloaded = Task("Call plumber @home +house")
        val tasks = listOf(Task("Other task"), reloaded)

        assertEquals(1, TodoListEditMatcher.findEditableTaskIndex(tasks, original))
    }

    fun testDoesNotMatchSameTextWithDifferentUuid() {
        // The original was removed elsewhere; a separate task with the same text must not be replaced
        val original = Task("Water plants uuid:0f8fad5b-d9cb-469f-a165-70867728950e")
        val sibling = Task("Water plants uuid:7c9e6679-7425-40de-944b-e07fc1f90ae7")
        val tasks = listOf(Task("Other task"), sibling)

        assertEquals(-1, TodoListEditMatcher.findEditableTaskIndex(tasks, original))
    }

    fun testReturnsMinusOneWhenTaskCannotBeMatched() {
        val original = Task("Call plumber @home +house")
        val tasks = listOf(Task("Other task"), Task("Different task"))

        assertEquals(-1, TodoListEditMatcher.findEditableTaskIndex(tasks, original))
    }
}
