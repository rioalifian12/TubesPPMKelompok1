package id.ac.unpas.tubes.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.ac.unpas.tubes.model.SepedaMotor
import id.ac.unpas.tubes.repositories.SepedaMotorRepository
import javax.inject.Inject

@HiltViewModel
class PengelolaanMotorViewModel @Inject constructor(private val sepedaMotorRepository: SepedaMotorRepository) : ViewModel()
{
    private val _isLoading: MutableLiveData<Boolean> =
        MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _success: MutableLiveData<Boolean> =
        MutableLiveData(false)
    val success: LiveData<Boolean> get() = _success
    private val _toast: MutableLiveData<String> =
        MutableLiveData()
    val toast: LiveData<String> get() = _toast
    private val _list: MutableLiveData<List<SepedaMotor>> =
        MutableLiveData()
    val list: LiveData<List<SepedaMotor>> get() = _list
    suspend fun loadItems()
    {
        _isLoading.postValue(true)
        sepedaMotorRepository.loadItems(onSuccess = {
            _isLoading.postValue(false)
            _list.postValue(it)
        }, onError = { list, message ->
            _toast.postValue(message)
            _isLoading.postValue(false)
            _list.postValue(list)
        })
    }
    suspend fun insert(model: String,
                       warna: String,
                       kapasitas: Int,
                       tanggal_rilis: String,
                       harga: Int,){
        _isLoading.postValue(true)
        sepedaMotorRepository.insert(model, warna, kapasitas, tanggal_rilis, harga,
            onError = { item, message ->
                _toast.postValue(message)
                _isLoading.postValue(false)
            }, onSuccess = {
                _isLoading.postValue(false)
                _success.postValue(true)
            })
    }

    suspend fun loadItem(id: String, onSuccess: (SepedaMotor?) ->
    Unit) {
        val item = sepedaMotorRepository.find(id)
        onSuccess(item)
    }
    suspend fun update(id: String,
                       model: String,
                       warna: String,
                       kapasitas: Int,
                       tanggal_rilis: String,
                       harga: Int,){
        _isLoading.postValue(true)
        sepedaMotorRepository.update(id, model, warna, kapasitas, tanggal_rilis, harga,
            onError = { item, message ->
                _toast.postValue(message)
                _isLoading.postValue(false)
            }, onSuccess = {
                _isLoading.postValue(false)
                _success.postValue(true)
            })
    }

    suspend fun delete(id: String) {
        _isLoading.postValue(true)
        sepedaMotorRepository.delete(id, onError = { message ->
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