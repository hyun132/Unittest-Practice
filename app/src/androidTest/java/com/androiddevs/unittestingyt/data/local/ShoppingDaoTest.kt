package com.androiddevs.unittestingyt.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.androiddevs.unittestingyt.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class) //java와 kotlin 의 모든 테스트는 jvm에서 실행됨.
@SmallTest
class ShoppingDaoTest {

    //아래에서 라이브데이터 차단했지만..junit이랑 뭐 문제있는듯..?
    @get:Rule
    var instantTaskExecutorRule=InstantTaskExecutorRule()

    private lateinit var database : ShoppingItemDatabase
    private lateinit var dao : ShoppingDao

    @Before
    fun setup(){
        //진짜 db아닌 ram에만 저장하기때문에 실제로 저장되는것이 아님.
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ShoppingItemDatabase::class.java
        ).allowMainThreadQueries().build() // 테스트시에는 서로 다른 스레드가 서로 조작하는것을 방지하고자(스레드가 동시에 실행되는 방식 예측할수 없으므로) 메인쓰레드로 강제함.
        dao = database.shoppingDao()
    }

    @After
    fun teardown(){
        database.close()
    }

    @Test
    fun insertShoppingItem()= runBlockingTest {
        val shoppingItem = ShoppingItem("name",1,1f,"url",1)
        dao.insertShoppingItem(shoppingItem)

        val allShoppingItem = dao.observeAllShoppingItems().getOrAwaitValue() //livedata가 비동기인데 이 문제 해결해줄

        assertThat(allShoppingItem).contains(shoppingItem)
    }

    @Test
    fun deleteShoppingItem()= runBlockingTest {
        val shoppingItem = ShoppingItem("name",1,1f,"url",1)
        dao.insertShoppingItem(shoppingItem)
        dao.deleteShoppingItem(shoppingItem)

        val allShoppingItem = dao.observeAllShoppingItems().getOrAwaitValue() //livedata가 비동기인데 이 문제 해결해줄

        assertThat(allShoppingItem).doesNotContain(shoppingItem)
    }

    @Test
    fun observeTotalPriceSum()= runBlockingTest {
        val shoppingItem = ShoppingItem("name",2,10f,"url",1)
        val shoppingItem2 = ShoppingItem("name",4,5.5f,"url",2)
        val shoppingItem3 = ShoppingItem("name",0,100f,"url",3)

        dao.insertShoppingItem(shoppingItem)
        dao.insertShoppingItem(shoppingItem2)
        dao.insertShoppingItem(shoppingItem3)

        val totalPriceSum = dao.observeTotalPrice().getOrAwaitValue()

        assertThat(totalPriceSum).isEqualTo(2*10f+4*5.5f)
    }

}