package id.ac.unpas.tubes.repositories

import com.benasher44.uuid.uuid4
import com.skydoves.sandwich.message
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import com.skydoves.whatif.whatIfNotNull
import id.ac.unpas.tubes.model.SepedaMotor
import id.ac.unpas.tubes.networks.SepedaMotorApi
import id.ac.unpas.tubes.persistences.SepedaMotorDao
import javax.inject.Inject

class SepedaMotorRepository @Inject constructor(
    private val api: SepedaMotorApi,
    private val dao: SepedaMotorDao
) : Repository {
    suspend fun loadItems(
        onSuccess: (List<SepedaMotor>) -> Unit,
        onError: (List<SepedaMotor>, String) -> Unit
    ) {
        val list: List<SepedaMotor> = dao.getList()
        api.all()
// handle the case when the API request gets a success response.
            .suspendOnSuccess {
                data.whatIfNotNull {
                    it.data?.let { list ->
                        dao.insertAll(list)
                        val items: List<SepedaMotor> = dao.getList()
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
        model: String,
        warna: String,
        kapasitas: Int,
        tanggal_rilis: String,
        harga: Int,
        onSuccess: (SepedaMotor) -> Unit,
        onError: (SepedaMotor?, String) -> Unit
    ) {
        val id = uuid4().toString()
        val item = SepedaMotor(id, model, warna, kapasitas, tanggal_rilis, harga)
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
        model: String,
        warna: String,
        kapasitas: Int,
        tanggal_rilis: String,
        harga: Int,
        onSuccess: (SepedaMotor) -> Unit,
        onError: (SepedaMotor?, String) -> Unit
    ) {
        val item = SepedaMotor(id, model, warna, kapasitas, tanggal_rilis, harga)
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

    suspend fun find(id: String): SepedaMotor? {
        return dao.find(id)
    }
}