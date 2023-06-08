package id.ac.unpas.tubes.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.ac.unpas.tubes.model.Promo
import id.ac.unpas.tubes.repositories.PromoRepository
import javax.inject.Inject

@HiltViewModel
class PengelolaanPromoViewModel @Inject constructor(private val promoRepository: PromoRepository) : ViewModel()
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
    private val _list: MutableLiveData<List<Promo>> =
        MutableLiveData()
    val list: LiveData<List<Promo>> get() = _list
    suspend fun loadItems()
    {
        _isLoading.postValue(true)
        promoRepository.loadItems(onSuccess = {
            _isLoading.postValue(false)
            _list.postValue(it)
        }, onError = { list, message ->
            _toast.postValue(message)
            _isLoading.postValue(false)
            _list.postValue(list)
        })
    }
    suspend fun insert(model: String,
                       tanggal_awal: String,
                       tanggal_akhir: String,
                       persentase: Int,){
        _isLoading.postValue(true)
        promoRepository.insert(model, tanggal_awal, tanggal_akhir, persentase,
            onError = { item, message ->
                _toast.postValue(message)
                _isLoading.postValue(false)
            }, onSuccess = {
                _isLoading.postValue(false)
                _success.postValue(true)
            })
    }

    suspend fun loadItem(id: String, onSuccess: (Promo?) ->
    Unit) {
        val item = promoRepository.find(id)
        onSuccess(item)
    }
    suspend fun update(id: String,
                       model: String,
                       tanggal_awal: String,
                       tanggal_akhir: String,
                       persentase: Int,){
        _isLoading.postValue(true)
        promoRepository.update(id, model, tanggal_awal, tanggal_akhir, persentase,
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
        promoRepository.delete(id, onError = { message ->
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