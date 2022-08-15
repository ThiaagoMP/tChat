package br.com.thiago.tchat.data.channels

enum class ChannelType(val key: String) {

    GLOBAL("global"), LOCAL("local"), SHOUT("shout"), WHISPER("whisper"), CUSTOM("custom")

}