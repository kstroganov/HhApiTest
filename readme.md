# Тестовое задание от HeadHunter для трудоустройства на должность "Специалист по автоматизированному тестированию"

Данное тестовое задание выполнено на Groovy с использованием тестового фреймворка Grooviola (https://github.com/v-kharlamov/grooviola).
Хотя в условиях задания язык реализации был назначен Java, именно из за удобства использования упомянутого фреймворка проект был
выполнен на Groovy. Впрочем это практически не меняет сути - Groovy это тот же Java, только с синтаксическим сахаром.

Для запуска проекта необходимо скачать проект Grooviola по ссылке https://github.com/v-kharlamov/grooviola.git и добавить его к проекту теста.

Проект необходимо запускать с параметрами java com.vkharlamov.grooviola.Main -tests "tests/Java/JavaTest.groovy" -gui true
где com.vkharlamov.grooviola.Main - main class
    -tests "tests/SmokeTestScript.groovy" -gui true - входные параметры запуска