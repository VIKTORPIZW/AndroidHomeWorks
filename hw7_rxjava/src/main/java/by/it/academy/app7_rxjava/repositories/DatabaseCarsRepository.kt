package by.it.academy.app7_rxjava.repositories

import android.content.Context
import by.it.academy.app7_rxjava.database.DatabaseCars
import by.it.academy.app7_rxjava.entity.CarItem
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class DatabaseCarsRepository(context: Context) {

    private val dao = DatabaseCars.init(context)

    fun getAllCarsSortedByProducer(): @NonNull Single<MutableList<CarItem>>? {
        return Single.create<MutableList<CarItem>> {
            val list = dao.getCarDatabaseDAO().getAllCarsSorted()
            it.onSuccess(list)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun addCar(carItem: CarItem) {
        Single.create<CarItem> {
            dao.getCarDatabaseDAO().addCarToDatabase(carItem)

        }.subscribeOn(Schedulers.io()).subscribe()
    }

    fun updateCar(carItem: CarItem) {
        Single.create<CarItem> {
            dao.getCarDatabaseDAO().updateCar(carItem)
        }.subscribeOn(Schedulers.io()).subscribe()
    }

}


