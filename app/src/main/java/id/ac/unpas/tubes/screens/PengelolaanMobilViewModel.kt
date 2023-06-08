package id.ac.unpas.tubes.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.ac.unpas.tubes.model.Mobil
import id.ac.unpas.tubes.repositories.MobilRepository
import javax.inject.Inject

@HiltViewModel
class PengelolaanMobilViewModel @Inject constructor(private val mobilRepository: MobilRepository) : ViewModel() {
    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get()= _isLoading
    private val _success: MutableLiveData<Boolean> = MutableLiveData(false)
    val success: LiveData<Boolean> get() = _success
    private val _toast: MutableLiveData<String> = MutableLiveData()
    val toast: LiveData<String> get() = _toast
    private val _listData: MutableLiveData<List<Mobil>> = MutableLiveData()
    val listData: LiveData<List<Mobil>> get() = _listData

    suspend fun loadItems() {
        _isLoading.postValue(true)
        mobilRepository.loadItems(onSuccess = {
            _isLoading.postValue(false)
            _listData.postValue(it)
        }, onError = { list, message ->
            _toast.postValue(message)
            _isLoading.postValue(false)
            _listData.postValue(list)
        })
    }

    suspend fun loadItem(id: String, onSuccess: (Mobil?) -> Unit) {
        val item = mobilRepository.find(id)
        onSuccess(item)
    }

    suspend fun update(
        id: String,
        merk: String,
        model: String,
        bahan_bakar: String,
        dijual: Int,
        deskripsi: String,
    ) {
        _isLoading.postValue(true)
        mobilRepository.update(id, model, merk, bahan_bakar, dijual, deskripsi,
            onError = {
                    item, message ->
                _toast.postValue(message)
                _isLoading.postValue(false)
            }, onSuccess = {
                _success.postValue(true)
                _isLoading.postValue(false)
            })
    }

    suspend fun insert(
        merk: String,
        model: String,
        bahan_bakar: String,
        dijual: Int,
        deskripsi: String,
    ) {
        _isLoading.postValue(true)
        mobilRepository.insert(model, merk, bahan_bakar, dijual, deskripsi,
            onError = {
                    item, message ->
                _toast.postValue(message)
                _isLoading.postValue(false)
            }, onSuccess = {
                _success.postValue(true)
                _isLoading.postValue(false)
            })
    }

    suspend fun delete(id: String) {
        _isLoading.postValue(true)
        mobilRepository.delete(id, onError = { message ->
            _toast.postValue(message)
            _isLoading.postValue(false)
            _success.postValue(true)
        }, onSuccess = {
            _toast.postValue("Data Berhasil Dihapus")
            _isLoading.postValue(false)
            _success.postValue(true)
        })
    }
}