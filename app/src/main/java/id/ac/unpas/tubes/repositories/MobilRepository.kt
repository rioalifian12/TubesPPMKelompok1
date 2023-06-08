package id.ac.unpas.tubes.repositories

import com.benasher44.uuid.uuid4
import com.skydoves.sandwich.message
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import com.skydoves.whatif.whatIfNotNull
import id.ac.unpas.tubes.model.Mobil
import id.ac.unpas.tubes.networks.MobilApi
import id.ac.unpas.tubes.persistences.MobilDao
import javax.inject.Inject

class MobilRepository @Inject constructor(
    private val api: MobilApi,
    private val dao: MobilDao
) : Repository {
    suspend fun loadItems(
        onSuccess: (List<Mobil>) -> Unit,
        onError: (List<Mobil>, String) -> Unit
    ) {
        val list: List<Mobil> = dao.getList()
        api.all()
// handle the case when the API request gets a success response.
            .suspendOnSuccess {
                data.whatIfNotNull {
                    it.data?.let { list ->
                        dao.insertAll(list)
                        val items: List<Mobil> = dao.getList()
                        onSuccess(items)
                    }
                }
            }
// handle the case when the API request gets an error response.
// e.g. internal server error.
            .suspendOnError {
                onError(list, message())
            }
// handle the case when the API request gets an exception response.
// e.g. network connection error.
            .suspendOnException {
                onError(list, message())
            }
    }

    suspend fun insert(
        merk: String,
        model: String,
        bahan_bakar: String,
        dijual: Int,
        deskripsi: String,
        onSuccess: (Mobil) -> Unit,
        onError: (Mobil?, String) -> Unit
    ) {
        val id = uuid4().toString()
        val item = Mobil(id, merk, model, bahan_bakar, dijual, deskripsi)
        dao.insertAll(item)
        api.insert(item)
// handle the case when the API request gets a success response.
            .suspendOnSuccess {
                onSuccess(item)
            }
// handle the case when the API request gets an error response.
// e.g. internal server error.
            .suspendOnError {
                onError(item, message())
            }
// handle the case when the API request gets an exception response.
// e.g. network connection error.
            .suspendOnException {
                onError(item, message())
            }
    }

    suspend fun update(
        id: String,
        merk: String,
        model: String,
        bahan_bakar: String,
        dijual: Int,
        deskripsi: String,
        onSuccess: (Mobil) -> Unit,
        onError: (Mobil?, String) -> Unit
    ) {
        val item = Mobil(id, merk, model, bahan_bakar, dijual, deskripsi)
        dao.insertAll(item)
        api.update(id, item)
// handle the case when the API request gets a success response.
            .suspendOnSuccess {
                onSuccess(item)
            }
// handle the case when the API request gets an error response.
// e.g. internal server error.
            .suspendOnError {
                onError(item, message())
            }
// handle the case when the API request gets an exception response.
// e.g. network connection error.
            .suspendOnException {
                onError(item, message())
            }
    }

    suspend fun delete(
        id: String, onSuccess: () -> Unit, onError: (String) -> Unit
    ) {
        dao.delete(id)
        api.delete(id)
// handle the case when the API request gets a success response.
            .suspendOnSuccess {
                data.whatIfNotNull {
                    onSuccess()
                }
            }
// handle the case when the API request gets an error response.
// e.g. internal server error.
            .suspendOnError {
                onError(message())
            }
// handle the case when the API request gets an exception response.
// e.g. network connection error.
            .suspendOnException {
                onError(message())
            }
    }

    suspend fun find(id: String): Mobil? {
        return dao.find(id)
    }
}