package delegation.and.proxy.logging

/**
 * Логгер проекта.
 */
object Logger : Loggable {
    /**
     * Конкретный логгер, использующийся для протоколирования.
     */
    private val impl: Loggable by lazy {
        ConsoleLogger()
    }

    /**
     * Набор объектов для отслеживания их идентификаторов.
     *
     * __Важное замечание__:
     * В данной реализации [Set] используются стандартные ссылки на объекты,
     * следовательно, все протоколируемые объекты будут иметь хотя бы одну
     * живую ссылку на себя и не будут удалены сборщиком мусора.
     *
     * В "реальных" приложениях можно решить эту проблему реализацией
     * WeakSet, в которой используются "слабые" ссылки, которые не влияют
     * на работу сборщика мусора.
     *
     * Время жизни программы в лабораторных работах маленькое (буквально
     * несколько секунд), поэтому для простоты было сделано так.
     *
     * В курсовом проекте будет использоваться другой логгер.
     */
    private val idPool by lazy {
        mutableSetOf<Any>()
    }

    override fun log(obj: Any, role: String, method: String, methodRole: String, id: Int) {
        // Помещаем объект в набор и получаем идентификатор для него.
        val actualId = idPool.apply { add(obj) }.indexOf(obj)
        impl.log(obj, role, method, methodRole, actualId)
    }
}