package lambda;


public class LambdaExample {

    static int x = 10;
    static int y = 20;

    public static void main(String[] args) {

        // функциональный интерфейс должен содержать только один единственный метод без реализации

        // 1-й вариант применения
        Operation operation;
        operation = ((x, y) -> x + y);

        int result = operation.calculate(10, 5);
        System.out.println(result);


        // 2-й вариант применения
        Operation operation2 = new Operation() {
            @Override
            public int calculate(int x, int y) {
                return x + y;
            }
        };

        int result2 = operation2.calculate(3, 15);
        System.out.println(result2);


        // 3-й вариант применения
        Operation operation3 = (Integer::sum);
        int result3 = operation3.calculate(10, 5);
        System.out.println(result3);


        // для одного функционального интерфейса мы можем определить множество лямбда-выражений
        Operation plus = (int x, int y) -> x + y;
        Operation minus = (int x, int y) -> x - y;
        Operation multiply = (int x, int y) -> x * y;

        System.out.println(plus.calculate(20, 10));     // 30
        System.out.println(minus.calculate(20, 10));    // 10
        System.out.println(multiply.calculate(20, 10)); // 200


        // Терминальные лямбда-выражения

        // 1-й вариант применения
        Printable printer = s -> System.out.println(s);
        printer.print("Hello, Java!");

        // 2-й вариант применения
        Printable printer2 = System.out::println;
        printer.print("Hello, Java 11!");


        // Лямбды и локальные переменные

        // 1-й вариант применения (значения статических переменных можно изменить)
        OperationPlus operationPlus = () -> {
            x = 30;
            return x + y;
        };

        System.out.println(operationPlus.calculate()); // 50
        System.out.println(x); // 30 - значение x изменилось

        // 2-й вариант применения (значения локальных переменных изменять нельзя)
        int n = 70;
        int m = 30;

        OperationPlus op = () -> {
            // n = 100; - так нельзя сделать
            return m + n;
        };

        // n = 100;  - так тоже нельзя
        System.out.println(op.calculate()); // 100


        // Блоки кода в лямбда-выражениях

        Operation operation_ = (int x, int y) -> {

            if (y == 0)
                return 0;
            else
                return x / y;
        };

        System.out.println(operation_.calculate(20, 10));    // 2
        System.out.println(operation_.calculate(20, 0));     // 0


        // Обобщенный функциональный интерфейс

        GenericOperation<Integer> go1 = (x, y) -> x + y;
        GenericOperation<String> go2 = (x, y) -> x + y;

        System.out.println(go1.calculate(20, 10));      // 30
        System.out.println(go2.calculate("20", "10"));  // 2010


        // Лямбды как параметры методов

        // лямбды можно передавать в качестве параметров в методы
        Expression func = (number) -> number % 2 == 0;
        int[] nums = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        System.out.println(sum(nums, func));    // 20

        // можно не определять переменную интерфейса, а сразу передать в метод лямбда-выражение
        int[] nums2 = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        int res = sum(nums2, (number) -> number > 5);  // сумма чисел, которые больше 5
        System.out.println(res);     // 30


        // Ссылки на метод как параметры методов

        // 1-й вариант применения
        int[] nums_ = {-5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5};
        System.out.println(sum(nums_, ExpressionHelper::isEven));

        // 2-й вариант применения
        Expression expr = ExpressionHelper::isPositive;
        System.out.println(sum(nums_, expr));

        // если нам надо вызвать нестатические методы, то в ссылке вместо имени класса
        // применяется имя объекта этого класса
        int[] nums_ns = {-5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5};
        ExpressionHelperNoStatic exprHelperNS = new ExpressionHelperNoStatic();
        System.out.println(sum(nums_ns, exprHelperNS::isEven));  // 0


        // Ссылки на конструкторы
        UserBuilder userBuilder = User::new;
        User user = userBuilder.create("Tom");
        System.out.println(user.getName());


        // Лямбды как результат методов

        // 1-й вариант применения
        OperationExecute oe = action(1);

        int a = oe.execute(6, 5);
        System.out.println(a);          // 11

        int b = action(2).execute(8, 2);
        System.out.println(b);          // 6

        // 2-й вариант применения
        int c = action(2).execute(8, 4);
        System.out.println(c);          // 4
    }

    private static int sum(int[] numbers, Expression func) {

        int result = 0;

        for (int i : numbers) {
            if (func.isEqual(i))
                result += i;
        }
        return result;
    }

    private static OperationExecute action(int number) {

        switch (number) {
            case 1:
                return Integer::sum;
            case 2:
                return (x, y) -> x - y;
            case 3:
                return (x, y) -> x * y;
            default:
                return (x, y) -> 0;
        }
    }
}

interface UserBuilder {
    User create(String name);
}

class User {

    private String name;

    String getName() {
        return name;
    }

    User(String name) {
        this.name = name;
    }
}

interface Expression {
    boolean isEqual(int n);
}

class ExpressionHelper {

    static boolean isEven(int n) {
        return n % 2 == 0;
    }

    static boolean isPositive(int n) {
        return n > 0;
    }
}

class ExpressionHelperNoStatic {

    boolean isEven(int n) {
        return n % 2 == 0;
    }
}

interface GenericOperation<T> {
    T calculate(T x, T y);
}

interface OperationExecute {
    int execute(int x, int y);
}

interface Operation {
    int calculate(int x, int y);
}

interface OperationPlus {
    int calculate();
}

interface Printable {
    void print(String s);
}
