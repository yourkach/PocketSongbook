package com.ybond.core_entities.models

sealed class InternalError : Throwable() {

    sealed class LoadSearchResultsError : InternalError() {
        class ParsingError(override val cause: Throwable? = null) : LoadSearchResultsError()
        class ConnectionError(override val cause: Throwable? = null) : LoadSearchResultsError()
    }

    class ParseSongPageError(override val cause: Throwable? = null) : InternalError()

}
