package day_007_part2

class Hand(val type: HandType, val cards: List<String>, val value: Int, val bidAmount: Int) : Comparable<Hand> {


    companion object {
        fun parse(line: String): Hand {
            val bidAmount = line.split(" ")[1].toInt()
            val cards = line.split(" ")[0].split("").filter { it.isNotBlank() }

            val count = mutableMapOf<String, Int>()
            for (card in cards) {
                if (count[card] == null) {
                    count[card] = 1
                } else {
                    count[card] = count[card]!! + 1
                }
            }
            count[""] = 0

            for (entry in count) {
                if (entry.key == "J") {
                    continue
                }
                val jCount = count.getOrDefault("J", 0)
                if (entry.value + jCount == 5) {
                    return Hand(HandType.FIVE_OF_KIND, cards, mapValue(entry.key), bidAmount)
                }
            }

            for (entry in count) {
                if (entry.key == "J") {
                    continue
                }
                val jCount = count.getOrDefault("J", 0)
                if (entry.value + jCount == 4) {
                    return Hand(HandType.FOUR_OF_KIND, cards, mapValue(entry.key), bidAmount)
                }
            }

            for (entry in count) {
                if (entry.key == "J") {
                    continue
                }
                val jCount = count.getOrDefault("J", 0)

                if (entry.value + jCount == 3) {
                    for (entry2 in count) {
                        if (entry == entry2 || entry2.key == "J") {
                            continue
                        }
                        if (entry2.value == 2) {
                            return Hand(
                                HandType.FULL_HOUSE,
                                cards,
                                mapValue(entry.key) * 100 + mapValue(entry2.key),
                                bidAmount
                            )
                        }
                    }
                    //?????
                    return Hand(HandType.THREE_OF_KIND, cards, mapValue(entry.key), bidAmount)
                }
            }


            for (entry in count) {
                if (entry.key == "J") {
                    continue
                }
                val jCount = count.getOrDefault("J", 0)
                if (entry.value + jCount == 2) {
                    for (entry2 in count) {
                        if (entry == entry2 || entry2.key == "J") {
                            continue
                        }
                        if (entry2.value == 2) {
                            return if (mapValue(entry.key) > mapValue(entry2.key)) {
                                Hand(
                                    HandType.TWO_PAIRS, cards,
                                    mapValue(entry.key) * 100 + mapValue(entry2.key),
                                    bidAmount
                                )
                            } else {
                                Hand(
                                    HandType.TWO_PAIRS, cards,
                                    mapValue(entry2.key) * 100 + mapValue(entry.key),
                                    bidAmount
                                )
                            }
                        }
                    }
                }
            }
            for (entry in count) {
                if (entry.key == "J") {
                    continue
                }
                val jCount = count.getOrDefault("J", 0)
                if (entry.value + jCount == 2) {
                    return Hand(HandType.PAIR, cards, mapValue(entry.key), bidAmount)
                }
            }
            var highestCardValue = 0
            for (entry in count) {
                if (mapValue(entry.key) > highestCardValue) {
                    highestCardValue = mapValue(entry.key)
                }
            }
            return Hand(HandType.HIGHEST_CARD, cards, highestCardValue, bidAmount)
        }

        private fun mapValue(key: String): Int {
            if (key.toIntOrNull() != null) {
                return key.toInt()
            }
            if (key == "A") {
                return 14
            }//AKQJT
            if (key == "K") {
                return 13
            }
            if (key == "Q") {
                return 12
            }
            if (key == "J") {
                return 1
            }
            if (key == "T") {
                return 10
            }
            if (key == "") {
                return 1
            }
            throw RuntimeException("wtf is wrong? value=$key")
        }
    }

    override fun compareTo(other: Hand): Int {
        val thisType = this.type.ordinal
        val otherType = other.type.ordinal
        if (thisType != otherType) {
            return otherType - thisType // na odwrot bo 0 to najlepiej
        }
        for ((index, card) in this.cards.withIndex()) {
            val otherCard = other.cards[index]
            if (mapValue(card) != mapValue(otherCard)) {
                return mapValue(card) - mapValue(otherCard)
            }
        }
        throw RuntimeException("no same values allowed! \n$this AND \n$other")
    }

    override fun toString(): String {
        return "Hand(type=$type, cards=$cards, value=$value, bidAmount=$bidAmount)"
    }

}