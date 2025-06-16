package praktikum;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BurgerTest {

    @Mock
    private Bun bun; // мокаем булки
    @Mock
    private Ingredient dinosaurFillingIngredient;
    @Mock
    private Ingredient chiliSauceIngredient;
    @Mock
    private Ingredient cutletFillingIngredient;

    private Burger burger; // что будем тестить

    @Before
    public void makeBurger() {
        burger = new Burger();
    }


    // Метод для настройки бургера с булочкой и ингредиентами
    private void setupBurgerWithBunAndIngredients() {
        when(bun.getName()).thenReturn("red bun");
        when(bun.getPrice()).thenReturn(300F);
        burger.setBuns(bun);

        when(dinosaurFillingIngredient.getName()).thenReturn("dinosaur");
        when(dinosaurFillingIngredient.getType()).thenReturn(IngredientType.FILLING);
        when(dinosaurFillingIngredient.getPrice()).thenReturn(200F);
        burger.addIngredient(dinosaurFillingIngredient);

        when(chiliSauceIngredient.getName()).thenReturn("chili sauce");
        when(chiliSauceIngredient.getType()).thenReturn(IngredientType.SAUCE);
        when(chiliSauceIngredient.getPrice()).thenReturn(300F);
        burger.addIngredient(chiliSauceIngredient);

        when(cutletFillingIngredient.getName()).thenReturn("cutlet");
        when(cutletFillingIngredient.getType()).thenReturn(IngredientType.FILLING);
        when(cutletFillingIngredient.getPrice()).thenReturn(100F);
        burger.addIngredient(cutletFillingIngredient);
    }

    @Test //тест на булочку
    public void setBunsTest() {
        burger.setBuns(bun);    //устанавливаем мок-булку в бургер
        assertEquals("Булочка должна быть установлена корректно", bun, burger.bun); //Проверяем, что булка действительно установлена в объекте
    }

    @Test //тест на верную булочку
    public void setBunsCorrectBunNameTest() {
        burger.setBuns(bun);
        when(bun.getName()).thenReturn("Название булочки");
        assertEquals("Установлена неверная булочка", "Название булочки", burger.bun.getName());
    }

    @Test //тест на добавление ингред
    public void addIngredientShouldIncreaseIngredientCountTest() {
        burger.addIngredient(dinosaurFillingIngredient);
        assertEquals("В бургер добавлен один ингредиент", 1, burger.ingredients.size());
    }

    @Test
    public void addIngredientShouldAddCorrectIngredient() {
        burger.addIngredient(dinosaurFillingIngredient);
        assertTrue("Список должен содержать добавленный ингредиент", burger.ingredients.contains(dinosaurFillingIngredient));
    }

    @Test //тест на удаление ингредиента
    public void removeIngredientShouldDecreaseIngredientsCount() {
        //добавляем ингредиенты
        burger.addIngredient(dinosaurFillingIngredient);
        burger.addIngredient(chiliSauceIngredient);
        burger.addIngredient(cutletFillingIngredient);

        //удаляем первый
        burger.removeIngredient(0);

        // проверяем, что осталось 2 ингр
        assertEquals("После удаления должно остаться 2 ингредиента", 2, burger.ingredients.size());
    }

    @Test //тест, что удалился верный ингредиент
    public void removeIngredientShouldRemoveCorrectIngredient() {
        //добавляем ингредиенты
        burger.addIngredient(dinosaurFillingIngredient);
        burger.addIngredient(chiliSauceIngredient);
        burger.addIngredient(cutletFillingIngredient);

        //удаляем первый
        burger.removeIngredient(0);

        // Проверяем, что ингр(0) удалился
        assertFalse("Удаленный ингредиент все еще присутствует", burger.ingredients.contains(dinosaurFillingIngredient));
    }

    @Test //тест, что остальные игредиенты остались
    public void removeIngredientShouldKeepOtherIngredients() {
        burger.addIngredient(dinosaurFillingIngredient);
        burger.addIngredient(chiliSauceIngredient);
        burger.addIngredient(cutletFillingIngredient);

        burger.removeIngredient(0);
        // проверяем, что ингр1 и 2 остались
        assertTrue("Ингредиент 1 должен остаться", burger.ingredients.contains(chiliSauceIngredient));
        assertTrue("Ингредиент 2 должен остаться", burger.ingredients.contains(cutletFillingIngredient));
    }

    @Test //тест на изменение позиций ингред dinosaur на последнее место
    public void moveIngredientShouldChangeOrderPositionTest() {
        //добавляем 3 игред
        burger.addIngredient(dinosaurFillingIngredient);
        burger.addIngredient(chiliSauceIngredient);
        burger.addIngredient(cutletFillingIngredient);
        //перемещаем первый на последнее место 0 -> 2
        burger.moveIngredient(0, 2);
        //проверяем новый порядок
        assertEquals("На позиции 0 стоит chiliSauceIngredient", chiliSauceIngredient, burger.ingredients.get(0));
    }

    //Тесты расчета цены
    @Test
//тест расчет цены бургера. Булочка считается за каждую отдельно(верхняя и нижняя = 2) Цена будет=булка*2+ингр1+ингр2+инг3
    public void getPriceShouldCalculateCorrectly() {
        //добавляем булочку
        burger.setBuns(bun);
        //добавляем ингредиенты
        burger.addIngredient(dinosaurFillingIngredient);
        burger.addIngredient(chiliSauceIngredient);
        burger.addIngredient(cutletFillingIngredient);

        //настраиваем моки и даем цену: булочка = 50 флоат, ингр0 = 35 флоат, ингр1 = 15 флоат, ингр2 = 20 флоат: 50*2+35+15+20=170флоат
        when(bun.getPrice()).thenReturn(50.0F);
        when(dinosaurFillingIngredient.getPrice()).thenReturn(35.0F);
        when(chiliSauceIngredient.getPrice()).thenReturn(25.0F);
        when(cutletFillingIngredient.getPrice()).thenReturn(20.0F);

        float expectedPrice = 50.0F * 2 + 35.0F + 25.0F + 20.0F;

        //Проверяем расчет
        assertEquals("Цена рассчитана неверно", expectedPrice, burger.getPrice(), 1.0F);
    }

    @Test //тест на получение чека
    public void getReceiptShouldContainBunName() {

        // Настроим бургер
        setupBurgerWithBunAndIngredients();

        // Получаем чек
        String receipt = burger.getReceipt();

        // Проверяем, что чек содержит всё нужное
        assertTrue("Чек должен содержать название булочки", receipt.contains("red bun"));
    }

    @Test
    public void getReceiptShouldContainIngredientNameDinosaur() {
        // Настроим бургер
        setupBurgerWithBunAndIngredients();

        String receipt = burger.getReceipt();
        assertTrue("Чек должен содержать название ингредиента", receipt.contains("dinosaur"));
    }

    @Test
    public void getReceiptShouldContainIngredientNameChiliSauce() {
        // Настроим бургер
        setupBurgerWithBunAndIngredients();

        String receipt = burger.getReceipt();
        assertTrue("Чек должен содержать тип ингредиента (sauce)", receipt.contains("chili sauce"));
    }

    @Test
    public void getReceiptShouldContainIngredientNameCutlet() {
        // Настроим бургер
        setupBurgerWithBunAndIngredients();

        String receipt = burger.getReceipt();
        assertTrue("Чек должен содержать название ингредиента", receipt.contains("cutlet"));
    }

    @Test
    public void getReceiptShouldContainTotalPrice() {
        //Настроим бургер
        setupBurgerWithBunAndIngredients();

        String receipt = burger.getReceipt();

        float expectedPrice = 300F * 2 + 200F + 300F + 100F;
        String expectedPriceString = String.format("Price: %f", expectedPrice); //%f — формат для вещественного (дробного) числа (float или double)

        assertTrue("Чек должен содержать итоговую цену", receipt.contains(expectedPriceString));
    }


}