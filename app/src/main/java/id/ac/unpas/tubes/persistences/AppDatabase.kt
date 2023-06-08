package id.ac.unpas.tubes.persistences

import androidx.room.Database
import androidx.room.RoomDatabase
import id.ac.unpas.tubes.model.Mobil
import id.ac.unpas.tubes.model.SepedaMotor
import id.ac.unpas.tubes.model.Promo

@Database(entities = [Mobil::class, SepedaMotor::class, Promo::class], version = 2)
abstract class AppDatabase: RoomDatabase() {
    abstract fun mobilDao(): MobilDao
    abstract fun sepedaMotorDao(): SepedaMotorDao
    abstract fun promoDao(): PromoDao
}
