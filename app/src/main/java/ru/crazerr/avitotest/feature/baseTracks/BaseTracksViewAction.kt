package ru.crazerr.avitotest.feature.baseTracks

import ru.crazerr.avitotest.domain.model.Track

sealed interface BaseTracksViewAction {
    data class UpdateSearchQuery(val search: String) : BaseTracksViewAction
    data class ClickTrack(val track: Track) : BaseTracksViewAction
    data object ClearSearchQuery : BaseTracksViewAction
}