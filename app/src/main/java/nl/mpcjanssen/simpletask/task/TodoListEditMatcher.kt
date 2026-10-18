package nl.mpcjanssen.simpletask.task

internal object TodoListEditMatcher {
    fun findEditableTaskIndex(todoItems: List<Task>, original: Task): Int {
        val byObject = todoItems.indexOf(original)
        if (byObject != -1) {
            return byObject
        }

        val originalUuid = original.uuid
        if (originalUuid != null) {
            val byUuid = todoItems.indexOfFirst { it.uuid == originalUuid }
            if (byUuid != -1) {
                return byUuid
            }
        }

        return todoItems.indexOfFirst { it.text == original.text }
    }
}
