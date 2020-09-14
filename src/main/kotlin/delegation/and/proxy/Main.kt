package delegation.and.proxy

import delegation.and.proxy.io.FileAudioOutput
import delegation.and.proxy.io.FileAudioInput
import delegation.and.proxy.processors.*
import delegation.and.proxy.proxy.AutoLoggedProcessor

fun main() {
    // Путь до файла со входными данными
    val sourcePath = "data/test.music"
    // Путь до файла, в который будут помещены выходные данные
    val destinationPath = "data/processed.test.music"

    // Получение звукового сигнала
    val data = FileAudioInput(sourcePath).read()

    // Обработка звука
    val delay = Delay()
    val processor = sequentialProcessorOf(
        Distortion(),
        delay,
        WahWah(),
    ).apply {
        // Удаляет delay из середины цепи и добавляет его в конец.
        remove(delay)
        add(delay)
        // Добавляет Chorus, протоколируемый через прокси AutoLoggedProcessor
        add(AutoLoggedProcessor(Chorus(), "Chorus"))
    }
    val processedData = processor.process(data)

    // Запись обработанного звукового сигнала в файл
    with(FileAudioOutput(destinationPath)) {
        write(processedData)
        close()
    }
}