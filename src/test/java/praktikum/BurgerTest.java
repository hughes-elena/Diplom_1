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
    private Ingredient ingredient0;
    @Mock
    private Ingredient ingredient1;
    @Mock
    private Ingredient ingredient2;

    private Burger burger; // что будем тестить

    @Before
    public void makeBurger() {
        burger = new Burger();
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
    public void addIngredientTest() {
        burger.addIngredient(ingredient0);
        assertEquals("В бургер добавлен один ингредиент", 1, burger.ingredients.size());
        assertTrue("Список должен содержать добавленный ингредиент", burger.ingredients.contains(ingredient0));
    }

    @Test //тест на удаление ингредиента
    public void removeIngredientTest() {
        //добавляем ингредиенты
        burger.addIngredient(ingredient0);
        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);

//удаляем первый
        burger.removeIngredient(0);

        // проверяем, что осталось 2 ингр
        assertEquals("После удаления должно остаться 2 ингредиента", 2, burger.ingredients.size());

        // Проверяем, что ингр(0) удалился
        assertFalse("Удаленный ингредиент все еще присутствует", burger.ingredients.contains(ingredient0));

        // проверяем, что ингр1 и 2 остались
        assertTrue("Ингредиент 1 должен остаться", burger.ingredients.contains(ingredient1));
        assertTrue("Ингредиент 2 должен остаться", burger.ingredients.contains(ingredient2));
    }

    @Test //тест на изменение позиций ингред
    public void moveIngredientTest() {
        //добавляем 3 игред
        burger.addIngredient(ingredient0);
        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);
        //перемещаем первый на последнее место 0 -> 2
        burger.moveIngredient(0, 2);
        //проверяем новый порядок
        assertEquals("На позиции 0 стоит ingredient1", ingredient1, burger.ingredients.get(0));
        assertEquals("На позиции 1 стоит ingredient2", ingredient2, burger.ingredients.get(1));
        assertEquals("На позиции 2 стоит ingredient0", ingredient0, burger.ingredients.get(2));
    }

    @Test //тест расчет цены бургера. Булочка считается за каждую отдельно(верхняя и нижняя = 2) Цена будет=булка*2+ингр1+ингр2+инг3
    public void getPriceTest() {
        //добавляем булочку
        burger.setBuns(bun);
        //добавляем ингредиенты
        burger.addIngredient(ingredient0);
        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);

        //настраиваем моки и даем цену: булочка = 50 флоат, ингр0 = 35 флоат, ингр1 = 15 флоат, ингр2 = 20 флоат: 50*2+35+15+20=170флоат
        when(bun.getPrice()).thenReturn(50.0F);
        when(ingredient0.getPrice()).thenReturn(35.0F);
        when(ingredient1.getPrice()).thenReturn(25.0F);
        when(ingredient2.getPrice()).thenReturn(20.0F);

        float expectedPrice = 50.0F * 2 + 35.0F + 25.0F + 20.0F;

        //Проверяем расчет
        assertEquals("Цена рассчитана неверно", expectedPrice, burger.getPrice(), 1.0F);
    }

    @Test //тест на получение чека
    public void getReceiptTest() {

        // Настроим булочку
        when(bun.getName()).thenReturn("red bun");
        when(bun.getPrice()).thenReturn(300F);
        burger.setBuns(bun);

        // Настроим ингредиент FILLING
        when(ingredient0.getName()).thenReturn("dinosaur");
        when(ingredient0.getType()).thenReturn(IngredientType.FILLING);
        when(ingredient0.getPrice()).thenReturn(200F);
        burger.addIngredient(ingredient0);

        // Настроим ингредиент SAUCE
        when(ingredient1.getName()).thenReturn("chili sauce");
        when(ingredient1.getType()).thenReturn(IngredientType.SAUCE);
        when(ingredient1.getPrice()).thenReturn(300F);
        burger.addIngredient(ingredient1);

        // Настроим ингредиент FILLING
        when(ingredient2.getName()).thenReturn("cutlet");
        when(ingredient2.getType()).thenReturn(IngredientType.FILLING);
        when(ingredient2.getPrice()).thenReturn(100F);
        burger.addIngredient(ingredient2);

        float expectedPrice = 300F * 2 + 200F + 300F + 100F;
        String expectedPriceString = String.format("Price: %f", expectedPrice); //%f — формат для вещественного (дробного) числа (float или double)

        // Получаем чек
        String receipt = burger.getReceipt();

        // Проверяем, что чек содержит всё нужное
        assertTrue("Чек должен содержать название булочки", receipt.contains("red bun"));
        assertTrue("Чек должен содержать название ингредиента", receipt.contains("dinosaur"));
        assertTrue("Чек должен содержать тип ингредиента (sauce)", receipt.contains("chili sauce"));
        assertTrue("Чек должен содержать название ингредиента", receipt.contains("cutlet"));
        assertTrue("Чек должен содержать итоговую цену", receipt.contains(expectedPriceString));
    }


}