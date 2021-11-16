>Автоматическое логирование.
>Цель:
>
>Понять как реализуется AOP, какие для этого есть технические средства.
>
>Разработайте такой функционал:
>метод класса можно пометить самодельной аннотацией `@Log`, например, так:
>
>```java
>class TestLogging {
>    @Log
>    public void calculation(int param) {}
>}
>```
>
>При вызове этого метода "автомагически" в консоль должны логироваться значения параметров.
>Например так.
>
>```java
>class Demo {
>    public void action() {
>        new TestLogging().calculation(6);
>    }
>}
>```
>
>В консоле дожно быть:
>executed method: calculation, param: 6
>
>Обратите внимание: явного вызова логирования быть не должно.
>
>Учтите, что аннотацию можно поставить, например, на такие методы: <br>
>`public void calculation(int param1)` <br>
>`public void calculation(int param1, int param2)` <br>
>`public void calculation(int param1, int param2, String param3)` <br>
>
>P.S.
>Выбирайте реализацию с ASM, если действительно этого хотите и уверены в своих силах.
