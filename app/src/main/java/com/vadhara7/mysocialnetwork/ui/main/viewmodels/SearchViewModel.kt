package com.vadhara7.mysocialnetwork.ui.main.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vadhara7.mysocialnetwork.data.entities.User
import com.vadhara7.mysocialnetwork.other.Event
import com.vadhara7.mysocialnetwork.other.Resource
import com.vadhara7.mysocialnetwork.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: MainRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
): ViewModel() {

    private val _searchResults = MutableLiveData<Event<Resource<List<User>>>>()
    val searchResults: LiveData<Event<Resource<List<User>>>> = _searchResults

    fun searchUser(query: String) {
        if(query.isEmpty()) return

        _searchResults.postValue(Event(Resource.Loading()))
        viewModelScope.launch(dispatcher) {
            val result = repository.searchUser(query)
            _searchResults.postValue(Event(result))
        }
    }
}