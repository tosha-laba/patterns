package main

import delegation.and.proxy.io.FileAudioInput
import delegation.and.proxy.io.FileAudioOutput
import delegation.and.proxy.logging.Logger
import delegation.and.proxy.logging.writers.ConsoleLogWriter
import delegation.and.proxy.processors.sequentialProcessorOf
import delegation.and.proxy.processors.single.Delay
import delegation.and.proxy.processors.single.Distortion
import delegation.and.proxy.processors.single.WahWah
import structural.part.one.decorators.GainFilter
import structural.part.one.decorators.PanFilter
import structural.part.one.decorators.VolumeFilter

fun main() {
    // Инициализация логгера
    Logger.setWriters(
        ConsoleLogWriter(),
    )

    // Путь до файла со входными данными
    val sourcePath = "data/test.music"
    // Путь до файла, в который будут помещены выходные данные
    val destinationPath = "data/processed.test.music"

    // Получение звукового сигнала
    val data = with(FileAudioInput(sourcePath)) {
        val data = read()
        close()
        data
    }

    // Создание обработчика
    val processor = sequentialProcessorOf(
        Distortion(),
        Delay(),
        WahWah(),
    )

    // Пример декорирования объектов
    val decoratedProcessor = PanFilter(
        GainFilter(
            VolumeFilter(processor).apply {
                volume = 10
            }
        ).apply {
            gain = 20
        }
    ).apply {
        balance = 75
    }
    val processedData = decoratedProcessor.process(data)

    // Запись обработанного звукового сигнала в файл
    with(FileAudioOutput(destinationPath)) {
        write(processedData)
        close()
    }
}