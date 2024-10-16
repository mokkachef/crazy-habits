package com.example.crazy_habits

import androidx.test.core.app.ActivityScenario
import androidx.test.filters.LargeTest
import com.example.crazy_habits.screen.KFirstActivityScreen
import com.example.crazy_habits.screen.KGoodListFragmentScreen
import com.example.crazy_habits.screen.KHabitEditFragmentScreen
import com.example.data.HabitEditRepositoryImpl
import com.example.domain.entities.Habit
import com.example.domain.entities.Priority
import com.example.domain.entities.Type
import com.example.presentation.listhabits.goodlist.GoodListFragment
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.kakao.spinner.KSpinnerItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

/**
 * UI tests for the implementation of [GoodListFragment].
 */
@HiltAndroidTest
@LargeTest
class GoodListFragmentTest : TestCase() {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var habitEditRepositoryImpl: HabitEditRepositoryImpl

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun fabIsWorking() {
        val activityScenario = ActivityScenario.launch(FirstActivity::class.java)
        KGoodListFragmentScreen {
            fab {
                isVisible()
                click()
            }
        }
        activityScenario.close()
    }

    @Test
    fun checkFirstPageTitle() {
        val activityScenario = ActivityScenario.launch(FirstActivity::class.java)
        KGoodListFragmentScreen {
            checkPageTitle("good")
        }
        activityScenario.close()
    }

    @Test
    fun editHabit() = run {
        runBlocking {
            habitEditRepositoryImpl.addHabit(testHabit)
            val activityScenario = ActivityScenario.launch(FirstActivity::class.java)
            val newNameText = "super habit"
            val descriptionText = "Lorem ipsum"
            val numberText = "5"
            val frequencyText = "6"
            val typeText = "полезная"
            val priorityText = "Низкий"

            step("проверка, что на экране хороших привычек есть привычка") {
                KGoodListFragmentScreen {
                    rvListHabits {
                        childAt<KGoodListFragmentScreen.HabitListScreenItem>(0) {
                            isVisible()
                            isClickable()
                            tvHabitName.isVisible()
                            tvHabitDescription.isVisible()
                            tvHabitDescription.hasAnyText()
                            click()
                        }
                    }
                }
            }
            step("редактирование привычки") {
                KHabitEditFragmentScreen {
                    name.replaceText(newNameText)
                    description.replaceText(descriptionText)
                    typeRb1.click()
                    typeRb0.click()
                    priority {
                        open()
                        childAt<KSpinnerItem>(2) {
                            click()
                        }
                    }
                    number.replaceText(numberText)
                    frequency.replaceText(frequencyText)
                    submitButton {
                        isVisible()
                        isClickable()
                        hasText("изменить")
                        click()
                    }
                }
            }
            step("проверка, что все поля изменились и удаление привычки") {
                KGoodListFragmentScreen {
                    rvListHabits {
                        childAt<KGoodListFragmentScreen.HabitListScreenItem>(0) {
                            tvHabitName.isVisible()
                            tvHabitName.hasText(newNameText)

                            tvHabitPriority.isVisible()
                            tvHabitPriority.hasText(priorityText)

                            tvHabitType.isVisible()
                            tvHabitType.hasText(typeText)

                            tvHabitFrequency.isVisible()
                            tvHabitFrequency.hasText(frequencyText)

                            tvHabitDescription.isVisible()
                            tvHabitDescription.hasText(descriptionText)

                            //delete habit
                            longClick()
                        }
                    }
                }
            }
            activityScenario.close()
        }
    }

    @Test
    fun createNewHabit() = run {
        val activityScenario = ActivityScenario.launch(FirstActivity::class.java)

        val newNameText = "mega habit"
        val descriptionText = "Lorem ipsum"
        val numberText = "7"
        val frequencyText = "8"
        val typeText = "полезная"
        val priorityText = "Низкий"

        step("переход на экран создания привычки") {
            KGoodListFragmentScreen {
                fab.click()
            }
        }
        step("заполнение полей") {
            KHabitEditFragmentScreen {
                name.typeText(newNameText)
                description.typeText(descriptionText)
                typeRb1.click()
                typeRb0.click()
                priority {
                    open()
                    childAt<KSpinnerItem>(2) {
                        click()
                    }
                }
                closeSoftKeyboard()
                number.typeText(numberText)
                closeSoftKeyboard()
                frequency.replaceText(frequencyText)
                closeSoftKeyboard()
                submitButton {

                    isVisible()
                    isClickable()
                    click()
                }
            }
        }
        step("переход обратно на экран со списком, проверка данных") {
            KGoodListFragmentScreen {
                rvListHabits {
                    childAt<KGoodListFragmentScreen.HabitListScreenItem>(0) {
                        tvHabitName.isVisible()
                        tvHabitName.hasText(newNameText)

                        tvHabitPriority.isVisible()
                        tvHabitPriority.hasText(priorityText)

                        tvHabitType.isVisible()
                        tvHabitType.hasText(typeText)

                        tvHabitFrequency.isVisible()
                        tvHabitFrequency.hasText(frequencyText)

                        tvHabitDescription.isVisible()
                        tvHabitDescription.hasText(descriptionText)

                        //delete habit
                        longClick()
                    }
                }
            }
        }
        activityScenario.close()
    }

