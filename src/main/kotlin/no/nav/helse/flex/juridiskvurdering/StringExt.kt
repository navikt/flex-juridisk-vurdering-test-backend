package no.nav.helse.flex.juridiskvurdering

fun String.erFnr(): Boolean = this.all { it.isDigit() } && this.length == 11
