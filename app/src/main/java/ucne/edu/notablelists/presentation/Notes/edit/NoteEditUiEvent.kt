package ucne.edu.notablelists.presentation.Notes.edit

sealed interface NoteEditUiEvent {
    data object NavigateBack : NoteEditUiEvent
    data object NavigateToLogin : NoteEditUiEvent
    data object NavigateToFriendList : NoteEditUiEvent
}