    @Test
    fun checkSorting() = run {
        val name0 = "ff"
        val name1 = "ww"
        val name2 = "aa"
        runBlocking {
            habitEditRepositoryImpl.addHabit(createHabit(name = name0))
            habitEditRepositoryImpl.addHabit(createHabit(name = name1))
            habitEditRepositoryImpl.addHabit(createHabit(name = name2))
        }
        val activityScenario = ActivityScenario.launch(FirstActivity::class.java)

        step("проверка текущего порядка") {
            KGoodListFragmentScreen {
                rvListHabits {
                    childAt<KGoodListFragmentScreen.HabitListScreenItem>(0) {
                        tvHabitName.isVisible()
                        tvHabitName.hasText(name0)
                    }
                    childAt<KGoodListFragmentScreen.HabitListScreenItem>(1) {
                        tvHabitName.isVisible()
                        tvHabitName.hasText(name1)
                    }
                    childAt<KGoodListFragmentScreen.HabitListScreenItem>(2) {
                        tvHabitName.isVisible()
                        tvHabitName.hasText(name2)
                    }
                }
            }
        }

        step("сортировка ASC") {
            KFirstActivityScreen {
                sortButton.click()
            }
            KGoodListFragmentScreen {
                rvListHabits {
                    childAt<KGoodListFragmentScreen.HabitListScreenItem>(0) {
                        tvHabitName.isVisible()
                        tvHabitName.hasText(name2)
                    }
                    childAt<KGoodListFragmentScreen.HabitListScreenItem>(1) {
                        tvHabitName.isVisible()
                        tvHabitName.hasText(name0)
                    }
                    childAt<KGoodListFragmentScreen.HabitListScreenItem>(2) {
                        tvHabitName.isVisible()
                        tvHabitName.hasText(name1)
                    }
                }
            }
        }

        step("сортировка DESC") {
            KFirstActivityScreen {
                sortButton.click()
            }
            KGoodListFragmentScreen {
                rvListHabits {
                    childAt<KGoodListFragmentScreen.HabitListScreenItem>(0) {
                        tvHabitName.isVisible()
                        tvHabitName.hasText(name1)
                    }
                    childAt<KGoodListFragmentScreen.HabitListScreenItem>(1) {
                        tvHabitName.isVisible()
                        tvHabitName.hasText(name0)
                    }
                    childAt<KGoodListFragmentScreen.HabitListScreenItem>(2) {
                        tvHabitName.isVisible()
                        tvHabitName.hasText(name2)
                    }
                }
            }
        }

        step("удаление добавленных привычек и закрытие activity") {
            KGoodListFragmentScreen {
                rvListHabits {
                    childAt<KGoodListFragmentScreen.HabitListScreenItem>(0) { longClick() }
                    childAt<KGoodListFragmentScreen.HabitListScreenItem>(0) { longClick() }
                    childAt<KGoodListFragmentScreen.HabitListScreenItem>(0) { longClick() }
                }
            }
            activityScenario.close()
        }

    }

