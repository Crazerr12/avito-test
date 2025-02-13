package ru.crazerr.avitotest.feature.baseTracks

import ru.crazerr.avitotest.domain.model.PreviewTrack

sealed interface BaseTracksViewAction {
    data class UpdateSearchQuery(val search: String) : BaseTracksViewAction
    data class ClickTrack(val previewTrack: PreviewTrack) : BaseTracksViewAction
    data object ClearSearchQuery : BaseTracksViewAction
}