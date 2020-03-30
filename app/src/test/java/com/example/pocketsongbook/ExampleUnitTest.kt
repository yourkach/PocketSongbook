package com.example.pocketsongbook

import org.junit.Test

import org.junit.Assert.*
import java.lang.StringBuilder

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun htmlCompatTest() {
        val songText =
            "Спи в заброшенном доме, то в сладкой истоме, \n" +
                    "      <b>Am</b>    <b>Em</b> <b>D</b>\n" +
                    "То в судорогах.\n" +
                    "    <b>G</b>     <b>Cmaj7</b>    <b>Em</b>      <b>Am</b>      <b>Em</b> \n" +
                    "Слепи из пыли и тлена смешного оленя \n" +
                    "   <b>Am</b>         <b>Em</b> <b>D</b>\n" +
                    "На быстрых ногах.\n" +
                    "  <b>G</b>    <b>Cmaj7</b>     <b>Em</b>        <b>Am</b>       <b>Em</b> \n" +
                    "Плач испуганным зверем, и вырастет дерево \n" +
                    "    <b>Am</b>        <b>Em</b> <b>D</b>\n" +
                    "Из мертвого пня.\n" +
                    " <b>G</b>        <b>Cmaj7</b>      <b>Em</b>        <b>Am</b>      <b>Em</b> \n" +
                    "Сядь в разбитый трамвай и, глаза закрывая,\n" +
                    " <b>Am</b>        <b>Em</b> <b>D</b>\n" +
                    "Увидишь меня.  \n" +
                    "\n" +
                    "Припев:\n" +
                    "         <b>A</b>                         <b>C</b>\n" +
                    "       Кто-то разрешил трамвайным рельсам\n" +
                    "                       <b>Em</b>\n" +
                    "       Разрезать этот город.\n" +
                    "            <b>A</b>                       <b>C</b>\n" +
                    "       Трамвай идет разбитый, громыхая, через ночь\n" +
                    "                 <b>Em</b>\n" +
                    "       Ножом по горлу.\n" +
                    "\n" +
                    "  <b>G</b>      <b>Cmaj7</b>     <b>Em</b>          <b>Am</b>       <b>Em</b> \n" +
                    "Мне все лучше и лучше, мне хочется слушать, \n" +
                    "      <b>Am</b>    <b>Em</b> <b>D</b>\n" +
                    "но устали чтецы\n" +
                    "  <b>G</b>      <b>Cmaj7</b>     <b>Em</b>          <b>Am</b> <b>Em</b> \n" +
                    "Пить весенние капли из сморщенной \n" +
                    "      <b>Am</b>    <b>Em</b> <b>D</b>\n" +
                    "лапы медведицы.\n" +
                    "  <b>G</b>      <b>Cmaj7</b>     <b>Em</b>          <b>Am</b>     <b>Em</b> \n" +
                    "Кто откроет мне двери испуганным зверем \n" +
                    "      <b>Am</b>    <b>Em</b> <b>D</b>\n" +
                    "из мертвого пня.\n" +
                    "  <b>G</b>      <b>Cmaj7</b>     <b>Em</b>          <b>Am</b>     <b>Em</b> \n" +
                    "Там в разбитом трамвае, глаза закрывая, \n" +
                    "      <b>Am</b>    <b>Em</b> <b>D</b>\n" +
                    "Ты увидишь меня.\n" +
                    "\n" +
                    "Припев:\n" +
                    "         <b>A</b>                         <b>C</b>\n" +
                    "       Кто-то разрешил трамвайным рельсам\n" +
                    "                       <b>Em</b>\n" +
                    "       Разрезать этот город.\n" +
                    "         <b>A</b>                         <b>C</b>\n" +
                    "       Трамвай идет разбитый, громыхая, через ночь\n" +
                    "                       <b>Em</b>\n" +
                    "       Ножом...  Ножом...  Ножом...  Ножом"

        val newTextBuilder = StringBuilder()
        var chordStartIndex = songText.indexOf("<b>")
        var prevChordEnd = 0
        while (chordStartIndex != -1) {
            val chordEnd = songText.indexOf("</b>", chordStartIndex + 3)
            val chord =
                songText.substring(
                    chordStartIndex + 3,
                    chordEnd
                )
            newTextBuilder.append(songText.substring(prevChordEnd, chordStartIndex + 3))
            newTextBuilder.append(chord)
            newTextBuilder.append("</b>")
            prevChordEnd = chordEnd + 4
            chordStartIndex = songText.indexOf("<b>", prevChordEnd)
        }
        newTextBuilder.append(songText.substring(prevChordEnd))
        println(newTextBuilder.toString())
    }
}
