package ru.crazerr.avitotest.feature.baseTracks

import ru.crazerr.avitotest.domain.model.Track
import ru.crazerr.avitotest.utils.presentation.MviViewModel

abstract class BaseTracksViewModel :
    MviViewModel<BaseTracksState, BaseTracksViewAction>(InitialBaseTracksState) {
    override fun handleAction(action: BaseTracksViewAction) {
        when (action) {
            is BaseTracksViewAction.ClickTrack -> onClickTrack(track = action.track)
            is BaseTracksViewAction.UpdateSearchQuery -> onUpdateSearchQuery(search = action.search)
            BaseTracksViewAction.ClearSearchQuery -> onClearSearchQuery()
        }
    }

    protected abstract fun onClickTrack(track: Track)

    protected abstract fun onUpdateSearchQuery(search: String)

    protected abstract fun onClearSearchQuery()
}