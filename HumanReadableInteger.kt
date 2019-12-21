package ir.adanic.kilid.fragment.salam

import androidx.annotation.IntDef

object HumanReadableInteger {
    const val ONES = 1
    const val TEENS = 2
    const val TENS = 3
    const val HUNDREDS = 4
    const val THOUSANDS = 5
    const val ENGLISH = 0
    const val PERSIAN = 1

    private val delimiters: Map<Int, String> = mapOf((ENGLISH to " "), (PERSIAN to " و "))
    private val negative: Map<Int, String> = mapOf((ENGLISH to "Negative "), (PERSIAN to "منفی "))
    private val zero: Map<Int, String> = mapOf((ENGLISH to "Zero"), (PERSIAN to "صفر"))
    private val numberWords: List<NumberWord> = listOf(
//            NumberWord(TENS, ENGLISH, listOf("", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine")),
            NumberWord(ONES, PERSIAN, listOf("", "یک", "دو", "سه", "چهار", "پنج", "شش", "هفت", "هشت", "نه")),
//            NumberWord(TEENS, ENGLISH, listOf("Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen")),
            NumberWord(TEENS, PERSIAN, listOf("ده", "یازده", "دوازده", "سیزده", "چهارده", "پانزده", "شانزده", "هفده", "هجده", "نوزده")),
//            NumberWord(TENS, ENGLISH, listOf("Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety")),
            NumberWord(TENS, PERSIAN, listOf("بیست", "سی", "چهل", "پنجاه", "شصت", "هفتاد", "هشتاد", "نود")),
//            NumberWord(HUNDREDS, ENGLISH, listOf("", "One Hundred", "Two Hundred", "Three Hundred", "Four Hundred", "Five Hundred", "Six Hundred", "Seven Hundred", "Eight Hundred", "Nine Hundred")),
            NumberWord(HUNDREDS, PERSIAN, listOf("", "یکصد", "دویست", "سیصد", "چهارصد", "پانصد", "ششصد", "هفتصد", "هشتصد", "نهصد")),
//            NumberWord(THOUSANDS, ENGLISH, listOf("", " Thousand", " Million", " Billion", " Trillion", " Quadrillion", " Quintillion", " Sextillian", " Septillion", " Octillion", " Nonillion", " Decillion", " Undecillion", " Duodecillion", " Tredecillion", " Quattuordecillion", " Quindecillion", " Sexdecillion", " Septendecillion", " Octodecillion", " Novemdecillion", " Vigintillion", " Unvigintillion", " Duovigintillion", " 10^72", " 10^75", " 10^78", " 10^81", " 10^84", " 10^87", " Vigintinonillion", " 10^93", " 10^96", " Duotrigintillion", " Trestrigintillion")),
            NumberWord(THOUSANDS, PERSIAN, listOf("", " هزار", " میلیون", " میلیارد", " تریلیون", " Quadrillion", " Quintillion", " Sextillian", " Septillion", " Octillion", " Nonillion", " Decillion", " Undecillion", " Duodecillion", " Tredecillion", " Quattuordecillion", " Quindecillion", " Sexdecillion", " Septendecillion", " Octodecillion", " Novemdecillion", " Vigintillion", " Unvigintillion", " Duovigintillion", " 10^72", " 10^75", " 10^78", " 10^81", " 10^84", " 10^87", " Vigintinonillion", " 10^93", " 10^96", " Duotrigintillion", " Trestrigintillion")))

    fun numberToText(number: String, @Language language: Int) = numberToText(number.toLong(), language)

    fun numberToText(number: Long, @Language language: Int): String {
        return when (number) {
            0L -> zero.getOrElse(language) { "" }
            else -> if (number < 0) negative[language].toString() + numberToText(-number, language) else toWord(number, language, "", 0)
        }
    }



    private fun getName(idx: Int, @Language language: Int, @DigitGroup group: Int): String {
        for (x in numberWords) if (x.group == group && x.language == language) return x.names[idx]
        return ""
    }

    private fun toWord(number: Long, @Language language: Int, leftDigitsText: String, thousands: Int): String {
        if (number == 0L) return leftDigitsText
        var wordValue = leftDigitsText
        if (wordValue.isNotEmpty()) wordValue += delimiters[language]
        wordValue += when {
            number < 10 -> getName(number.toInt(), language, ONES)
            number < 20 -> getName((number - 10).toInt(), language, TEENS)
            number < 100 -> toWord(number % 10, language, getName((number / 10 - 2).toInt(), language, TENS), 0)
            number < 1000 -> toWord(number % 100, language, getName((number / 100).toInt(), language, HUNDREDS), 0)
            else -> toWord(number % 1000, language, toWord(number / 1000, language, "", thousands + 1), 0)
        }
        return if (number % 1000 == 0L) wordValue else wordValue + getName(thousands, language, THOUSANDS)
    }

    @IntDef(ONES, TEENS, TENS, HUNDREDS, THOUSANDS)
    @Retention(AnnotationRetention.SOURCE)
    private annotation class DigitGroup

    @IntDef(ENGLISH, PERSIAN)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Language

    internal data class NumberWord(
            @DigitGroup
            var group: Int,
            @Language
            var language: Int,
            var names: List<String>)
}