    @Test
    fun checkFilter() = run {
        val name0 = "ff"
        val name1 = "wwa"
        val name2 = "aa"
        runBlocking {
            habitEditRepositoryImpl.addHabit(createHabit(name = name0))
            habitEditRepositoryImpl.addHabit(createHabit(name = name1))
            habitEditRepositoryImpl.addHabit(createHabit(name = name2))
        }
        val activityScenario = ActivityScenario.launch(FirstActivity::class.java)

        step("проверка, что привычки есть в списке") {
            KGoodListFragmentScreen {
                rvListHabits {
                    childAt<KGoodListFragmentScreen.HabitListScreenItem>(0) {
                        tvHabitName.isVisible()
                        tvHabitName.hasText(name0)
                    }
                    childAt<KGoodListFragmentScreen.HabitListScreenItem>(1) {
                        tvHabitName.isVisible()
                        tvHabitName.hasText(name1)
                    }
                    childAt<KGoodListFragmentScreen.HabitListScreenItem>(2) {
                        tvHabitName.isVisible()
                        tvHabitName.hasText(name2)
                    }
                }
            }
        }
        step("фильтрация по a") {
            KFirstActivityScreen {
                filterButton.click()
                bottomSheetHeader.click()
                filterText.typeText("a")
                bottomSheetFilterButton.click()
                closeSoftKeyboard()
                pressBack()
            }
            KGoodListFragmentScreen {
                rvListHabits {
                    childAt<KGoodListFragmentScreen.HabitListScreenItem>(0) {
                        tvHabitName.isVisible()
                        tvHabitName.hasText(name1)
                    }
                    childAt<KGoodListFragmentScreen.HabitListScreenItem>(1) {
                        tvHabitName.isVisible()
                        tvHabitName.hasText(name2)
                    }
                }
            }
        }

        step("фильтрация по ww") {
            KFirstActivityScreen {
                filterButton.click()
                bottomSheetHeader.click()
                filterText.typeText("ww")
                bottomSheetFilterButton.click()
                closeSoftKeyboard()
                pressBack()
            }
            KGoodListFragmentScreen {
                rvListHabits {
                    childAt<KGoodListFragmentScreen.HabitListScreenItem>(0) {
                        tvHabitName.isVisible()
                        tvHabitName.hasText(name1)
                    }
                    childAt<KGoodListFragmentScreen.HabitListScreenItem>(1) { doesNotExist() }
                }
            }
        }

        step("очистка фильтра, удаление добавленных привычек и закрытие activity") {
            KFirstActivityScreen {
                filterButton.click()
                bottomSheetHeader.click()
                filterText.clearText()
                bottomSheetFilterButton.click()
                closeSoftKeyboard()
                pressBack()
            }
            KGoodListFragmentScreen {
                rvListHabits {
                    childAt<KGoodListFragmentScreen.HabitListScreenItem>(0) { longClick() }
                    childAt<KGoodListFragmentScreen.HabitListScreenItem>(0) { longClick() }
                    childAt<KGoodListFragmentScreen.HabitListScreenItem>(0) { longClick() }
                }
            }
            activityScenario.close()
        }

    }


    @Test
    fun scrollWhenNewHabitAddedCheck() = run {
        step("создание ряда привычек") {
            val name0 = "0"
            val name1 = "1"
            val name2 = "2"
            val name3 = "3"
            val name4 = "4"
            val name5 = "5"
            val name6 = "6"
            val name7 = "7"
            runBlocking {
                habitEditRepositoryImpl.addHabit(createHabit(name = name0))
                habitEditRepositoryImpl.addHabit(createHabit(name = name1))
                habitEditRepositoryImpl.addHabit(createHabit(name = name2))
                habitEditRepositoryImpl.addHabit(createHabit(name = name3))
                habitEditRepositoryImpl.addHabit(createHabit(name = name4))
                habitEditRepositoryImpl.addHabit(createHabit(name = name5))
                habitEditRepositoryImpl.addHabit(createHabit(name = name6))
                habitEditRepositoryImpl.addHabit(createHabit(name = name7))
            }
        }

        val activityScenario = ActivityScenario.launch(FirstActivity::class.java)

        step("добавление еще привычек, проверка скрола в конец списка") {
            for (i in 0..10) {
                val newNameText = "mega_habit_$i"
                val descriptionText = "Lorem ipsum"
                val numberText = "7"
                val frequencyText = "8"

                KGoodListFragmentScreen {
                    fab.click()
                }
                KHabitEditFragmentScreen {
                    name.typeText(newNameText)
                    description.typeText(descriptionText)
                    typeRb1.click()
                    typeRb0.click()
                    priority {
                        open()
                        childAt<KSpinnerItem>(2) {
                            click()
                        }
                    }
                    closeSoftKeyboard()
                    number.typeText(numberText)
                    closeSoftKeyboard()
                    frequency.replaceText(frequencyText)
                    closeSoftKeyboard()
                    submitButton {

                        isVisible()
                        isClickable()
                        click()
                    }
                   runBlocking { delay(200)}
                    KGoodListFragmentScreen {
                        rvListHabits {
                            childAt<KGoodListFragmentScreen.HabitListScreenItem>(8 + i) {
                                tvHabitName.isVisible()
                                tvHabitName.hasText(newNameText)
                            }
                            scrollTo(0)
                        }
                    }
                }
            }
        }
        step("зачистка") {
            activityScenario.use {
                repeat(19) {
                    KGoodListFragmentScreen {
                        rvListHabits {
                            childAt<KGoodListFragmentScreen.HabitListScreenItem>(0) {
                                longClick()
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        val testHabit = createHabit()
        private fun createHabit(
            name: String = "aa",
            desc: String = "bb",
            type: Type = Type.Good,
            priority: Priority = Priority.High,
            number: Int = 2,
            frequency: Int = 3,
            colorHabit: Int = -1,
            isSentToServer: Boolean = false,
            date: Int = 11,
            doneCount: Int = 0,
            id: String = "uid",
        ) = Habit(
            name = name,
            desc = desc,
            type = type,
            priority = priority,
            number = number,
            frequency = frequency,
            colorHabit = colorHabit,
            isSentToServer = isSentToServer,
            date = date,
            doneCount = doneCount,
            id = id,
        )
    }

}