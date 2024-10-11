package com.example.seniorproject_icebreaker.data.model

enum class ActivityType {
    GAME,
    QUESTION
}

enum class GameActivity {
    FOUR_IN_A_ROW,
    TIC_TAC_TOE
}

data class WouldYouRatherQuestion(
    val question: String,
    val firstOption: String,
    val secondOption: String
)

fun WouldYouRatherQuestion.toMap(): Map<String, Any?> {
    return mapOf(
        "question" to question,
        "firstOption" to firstOption,
        "secondOption" to secondOption
    )
}

enum class QuestionActivity(val questions: List<Any>) {
    HYPOTHETICAL(
        listOf(
            "If you could have dinner with any historical figure, who would it be and why?",
            "If you were stranded on a deserted island and could only have one item, what would it be?",
            "If you could time travel to any period in history, where would you go and why?",
            "If you had the power to read minds for one day, whose mind would you read and what would you hope to find out?",
            "If you could switch lives with any fictional character for a week, who would it be and what would you do?",
            "If you were given $1 million to spend in 24 hours, but couldn't keep any of it, what would you spend it on?",
            "If you could instantly learn any skill or talent, what would it be and how would you use it?",
            "If you had the chance to have dinner with any three people, living or dead, who would they be and what would you ask them?",
            "If you had to relive one day of your life forever, which day would it be and what would you do differently each time?",
            "If you could live in any fictional universe, which one would you choose and why?",
            "If you could communicate with animals, which animal would you talk to first and what would you ask them?",
            "If you could only eat one type of cuisine for the rest of your life, what would it be and why?",
            "If you could have a conversation with your future self 20 years from now, what would you ask or tell them?",
            "If you were offered the chance to explore space but could never return to Earth, would you go? Why or why not?",
            "If you could live anywhere in the world without any restrictions, where would you choose and what kind of lifestyle would you have?",
            "If you could create a new holiday, what would it be called, when would it be celebrated, and how would people celebrate it?",
            "If you could erase one invention from history to make the world a better place, which one would it be and why?",
            "Suppose you woke up one day and everyone's emotions were visible as colors around them. How would this affect your interactions?",
            "What if you could experience the memories of anyone you touched? How would this change your understanding of people?",
            "If you could swap lives with any person in history for a week, who would you choose and why? What would you hope to experience or change during that time?"
        )
    ),
    WOULD_YOU_RATHER(
        listOf(
            WouldYouRatherQuestion(
                "Would you rather live without music or without television?",
                "Without music",
                "Without television"
            ),
            WouldYouRatherQuestion(
                "Would you rather eat finger nail or toe nail clippings?",
                "Finger nail",
                "Toe nail"
            ),
            WouldYouRatherQuestion(
                "Would you rather have unlimited money or unlimited wisdom?",
                "Unlimited money",
                "Unlimited wisdom"
            ),
            WouldYouRatherQuestion(
                "Would you rather live in the wilderness far from civilization or in a bustling city?",
                "Wilderness",
                "City"
            ),
            WouldYouRatherQuestion(
                "Would you rather always speak your mind or never speak again?",
                "Always speak",
                "Never speak"
            ),
            WouldYouRatherQuestion(
                "Would you rather have a rewind button for your life or a pause button?",
                "Rewind button",
                "Pause button"
            ),
            WouldYouRatherQuestion(
                "Would you rather experience the beginning of planet Earth or the end of planet Earth?",
                "The beginning",
                "The end"
            ),
            WouldYouRatherQuestion(
                "Would you rather have the ability to teleport anywhere in the world or be able to teleport to a fictional world of your choice?",
                "Our world",
                "Fictional world"
            ),
            WouldYouRatherQuestion(
                "Would you rather live in a world where everyone's memories reset every day, or where everyone can read each other's minds?",
                "Memory resets",
                "Read minds"
            ),
            WouldYouRatherQuestion(
                "Would you rather have the power to control the weather or the power to control time?",
                "Weather",
                "Time"
            ),
            WouldYouRatherQuestion(
                "Would you rather live without the internet for a year, or without your car for a year?",
                "Internet",
                "Car"
            ),
            WouldYouRatherQuestion(
                "Would you rather live in a world where everyone's dreams are visible to others on a screen when they wake up, or where everyone's browsing history is public?",
                "Dreams",
                "Browsing history"
            ),
            WouldYouRatherQuestion(
                "Would you rather have the ability to instantly learn any musical instrument or be a master of every sport?",
                "Instrument",
                "Sport"
            ),
            WouldYouRatherQuestion(
                "Would you rather have the power to instantly learn any new language fluently or have perfect photographic memory?",
                "Language fluency",
                "Photographic memory"
            ),
            WouldYouRatherQuestion(
                "Would you rather have the power to heal any physical injury or heal any emotional pain and suffering?",
                "Physical",
                "Emotional"
            ),
            WouldYouRatherQuestion(
                "Would you rather live in a world where everyone's dreams come true instantly or where everyone's nightmares come true?",
                "Dreams",
                "Nightmares"
            ),
            WouldYouRatherQuestion(
                "Would you rather have the ability to control fire or control ice?",
                "Fire",
                "Ice"
            ),
            WouldYouRatherQuestion(
                "Would you rather have the ability to understand and speak every human language fluently or be able to communicate with all animals?",
                "All languages",
                "All animals"
            ),
            WouldYouRatherQuestion(
                "Would you rather have the power to create a new holiday that everyone celebrates or be able to abolish one existing holiday?",
                "New holiday",
                "Abolish holiday"
            ),
            WouldYouRatherQuestion(
                "Would you rather have the ability to manipulate gravity or control the weather?",
                "Gravity",
                "Weather"
            ),
        )
    )
}

// Sealed class to encapsulate both types of activities
sealed class ActivityName {
    data class Game(val gameActivity: GameActivity) : ActivityName() {
        override fun toString(): String {
            return gameActivity.toString() // Use GameActivity's toString()
        }
    }

    data class Question(val questionActivity: QuestionActivity) : ActivityName() {
        override fun toString(): String {
            return questionActivity.toString() // Use QuestionActivity's toString()
        }
    }
}

data class Activity(val activityType: ActivityType, val activityName: ActivityName) {
    companion object {
        val allActivities: List<Activity> by lazy {
            val gameActivities = GameActivity.entries.map {
                Activity(ActivityType.GAME, ActivityName.Game(it))
            }
            val questionActivities = QuestionActivity.entries.map {
                Activity(ActivityType.QUESTION, ActivityName.Question(it))
            }
            gameActivities + questionActivities
        }
    }
}
