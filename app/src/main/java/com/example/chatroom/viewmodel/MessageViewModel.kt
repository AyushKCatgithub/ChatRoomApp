package com.example.chatroom.viewmodel


import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatroom.data.Injection
import com.example.chatroom.data.Message
import com.example.chatroom.data.MessageRepository
import com.example.chatroom.data.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MessageViewModel : ViewModel() {

    private val messageRepository: MessageRepository
    private val userRepository: UserRepository

    init {
        messageRepository = MessageRepository(Injection.instance())
        userRepository = UserRepository(
            FirebaseAuth.getInstance(),
            Injection.instance()
        )
        loadCurrentUser()
    }

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages

    private val _roomId = MutableLiveData<String>()
    private val _currentUser = MutableLiveData<com.example.chatroom.data.User?>()
    val currentUser: MutableLiveData<com.example.chatroom.data.User?> get() = _currentUser

    private fun loadCurrentUser() {
        viewModelScope.launch {
            when (val result = userRepository.getCurrentUser()) {
                is com.example.chatroom.data.Result.Success -> {
                    val customUser = result.data
                    _currentUser.value = customUser
                }

                is com.example.chatroom.data.Result.Error -> {
                    Log.e("Error", result.exception.message ?: "Unknown error")
                }
            }

        }
    }

    fun loadMessages() {
        viewModelScope.launch {
            if (_roomId != null) {
                messageRepository.getChatMessages(_roomId.value.toString())
                    .collect { _messages.value = it }
            }
        }
    }

    @RequiresApi(value = 29)
    fun sendMessage(text: String) {
        if (_currentUser.value != null) {
            val message = Message(
                senderFirstName = _currentUser.value!!.firstName,
                senderId = _currentUser.value!!.email,
                text = text,
                timestamp = System.currentTimeMillis()
            )
            viewModelScope.launch {
                when (messageRepository.sendMessage(_roomId.value.toString(), message)) {
                    is com.example.chatroom.data.Result.Error -> TODO()
                    is com.example.chatroom.data.Result.Success -> TODO()
                }
            }
        }
    }

    fun setRoomId(roomId: String) {
        _roomId.value = roomId
        loadMessages()
    }
}