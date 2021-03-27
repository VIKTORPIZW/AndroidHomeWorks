package by.it.academy.app7_rxjava.repositories

import android.content.Context
import by.it.academy.app7_rxjava.database.DatabaseCars
import by.it.academy.app7_rxjava.entity.WorkItem
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class DatabaseWorksRepository(context: Context) {

    private val dao = DatabaseCars.init(context)

    fun getCarWorkList(carPlate: String): Single<MutableList<WorkItem>> = Single.create<MutableList<WorkItem>> {
        val list = dao.getWorkListDatabaseDAO().getCarWorkList(carPlate)
        it.onSuccess(list)
    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    fun addWork(workItem: WorkItem) {
        Single.create<WorkItem> {
            dao.getWorkListDatabaseDAO().addWork(workItem)
        }.subscribeOn(Schedulers.io()).subscribe()
    }

    fun updateWork(workItem: WorkItem) {
        Single.create<WorkItem> {
            dao.getWorkListDatabaseDAO().updateWork(workItem)
        }.subscribeOn(Schedulers.io()).subscribe()
    }

    fun deleteWork(workItem: WorkItem) {
        Single.create<WorkItem> {
            dao.getWorkListDatabaseDAO().deleteWork(workItem)
        }.subscribeOn(Schedulers.io()).subscribe()
    }

    fun getFilteredCarWorkListByWorkType(carPlate: String,
                                         workStatus: Int): Single<MutableList<WorkItem>>? = Single.create<MutableList<WorkItem>> {
        val list = dao.getWorkListDatabaseDAO().getFilteredCarWorkListByWorkType(carPlate, workStatus)
        it.onSuccess(list)
    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

}