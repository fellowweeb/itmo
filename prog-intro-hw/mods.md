# Тесты к курсу «Введение в программирование»

## Домашнее задание 13. Обработка ошибок

Модификации
 * *Базовая*
    * Класс `ExpressionParser` должен реализовывать интерфейс
        [Parser](expression/exceptions/Parser.java)
    * Классы `CheckedAdd`, `CheckedSubtract`, `CheckedMultiply`,
        `CheckedDivide` и `CheckedNegate` должны реализовывать интерфейс
        [TripleExpression](expression/TripleExpression.java)
    * Нельзя использовать типы `long` и `double`
    * Нельзя использовать методы классов `Math` и `StrictMath`
    * [Исходный код тестов](expression/exceptions/ExceptionsTest.java)
* *PowLog*
    * Дополнительно реализуйте бинарные операции (максимальный приоритет):
        * `**` – возведение в степень, `2 ** 3` равно 8;
        * `//` – логарифм, `10 // 2` равно 3.
    * [Исходный код тестов](expression/exceptions/ExceptionsPowLogTest.java)


## Домашнее задание 12. Разбор выражений

Модификации
 * *Базовая*
    * Класс `ExpressionParser` должен реализовывать интерфейс
        [Parser](expression/parser/Parser.java)
    * Результат разбора должен реализовывать интерфейс
        [TripleExpression](expression/TripleExpression.java)
    * [Исходный код тестов](expression/parser/ParserTest.java)
 * *Shifts*
    * Дополнительно реализуйте бинарные операции:
        * `<<` – сдвиг влево, минимальный приоритет (`1 << 5 + 3` равно `1 << (5 + 3)` равно 256);
        * `>>` – сдвиг вправо, минимальный приоритет (`1024 >> 5 + 3` равно `1024 >> (5 + 3)` равно 4);
    * [Исходный код тестов](expression/parser/ParserShiftsTest.java)
 * *ReverseDigits*
    * Реализуйте операции из модификации *Shifts*.
    * Дополнительно реализуйте унарные операции (приоритет как у унарного минуса):
        * `reverse` – число с переставленными цифрами, `reverse -12345` равно -54321;
        * `digits` – сумма цифр числа, `digits -12345` равно 15.
    * [Исходный код тестов](expression/parser/ParserReverseDigitsTest.java)
 * *Abs*
    * Реализуйте операции из модификации *Shifts*.
    * Дополнительно реализуйте унарные операции (приоритет как у унарного минуса):
        * `abs` – модуль числа, `abs -5` равно 5;
        * `square` – возведение в квадрат, `square -5` равно 25.
    * [Исходный код тестов](expression/parser/ParserAbsSquareTest.java)


## Домашнее задание 11. Выражения

Модификации
 * *Базовая*
    * Реализуйте интерфейс [Expression](expression/Expression.java)
    * [Исходный код тестов](expression/ExpressionTest.java)
        * Запускать c аргументом `easy` или `hard`
 * *Triple*
    * Дополнительно реализуйте интерфейс [TripleExpression](expression/TripleExpression.java)
    * [Исходный код тестов](expression/TripleExpressionTest.java)


## Домашнее задание 10. Игра n,m,k

Модификации
 * *Матчи*
    * Добавьте поддержку матчей: последовательность игр указанного числа побед
    * Стороны в матче должны меняться каждую игру


## Домашнее задание 7. Разметка

Модификации
 * *HTML*
    * Дополнительно реализуйте метод `toHtml`, генерирующий HTML-разметку:
      * выделеный текст окружается тегом `em`;
      * сильно выделеный текст окружается тегом `strong`;
      * зачеркнутый текст окружается тегом `s`.
    * [Исходный код тестов](markup/HtmlTest.java)
 * *TeX*
    * Дополнительно реализуйте метод `toTex`, генерирующий TeX-разметку:
      * выделеный текст заключается в `\emph{` и `}`;
      * сильно выделеный текст заключается в `\textbf{` и `}`;
      * зачеркнутый текст заключается в `\textst{` и `}`.
    * [Исходный код тестов](markup/TexTest.java)
 * *Tex списки*
    * Добавьте поддержку:
      * Нумерованных списков (класс `OrderedList`, окружение `enumerate`): последовательность элементов
      * Ненумерованных списков (класс `UnorderedList`, окружение `itemize`): последовательность элементов
      * Элементов списка (класс `ListItem`, тег `\item`: последовательность абзацев и списков
    * Для новых классов поддержка Markdown не требуется
    * [Исходный код тестов](markup/TexListTest.java)


Исходный код тестов:

 * [MarkdownTest.java](markup/MarkdownTest.java)
 * [AbstractTest.java](markup/AbstractTest.java)


## Домашнее задание 6. Подсчет слов++

Модификации
 * *LastIndex*
    * Вместо номеров вхождений во всем файле надо указывать
      только последнее вхождение в каждой строке
    * Класс должен иметь имя `WordStatLastIndex`
    * [Исходный код тестов](wordStat/WordStatLastIndexTest.java)

Исходный код тестов:

* [WordStatIndexTest.java](wordStat/WordStatIndexTest.java)
* [WordStatIndexChecker.java](wordStat/WordStatIndexChecker.java)


## Домашнее задание 5. Свой сканнер

Модификации
 * *Min*
    * Рассмотрим входные данные как (не полностью определенную) матрицу,
      вместо каждого числа выведите минимум из чисел в его столбце и строке
    * Класс должен иметь имя `ReverseMin`
    * [Исходный код тестов](reverse/FastReverseMinTest.java)

Исходный код тестов:

* [FastReverseTest.java](reverse/FastReverseTest.java)


## Домашнее задание 4. Подсчет слов

Модификации
 * *Count*
    * В выходном файле слова должны быть упорядочены по возрастанию числа
      вхождений, а при равном числе вхождений – по порядку первого вхождения
      во входном файле
    * Класс должен иметь имя `WordStatCount`
    * [Исходный код тестов](wordStat/WordStatCountTest.java)

Исходный код тестов:

* [WordStatInputTest.java](wordStat/WordStatInputTest.java)
* [WordStatChecker.java](wordStat/WordStatChecker.java)


## Домашнее задание 3. Реверс

Модификации
 * *Sum*
    * Рассмотрим входные данные как (не полностью определенную) матрицу,
      вместо каждого числа выведите сумму чисел в его столбце и строке
    * Класс должен иметь имя `ReverseSum`
    * [Исходный код тестов](reverse/ReverseSumTest.java)

Исходный код тестов:

* [ReverseTest.java](reverse/ReverseTest.java)
* [ReverseChecker.java](reverse/ReverseChecker.java)